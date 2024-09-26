package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.data.repositories.ArticleRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.MainNewsRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.RSSSiteRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.SessionRepository;
import lmrkonjic.leapwisehiringtask.dtos.AnalysisResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        // Logic to fetch, analyze RSS feeds, and store data

        // Create and save Session entity

    }

    public List<MainNews> fetchThreeMostTrendingNewsForSessionID(Long sessionID) {
        // Logic to fetch and return top three trending news based on the sessionID

        return new ArrayList<>(); // Replace with actual implementation
    }
}
