package lmrkonjic.leapwisehiringtask.data.repositories;

import lmrkonjic.leapwisehiringtask.data.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {}