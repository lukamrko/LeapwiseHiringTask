package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisRequestDTO;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.ArticleDTO;
import lmrkonjic.leapwisehiringtask.dtos.MainNewsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

@Service
public class RSSNewsService {
    private final DatabaseService databaseService;
    private final HotTopicService hotTopicService;
    private final RSSDataService rssDataService;

    public RSSNewsService(
            DatabaseService databaseService,
            HotTopicService hotTopicService,
            RSSDataService rssDataService) {
        this.databaseService = databaseService;
        this.hotTopicService = hotTopicService;
        this.rssDataService = rssDataService;
    }

    //TODO actual implementation
    public AnalysisResultDTO analyzeRSSNews(AnalysisRequestDTO requestDTO) {
        List<String> rssUrls = requestDTO.getRssUrls();
        Dictionary<String, List<ArticleDTO>> rssArticlesForURLs = rssDataService.fetchRSSArticlesForURLs(rssUrls);
        List<MainNews> analyzedData = hotTopicService.getMainNewsWithArticles(rssArticlesForURLs);

        databaseService.saveSessionWithData(analyzedData);

        List<MainNews> hotMainNews =  hotTopicService.getHotMainNews(analyzedData);
        return transformHotMainNewsToAnalysisResultDTO(hotMainNews);
    }

    public List<MainNewsDTO> fetchMostTrendingNewsForSessionID(Long sessionID) {
        List<MainNews> mostTrendingNews = databaseService.fetchMostTrendingNewsWithArticlesForSessionID(sessionID);
        return transformMainNewsToMainNewsDTO(mostTrendingNews);
    }

    private AnalysisResultDTO transformHotMainNewsToAnalysisResultDTO(List<MainNews> hotMainNews) {
        var analysisResultDTO = new AnalysisResultDTO();
        var sessionID = hotMainNews.getFirst().getSession().getSessionID();

        var mainNewsDTO = transformMainNewsToMainNewsDTO(hotMainNews);

        analysisResultDTO.setSessionID(sessionID);
        analysisResultDTO.setHotTopics(mainNewsDTO);

        return analysisResultDTO;
    }

    private List<MainNewsDTO> transformMainNewsToMainNewsDTO(List<MainNews> mostTrendingNews) {
        List<MainNewsDTO> mainNewsDTOs = new ArrayList<>();
        for(var mainNews : mostTrendingNews) {
            var mainNewsDTO = new MainNewsDTO();
            mainNewsDTO.setMainNewsID(mainNews.getMainNewsID());
            mainNewsDTO.setMainNewsTitle(mainNews.getMainNewsTitle());

            List<ArticleDTO> articleDTOs = transformArticlesToArticleDTOs(mainNews.getArticles());
            mainNewsDTO.setArticles(articleDTOs);

            mainNewsDTOs.add(mainNewsDTO);
        }

        return mainNewsDTOs;
    }

    private List<ArticleDTO> transformArticlesToArticleDTOs(List<Article> articles) {
        List<ArticleDTO> articleDTOs = new ArrayList<>();
        for(var article : articles) {
            var articleDTO = new ArticleDTO();
            articleDTO.setArticleID(article.getArticleID());
            articleDTO.setArticleTitle(article.getArticleTitle());
            articleDTO.setArticleURL(article.getArticleURL());
            articleDTO.setRssSiteURL(article.getRssSiteURL());

            articleDTOs.add(articleDTO);
        }

        return articleDTOs;
    }


}
