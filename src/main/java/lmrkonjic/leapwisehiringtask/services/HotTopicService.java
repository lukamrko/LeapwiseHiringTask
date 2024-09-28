package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.QueryParser;
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
    
    //TODO actual implementation
    private static final float SIMILARITY_THRESHOLD = 2f;
    private static long tempIdCounter = 1;

    public List<MainNews> getMainNewsWithArticles(List<ArticleDTO> rssArticles) throws IOException {
        // Assign temporary IDs
        for (ArticleDTO rssArticle : rssArticles) {
            rssArticle.setArticleID(tempIdCounter++);
        }

        // Create in-memory Lucene index
        Directory indexDirectory = new ByteBuffersDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // Index the articles
        try (IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer))) {
            for (ArticleDTO rssArticle : rssArticles) {
                Document doc = new Document();
                doc.add(new TextField("title", rssArticle.getArticleTitle(), Field.Store.YES));
                doc.add(new StringField("id", rssArticle.getArticleID().toString(), Field.Store.YES));
                doc.add(new StringField("rssSite", rssArticle.getRssSiteURL(), Field.Store.YES));
                writer.addDocument(doc);
            }
        }

        List<MainNews> mainNewsList = new ArrayList<>();
        Set<Long> processedIds = new HashSet<>();

        try (IndexReader reader = DirectoryReader.open(indexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            MoreLikeThis mlt = new MoreLikeThis(reader);
            mlt.setAnalyzer(analyzer);
            mlt.setFieldNames(new String[]{"title"});
            mlt.setMinTermFreq(1);
            mlt.setMinDocFreq(1);

            for (ArticleDTO activeArticle : rssArticles) {
                if (processedIds.contains(activeArticle.getArticleID())) {
                    continue;
                }

                MainNews activeMainNews = createMainNewsFromArticle(activeArticle);
                processedIds.add(activeArticle.getArticleID());

                // Find similar articles
                Query query = mlt.like("title", new StringReader(activeArticle.getArticleTitle()));
                TopDocs topDocs = searcher.search(query, rssArticles.size());

                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    if (scoreDoc.score < SIMILARITY_THRESHOLD) {
                        continue;
                    }

                    Document doc = searcher.doc(scoreDoc.doc);
                    long docId = Long.parseLong(doc.get("id"));

                    if (processedIds.contains(docId) || activeArticle.getRssSiteURL().equals(doc.get("rssSite"))) {
                        continue;
                    }

                    ArticleDTO similarArticle = rssArticles.stream()
                            .filter(a -> a.getArticleID() == docId)
                            .findFirst()
                            .orElse(null);

                    if (similarArticle != null) {
                        activeMainNews.getArticles().add(convertToArticle(similarArticle, activeMainNews));
                        processedIds.add(docId);
                    }
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

    private static List<MainNews> mockMainNews() {
        // Mock RSS site
        String rssSite = "https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&output=rss";

// Create MainNews 1 with 3 articles
        MainNews mainNews1 = new MainNews();
        mainNews1.setMainNewsTitle("Main News 1");

        Article article1 = new Article();
        article1.setArticleTitle("Article 1-1");
        article1.setArticleURL("https://example.com/article1-1");
        article1.setMainNews(mainNews1);
        article1.setRssSiteURL(rssSite);

        Article article2 = new Article();
        article2.setArticleTitle("Article 1-2");
        article2.setArticleURL("https://example.com/article1-2");
        article2.setMainNews(mainNews1);
        article2.setRssSiteURL(rssSite);

        Article article3 = new Article();
        article3.setArticleTitle("Article 1-3");
        article3.setArticleURL("https://example.com/article1-3");
        article3.setMainNews(mainNews1);
        article3.setRssSiteURL(rssSite);

        mainNews1.setArticles(List.of(article1, article2, article3));

// Create MainNews 2 with 2 articles
        MainNews mainNews2 = new MainNews();
        mainNews2.setMainNewsTitle("Main News 2");

        Article article4 = new Article();
        article4.setArticleTitle("Article 2-1");
        article4.setArticleURL("https://example.com/article2-1");
        article4.setMainNews(mainNews2);
        article4.setRssSiteURL(rssSite);

        Article article5 = new Article();
        article5.setArticleTitle("Article 2-2");
        article5.setArticleURL("https://example.com/article2-2");
        article5.setMainNews(mainNews2);
        article5.setRssSiteURL(rssSite);

        mainNews2.setArticles(List.of(article4, article5));

// Create MainNews 3 with 2 articles
        MainNews mainNews3 = new MainNews();
        mainNews3.setMainNewsTitle("Main News 3");

        Article article6 = new Article();
        article6.setArticleTitle("Article 3-1");
        article6.setArticleURL("https://example.com/article3-1");
        article6.setMainNews(mainNews3);
        article6.setRssSiteURL(rssSite);

        Article article7 = new Article();
        article7.setArticleTitle("Article 3-2");
        article7.setArticleURL("https://example.com/article3-2");
        article7.setMainNews(mainNews3);
        article7.setRssSiteURL(rssSite);

        mainNews3.setArticles(List.of(article6, article7));

// Create MainNews 4 with 1 article
        MainNews mainNews4 = new MainNews();
        mainNews4.setMainNewsTitle("Main News 4");

        Article article8 = new Article();
        article8.setArticleTitle("Article 4-1");
        article8.setArticleURL("https://example.com/article4-1");
        article8.setMainNews(mainNews4);
        article8.setRssSiteURL(rssSite);

        mainNews4.setArticles(List.of(article8));

// Combine all MainNews into a list
        List<MainNews> mockMainNewsList = List.of(mainNews1, mainNews2, mainNews3, mainNews4);
        return mockMainNewsList;
    }

    //TODO maybe add option for overlap on all news sites
    public List<MainNews> getHotMainNews(List<MainNews> analyzedData) {

        List<MainNews> hotMainNews = new ArrayList<>();
        for(MainNews mainNews : analyzedData){
            if(mainNews.getArticles().size()>=2)
            {
                hotMainNews.add(mainNews);
            }
        }
        return hotMainNews;
    }
}
