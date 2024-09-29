package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotTopicService {
	
	private static final String documentTitle = "title";
	private static final String documentID = "id";
	private static final String documentRssSite = "rssSite";
	
//	private static final float SIMILARITY_THRESHOLD = 0.94f;
	private static final float SIMILARITY_THRESHOLD = 1.9f;
	private static long tempIdCounter = 1;
	
	private record ScoredArticle(ArticleDTO article, float score) {
	}
	
	public List<MainNews> getMainNewsWithArticles(List<ArticleDTO> rssArticles) throws IOException {
		// Assign temporary IDs
		for (ArticleDTO rssArticle : rssArticles) {
			rssArticle.setArticleID(tempIdCounter++);
		}
		
		// Create in-memory Lucene index
		Directory indexDirectory = new ByteBuffersDirectory();
		Analyzer analyzer = new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(String fieldName) {
				Tokenizer source = new StandardTokenizer();
				TokenStream filter = new LowerCaseFilter(source);
				filter = new StopFilter(filter, EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);  // Remove English stopwords
				filter = new PorterStemFilter(filter);  // Apply stemming
				return new TokenStreamComponents(source, filter);
			}
		};
		// Index the articles
		try (IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer))) {
			for (ArticleDTO rssArticle : rssArticles) {
				Document doc = new Document();
				doc.add(new TextField(documentTitle, rssArticle.getArticleTitle(), Field.Store.YES));
				doc.add(new StringField(documentID, rssArticle.getArticleID().toString(), Field.Store.YES));
				doc.add(new StringField(documentRssSite, rssArticle.getRssSiteURL(), Field.Store.YES));
				writer.addDocument(doc);
			}
		}
		
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
					if (scoreDoc.score < SIMILARITY_THRESHOLD) {
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
					.collect(Collectors.groupingBy(
						sa -> sa.article.getRssSiteURL(),
						Collectors.collectingAndThen(
							Collectors.maxBy(Comparator.comparingDouble(sa -> sa.score)),
							optionalSa -> optionalSa.map(sa -> sa.article).orElse(null)
						)
					));
				
				for (ArticleDTO bestArticle : bestArticlePerSite.values()) {
					activeMainNews.getArticles().add(convertToArticle(bestArticle, activeMainNews));
					processedIds.add(bestArticle.getArticleID());
				}
				
				mainNewsList.add(activeMainNews);
			}
		}
		
		return mainNewsList;
	}
	
	private MainNews createMainNewsFromArticle(ArticleDTO articleDTO) {
		MainNews mainNews = new MainNews();
		mainNews.setMainNewsTitle(articleDTO.getArticleTitle());
		mainNews.setArticles(new ArrayList<>(Collections.singletonList(convertToArticle(articleDTO, mainNews))));
		return mainNews;
	}
	
	private Article convertToArticle(ArticleDTO articleDTO, MainNews mainNews) {
		Article article = new Article();
		article.setArticleTitle(articleDTO.getArticleTitle());
		article.setArticleURL(articleDTO.getArticleURL());
		article.setRssSiteURL(articleDTO.getRssSiteURL());
		article.setMainNews(mainNews);
		return article;
	}
	
	//TODO maybe add option for overlap on all news sites
	public List<MainNews> getHotMainNews(List<MainNews> analyzedData) {
		List<MainNews> hotMainNews = new ArrayList<>();
		for (MainNews mainNews : analyzedData) {
			if (mainNews.getArticles().size() >= 2) {
				hotMainNews.add(mainNews);
			}
		}
		return hotMainNews;
	}
}
