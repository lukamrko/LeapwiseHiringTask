package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.data.repositories.ArticleRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.MainNewsRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.RSSSiteRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.SessionRepository;
import lmrkonjic.leapwisehiringtask.dtos.SessionDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseService {

    private final SessionRepository sessionRepository;

    private final MainNewsRepository mainNewsRepository;

    private final ArticleRepository articleRepository;

    private final RSSSiteRepository rssSiteRepository;

    public DatabaseService(SessionRepository sessionRepository, MainNewsRepository mainNewsRepository, ArticleRepository articleRepository, RSSSiteRepository rssSiteRepository) {
        this.sessionRepository = sessionRepository;
        this.mainNewsRepository = mainNewsRepository;
        this.articleRepository = articleRepository;
        this.rssSiteRepository = rssSiteRepository;
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
