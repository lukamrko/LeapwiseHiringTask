package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RSSNewsService {


    private final DatabaseService databaseService;
    private final HotTopicSearchService hotTopicSearchService;
    private final RSSDataService rssDataService;

    public RSSNewsService(
            DatabaseService databaseService,
            HotTopicSearchService hotTopicSearchService,
            RSSDataService rssDataService) {
        this.databaseService = databaseService;
        this.hotTopicSearchService = hotTopicSearchService;
        this.rssDataService = rssDataService;
    }

    public AnalysisResultDTO analyzeRSSNews(String queryURL) {
        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();
        return  analysisResultDTO;

    }

    public List<MainNews> fetchMostTrendingNewsForSessionID(Long sessionID) {
        // Logic to fetch and return top three trending news based on the sessionID

        return new ArrayList<>(); // Replace with actual implementation
    }
}
