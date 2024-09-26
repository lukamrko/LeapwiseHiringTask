package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionID;

    private LocalDateTime sessionDateTime;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MainNews> mainNews;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(sessionID, session.sessionID) && Objects.equals(sessionDateTime, session.sessionDateTime) && Objects.equals(mainNews, session.mainNews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionID, sessionDateTime, mainNews);
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionID=" + sessionID +
                ", sessionDateTime=" + sessionDateTime +
                ", mainNews=" + mainNews +
                '}';
    }
}
