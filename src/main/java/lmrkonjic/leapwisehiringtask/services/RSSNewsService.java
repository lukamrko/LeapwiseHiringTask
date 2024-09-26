package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisRequestDTO;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import lmrkonjic.leapwisehiringtask.dtos.MainNewsDTO;
import org.springframework.stereotype.Service;

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
        var rssArticlesForURLs = rssDataService.fetchRSSArticlesForURLs(rssUrls);
        var analyzedData = hotTopicService.getMainNewsWithArticles(rssArticlesForURLs);

        databaseService.saveSessionWithData(analyzedData);

        return hotTopicService.getHotTopicsForAnalyzedData(analyzedData);
    }

    //TODO actual implementation
    public List<MainNewsDTO> fetchMostTrendingNewsForSessionID(Long sessionID) {
        List<MainNews> mostTrendingNews = databaseService.fetchMostTrendingNewsWithArticlesForSessionID(sessionID);
        return transformMainNewsToMainNewsDTO(mostTrendingNews);
    }

    //TODO actual implementation
    private List<MainNewsDTO> transformMainNewsToMainNewsDTO(List<MainNews> mostTrendingNews) {

        return null;
    }
}
