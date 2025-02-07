package lmrkonjic.leapwisehiringtask.data.repositories;

import lmrkonjic.leapwisehiringtask.data.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {}