package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import org.springframework.stereotype.Service;

import java.util.Dictionary;
import java.util.List;

@Service
public class HotTopicService {
    
    //TODO actual implementation
    public List<MainNews> getMainNewsWithArticles(Dictionary<String, List<ArticleDTO>> rssArticlesForURLs) {
        //        List<MainNews> mainNews = new ArrayList<>();
        List<MainNews> mainNews = mockMainNews();
        return  mainNews;
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
        article1.setRssSite(rssSite);

        Article article2 = new Article();
        article2.setArticleTitle("Article 1-2");
        article2.setArticleURL("https://example.com/article1-2");
        article2.setMainNews(mainNews1);
        article2.setRssSite(rssSite);

        Article article3 = new Article();
        article3.setArticleTitle("Article 1-3");
        article3.setArticleURL("https://example.com/article1-3");
        article3.setMainNews(mainNews1);
        article3.setRssSite(rssSite);

        mainNews1.setArticles(List.of(article1, article2, article3));

// Create MainNews 2 with 2 articles
        MainNews mainNews2 = new MainNews();
        mainNews2.setMainNewsTitle("Main News 2");

        Article article4 = new Article();
        article4.setArticleTitle("Article 2-1");
        article4.setArticleURL("https://example.com/article2-1");
        article4.setMainNews(mainNews2);
        article4.setRssSite(rssSite);

        Article article5 = new Article();
        article5.setArticleTitle("Article 2-2");
        article5.setArticleURL("https://example.com/article2-2");
        article5.setMainNews(mainNews2);
        article5.setRssSite(rssSite);

        mainNews2.setArticles(List.of(article4, article5));

// Create MainNews 3 with 2 articles
        MainNews mainNews3 = new MainNews();
        mainNews3.setMainNewsTitle("Main News 3");

        Article article6 = new Article();
        article6.setArticleTitle("Article 3-1");
        article6.setArticleURL("https://example.com/article3-1");
        article6.setMainNews(mainNews3);
        article6.setRssSite(rssSite);

        Article article7 = new Article();
        article7.setArticleTitle("Article 3-2");
        article7.setArticleURL("https://example.com/article3-2");
        article7.setMainNews(mainNews3);
        article7.setRssSite(rssSite);

        mainNews3.setArticles(List.of(article6, article7));

// Create MainNews 4 with 1 article
        MainNews mainNews4 = new MainNews();
        mainNews4.setMainNewsTitle("Main News 4");

        Article article8 = new Article();
        article8.setArticleTitle("Article 4-1");
        article8.setArticleURL("https://example.com/article4-1");
        article8.setMainNews(mainNews4);
        article8.setRssSite(rssSite);

        mainNews4.setArticles(List.of(article8));

// Combine all MainNews into a list
        List<MainNews> mockMainNewsList = List.of(mainNews1, mainNews2, mainNews3, mainNews4);
        return mockMainNewsList;
    }

    //TODO actual implementation
    public AnalysisResultDTO getHotTopicsForAnalyzedData(List<MainNews> analyzedData) {
        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();

        return analysisResultDTO;
    }
}
