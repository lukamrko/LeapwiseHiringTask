package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.data.repositories.ArticleRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.MainNewsRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    @Value("${news.top-count}")
    private int topCount;

    private final SessionRepository sessionRepository;

    private final MainNewsRepository mainNewsRepository;

    private final ArticleRepository articleRepository;


    public DatabaseService(SessionRepository sessionRepository, MainNewsRepository mainNewsRepository, ArticleRepository articleRepository) {
        this.sessionRepository = sessionRepository;
        this.mainNewsRepository = mainNewsRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public void saveSessionWithData(List<MainNews> analyzedData) {
        Session session = new Session();
        session.setSessionDateTime(LocalDateTime.now());

        //Saving session separately for the performance reasons
        sessionRepository.save(session);

        for (MainNews mainNews : analyzedData) {
            mainNews.setSession(session);
        }

        mainNewsRepository.saveAll(analyzedData);
    }

    @Transactional(readOnly = true)
    public List<MainNews> fetchMostTrendingNewsWithArticlesForSessionID(Long sessionID) {
        Pageable pageable = PageRequest.of(0, topCount); // Get the first 3 results
        return mainNewsRepository.findTopMainNewsForSessionID(sessionID, pageable);
    }
}
