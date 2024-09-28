package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotTopicService {
    
    //TODO actual implementation
    private static final float SIMILARITY_THRESHOLD = 2f;
    private static long tempIdCounter = 1;  // Temporary ID counter

    public List<MainNews> getMainNewsWithArticles(List<ArticleDTO> rssArticles) throws Exception {
        for(var rssArticle : rssArticles) {
            rssArticle.setArticleID(tempIdCounter++);
        }

        Directory indexDirectory = new ByteBuffersDirectory();  // In-memory directory
        StandardAnalyzer analyzer = new StandardAnalyzer();     // Analyzer for tokenizing the strings

        // Indexing the strings
        try (IndexWriter writer = new IndexWriter(indexDirectory, new IndexWriterConfig(analyzer)))
        {
            for(var rssArticle : rssArticles) {
                Document doc = new Document();
                doc.add(new TextField("title", rssArticle.getArticleTitle(), Field.Store.YES));
                doc.add(new TextField("id", rssArticle.getArticleID().toString(), Field.Store.YES));
                doc.add(new TextField("rssSite", rssArticle.getRssSiteURL(), Field.Store.YES));
                writer.addDocument(doc);
            }

        }

        List<MainNews> mainNews = new ArrayList<>();

        try (IndexReader reader = DirectoryReader.open(indexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            Set<String> usedIDs = new HashSet<>();
            while(!rssArticles.isEmpty())
            {
                var activeArticle = rssArticles.getFirst();
                usedIDs.add(activeArticle.getArticleID().toString());

                var activeMainNews = createMainNewsFromArticleWithoutArticles(activeArticle);

                QueryParser parser = new QueryParser("title", analyzer);

                Query titleQuery = parser.parse(QueryParser.escape(activeArticle.getArticleTitle()));

                // Step 2: Create a negative query to exclude documents from the same RSS site
                Query excludeRSSSiteQuery = new TermQuery(new Term("rssSite", activeArticle.getRssSiteURL()));

                // Step 3: Create a BooleanQuery for excluding used IDs
                BooleanQuery.Builder idExclusionBuilder = new BooleanQuery.Builder();
                for (String usedId : usedIDs) {
                    Query excludeIdQuery = new TermQuery(new Term("id", usedId));
                    idExclusionBuilder.add(excludeIdQuery, BooleanClause.Occur.MUST_NOT);
                }
                BooleanQuery idExclusionQuery = idExclusionBuilder.build();

                // Step 4: Combine all queries into a single BooleanQuery
                BooleanQuery combinedQuery = new BooleanQuery.Builder()
                        .add(titleQuery, BooleanClause.Occur.MUST)    // Must match similar titles
                        .add(excludeRSSSiteQuery, BooleanClause.Occur.MUST_NOT)  // Exclude same RSS site
                        .add(idExclusionQuery, BooleanClause.Occur.MUST_NOT)     // Exclude previously used IDs
                        .build();

                // Step 5: Perform the search, limiting to 100 results
                TopDocs topDocs = searcher.search(combinedQuery, 100);

                // Step 6: Extract the results
                Set<Long> IDsOfSimilarArticles = new HashSet<>();
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    Document doc = searcher.doc(scoreDoc.doc);
                    float score = scoreDoc.score;

                    // Only include results above the similarity threshold
                    if (score > SIMILARITY_THRESHOLD) {
                        var docID = doc.get("id");
                        IDsOfSimilarArticles.add(Long.parseLong(docID));
                    }
                }

                if(!IDsOfSimilarArticles.isEmpty())
                {
                    usedIDs.addAll(IDsOfSimilarArticles.stream()
                            .map(Object::toString)
                            .collect(Collectors.toSet()));

                    List<ArticleDTO> relatedArticleDTOs = rssArticles.stream()
                            .filter(article -> IDsOfSimilarArticles.contains(article.getArticleID()))
                            .collect(Collectors.toList());

                    rssArticles.removeAll(relatedArticleDTOs);

                    List<Article> relatedArticles = new ArrayList<>();
                    for(ArticleDTO articleDTO : relatedArticleDTOs)
                    {
                        var article = convertToArticle(articleDTO, activeMainNews);
                        relatedArticles.add(article);
                    }
                    activeMainNews.getArticles().addAll(relatedArticles);
                }
                mainNews.add(activeMainNews);
                rssArticles.removeFirst();
            }
        }

        return mainNews;
    }


    private MainNews createMainNewsFromArticleWithoutArticles(ArticleDTO articleDTO) {
        MainNews mainNews = new MainNews();
        mainNews.setMainNewsTitle(articleDTO.getArticleTitle());
        mainNews.setArticles(new ArrayList<>());
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


//    public List<MainNews> getMainNewsWithArticles(Map<String, List<ArticleDTO>> rssArticlesForURLs) throws Exception {
//        List<MainNews> mainNewsList = new ArrayList<>();
//        Directory indexDirectory = new ByteBuffersDirectory();
//        StandardAnalyzer analyzer = new StandardAnalyzer();
//        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        IndexWriter writer = new IndexWriter(indexDirectory, config);
//
//        // Index all articles
//        for (Map.Entry<String, List<ArticleDTO>> entry : rssArticlesForURLs.entrySet()) {
//            for (ArticleDTO articleDTO : entry.getValue()) {
//                indexArticle(writer, articleDTO);
//            }
//        }
//        writer.close();
//
//        // Search for similar articles and group them
//        IndexReader reader = DirectoryReader.open(indexDirectory);
//        IndexSearcher searcher = new IndexSearcher(reader);
//        QueryParser parser = new QueryParser("title", analyzer);
//
//        Set<Long> processedArticles = new HashSet<>();
//
//        for (Map.Entry<String, List<ArticleDTO>> entry : rssArticlesForURLs.entrySet()) {
//            for (ArticleDTO articleDTO : entry.getValue()) {
//                if (!processedArticles.contains(getTemporaryOrActualID(articleDTO))) {
//                    MainNews mainNews = createMainNewsFromArticle(articleDTO);
//                    List<Article> similarArticles = findSimilarArticles(searcher, parser, articleDTO, rssArticlesForURLs, processedArticles);
//                    mainNews.getArticles().addAll(similarArticles);
//                    mainNewsList.add(mainNews);
//                }
//            }
//        }
//
//        reader.close();
//        indexDirectory.close();
//
//        return mainNewsList;
//    }
//
//    private void indexArticle(IndexWriter writer, ArticleDTO articleDTO) throws IOException {
//        Document doc = new Document();
//        doc.add(new TextField("title", articleDTO.getArticleTitle(), Field.Store.YES));
//        doc.add(new TextField("id", getTemporaryOrActualID(articleDTO).toString(), Field.Store.YES));
//        writer.addDocument(doc);
//    }
//

//
//    private List<Article> findSimilarArticles(
//            IndexSearcher searcher,
//            QueryParser parser,
//            ArticleDTO sourceArticle,
//            Map<String, List<ArticleDTO>> rssArticlesForURLs,
//            Set<Long> processedArticles) throws Exception {
//
//        List<Article> similarArticles = new ArrayList<>();
//        Query query = parser.parse(QueryParser.escape(sourceArticle.getArticleTitle()));
//        TopDocs topDocs = searcher.search(query, 100);
//
//        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//            Document doc = searcher.doc(scoreDoc.doc);
//            Long articleId = Long.parseLong(doc.get("id"));
//            float similarityScore = scoreDoc.score;
//
//            // Check if article is not the source article, hasn't been processed, and passes the similarity threshold
//            if (!articleId.equals(getTemporaryOrActualID(sourceArticle))
//                    && !processedArticles.contains(articleId)
//                    && similarityScore > SIMILARITY_THRESHOLD) {
//
//                // Log similarity score (optional for debugging)
//                System.out.println("Found similar article with score: " + similarityScore);
//
//                ArticleDTO similarArticleDTO = findArticleDTOById(articleId, rssArticlesForURLs);
//                if (similarArticleDTO != null) {
//                    similarArticles.add(convertToArticle(similarArticleDTO, null));
//                    processedArticles.add(articleId);  // Mark article as processed
//                }
//            }
//        }
//        return similarArticles;
//    }
//

//
//    // Implementing the method to find ArticleDTO by its ID
//    private ArticleDTO findArticleDTOById(Long articleId, Map<String, List<ArticleDTO>> rssArticlesForURLs) {
//        for (List<ArticleDTO> articles : rssArticlesForURLs.values()) {
//            for (ArticleDTO article : articles) {
//                if (getTemporaryOrActualID(article).equals(articleId)) {
//                    return article;
//                }
//            }
//        }
//        return null;  // Return null if no article with the given ID is found
//    }
//
//    // Helper method to generate a temporary ID if the ArticleDTO has a null ID
//    private Long getTemporaryOrActualID(ArticleDTO articleDTO) {
//        if (articleDTO.getArticleID() != null) {
//            return articleDTO.getArticleID();
//        } else {
//            return generateTemporaryID();
//        }
//    }
//
//    // Synchronized method to generate unique temporary IDs
//    private synchronized Long generateTemporaryID() {
//        return tempIdCounter++;
//    }

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
