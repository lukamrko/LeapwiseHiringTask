package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.data.repositories.ArticleRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.MainNewsRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.RSSSiteRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RSSNewsService {

    private final SessionRepository sessionRepository;

    private final MainNewsRepository mainNewsRepository;

    private final ArticleRepository articleRepository;

    private final RSSSiteRepository rssSiteRepository;

    public RSSNewsService(SessionRepository sessionRepository, MainNewsRepository mainNewsRepository, ArticleRepository articleRepository, RSSSiteRepository rssSiteRepository) {
        this.sessionRepository = sessionRepository;
        this.mainNewsRepository = mainNewsRepository;
        this.articleRepository = articleRepository;
        this.rssSiteRepository = rssSiteRepository;
    }

    public Long analyzeRSSNews(String queryURL) {
        // Logic to fetch, analyze RSS feeds, and store data

        // Create and save Session entity
        Session session = new Session();
        session.setSessionDateTime(LocalDateTime.now());
        sessionRepository.save(session);

        // Placeholder return for session ID (unique identifier)
        return session.getSessionID();
    }

    public List<MainNews> fetchThreeMostTrendingNewsForSessionID(Long sessionID) {
        // Logic to fetch and return top three trending news based on the sessionID

        return new ArrayList<>(); // Replace with actual implementation
    }
}
