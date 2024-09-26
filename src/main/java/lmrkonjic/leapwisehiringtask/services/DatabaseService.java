package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.data.repositories.ArticleRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.MainNewsRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    private final SessionRepository sessionRepository;

    private final MainNewsRepository mainNewsRepository;

    private final ArticleRepository articleRepository;


    public DatabaseService(SessionRepository sessionRepository, MainNewsRepository mainNewsRepository, ArticleRepository articleRepository) {
        this.sessionRepository = sessionRepository;
        this.mainNewsRepository = mainNewsRepository;
        this.articleRepository = articleRepository;
    }

    //TODO actual implementation
    public void saveSessionWithData(List<MainNews> analyzedData) {
        Session session = new Session();
    }

    //TODO actual implementation
    public List<MainNews> fetchMostTrendingNewsWithArticlesForSessionID(Long sessionID) {

        return null;
    }
}
