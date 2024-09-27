package lmrkonjic.leapwisehiringtask.data.repositories;

import lmrkonjic.leapwisehiringtask.data.entities.MainNews;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MainNewsRepository extends JpaRepository<MainNews, Long> {

    @Query("SELECT m FROM MainNews m " +
            "LEFT JOIN FETCH m.articles " +
            "WHERE m.session.sessionID = :sessionID " +
            "ORDER BY SIZE(m.articles) DESC")
    List<MainNews> findTopMainNewsForSessionID(@Param("sessionID") Long sessionID, Pageable pageable);
}