package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.records.ScoredArticle;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class HotTopicService {
	
	private static final String documentTitle = "title";
	private static final String documentID = "id";
	private static final String documentRssSite = "rssSite";
	
	@Value("${analyze.similarity-threshold}")
	private float similarityThreshold;
	
	@Value("${analyze.min-frequency-to-be-hot-news}")
	private int minFrequencyToBeHotNews;
	
	public List<MainNews> getHotMainNews(List<MainNews> analyzedData) {
		List<MainNews> hotMainNews = new ArrayList<>();
		for (MainNews mainNews : analyzedData) {
			if (mainNews.getArticles().size() >= minFrequencyToBeHotNews) {
				hotMainNews.add(mainNews);
			}
		}
		return hotMainNews;
	}
	
	public List<MainNews> getMainNewsWithArticles(List<ArticleDTO> rssArticles) throws IOException {
		assignTemporaryIDsToArticles(rssArticles);
		
		// Create in-memory Lucene index
		Directory indexDirectory = new ByteBuffersDirectory();
		Analyzer analyzer = getAnalyzerForMainNews();
		indexTheArticles(rssArticles, indexDirectory, analyzer);
		
		List<MainNews> mainNewsList = new ArrayList<>();
		Set<Long> processedIds = new HashSet<>();
		
		try (IndexReader reader = DirectoryReader.open(indexDirectory)) {
			IndexSearcher searcher = new IndexSearcher(reader);
			MoreLikeThis mlt = new MoreLikeThis(reader);
			mlt.setAnalyzer(analyzer);
			mlt.setFieldNames(new String[]{documentTitle});
			mlt.setMinTermFreq(1);
			mlt.setMinDocFreq(1);
			
			for (ArticleDTO activeArticle : rssArticles) {
				if (processedIds.contains(activeArticle.getArticleID())) {
					continue;
				}
				
				MainNews activeMainNews = createMainNewsFromArticle(activeArticle);
				processedIds.add(activeArticle.getArticleID());
				
				// Find similar articles
				Query query = mlt.like(documentTitle, new StringReader(activeArticle.getArticleTitle()));
				TopDocs topDocs = searcher.search(query, rssArticles.size());
				
				List<ScoredArticle> similarArticles = new ArrayList<>();
				for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
					if (scoreDoc.score < similarityThreshold) {
						continue;
					}
					
					Document doc = searcher.storedFields().document(scoreDoc.doc);
					long docId = Long.parseLong(doc.get(documentID));
					String rssSite = doc.get(documentRssSite);
					
					if (processedIds.contains(docId)
						|| activeArticle.getRssSiteURL().equals(rssSite)) {
						continue;
					}
					
					rssArticles.stream()
						.filter(a -> a.getArticleID() == docId)
						.findFirst()
						.ifPresent(similarArticle -> similarArticles
							.add(new ScoredArticle(similarArticle, scoreDoc.score)));
				}
				
				// Select the best article from each RSS site
				Map<String, ArticleDTO> bestArticlePerSite = similarArticles.stream()
					.collect(Collectors.toMap(
						sa -> sa.article().getRssSiteURL(),
						sa -> sa,
						BinaryOperator.maxBy(Comparator.comparingDouble(ScoredArticle::score))
					))
					.values().stream()
					.collect(Collectors.toMap(
						sa -> sa.article().getRssSiteURL(),
						ScoredArticle::article
					));
				
				for (ArticleDTO bestArticleDTO : bestArticlePerSite.values()) {
					activeMainNews.getArticles().add(convertArticleDTOToArticle(bestArticleDTO, activeMainNews));
					processedIds.add(bestArticleDTO.getArticleID());
				}
				
				mainNewsList.add(activeMainNews);
			}
		}
		
		return mainNewsList;
	}
	
	private void assignTemporaryIDsToArticles(List<ArticleDTO> rssArticles) {
		long tempIdCounter = 1;
		for (ArticleDTO rssArticle : rssArticles) {
			rssArticle.setArticleID(tempIdCounter++);
		}
	}
	
	private void indexTheArticles(List<ArticleDTO> rssArticles, Directory indexDirectory, Analyzer analyzer) throws IOException {
		try (IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer))) {
			for (ArticleDTO rssArticle : rssArticles) {
				Document doc = new Document();
				doc.add(new TextField(documentTitle, rssArticle.getArticleTitle(), Field.Store.YES));
				doc.add(new StringField(documentID, rssArticle.getArticleID().toString(), Field.Store.YES));
				doc.add(new StringField(documentRssSite, rssArticle.getRssSiteURL(), Field.Store.YES));
				writer.addDocument(doc);
			}
		}
	}
	
	/**
	 * @return Analyzer with removed English stopwords and applied stemming
	 */
	private Analyzer getAnalyzerForMainNews() {
		return new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				Tokenizer source = new StandardTokenizer();
				TokenStream filter = new LowerCaseFilter(source);
				filter = new StopFilter(filter, EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
				filter = new PorterStemFilter(filter);
				return new TokenStreamComponents(source, filter);
			}
		};
	}
	
	private MainNews createMainNewsFromArticle(ArticleDTO articleDTO) {
		MainNews mainNews = new MainNews();
		mainNews.setMainNewsTitle(articleDTO.getArticleTitle());
		Article article = convertArticleDTOToArticle(articleDTO, mainNews);
		ArrayList<Article> articles = new ArrayList<>(Collections.singletonList(article));
		mainNews.setArticles(articles);
		return mainNews;
	}
	
	private Article convertArticleDTOToArticle(ArticleDTO articleDTO, MainNews mainNews) {
		//ID isn't set because at the moment it is fake
		Article article = new Article();
		article.setArticleTitle(articleDTO.getArticleTitle());
		article.setArticleURL(articleDTO.getArticleURL());
		article.setRssSiteURL(articleDTO.getRssSiteURL());
		article.setMainNews(mainNews);
		return article;
	}
	

}
