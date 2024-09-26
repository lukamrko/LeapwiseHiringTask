package lmrkonjic.leapwisehiringtask.services;

import lmrkonjic.leapwisehiringtask.data.entities.Session;
import lmrkonjic.leapwisehiringtask.data.repositories.ArticleRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.MainNewsRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.RSSSiteRepository;
import lmrkonjic.leapwisehiringtask.data.repositories.SessionRepository;
import lmrkonjic.leapwisehiringtask.dtos.SessionDTO;

import java.time.LocalDateTime;

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

    public void saveSessionData(SessionDTO sessionDTO) {
        Session session = new Session();
        session.setSessionDateTime(LocalDateTime.now());
        sessionRepository.save(session);

        // Placeholder return for session ID (unique identifier)
        long number = session.getSessionID();
    }
}
