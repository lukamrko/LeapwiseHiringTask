package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionID;

    private LocalDateTime sessionDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(sessionID, session.sessionID) && Objects.equals(sessionDateTime, session.sessionDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionID, sessionDateTime);
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionID=" + sessionID +
                ", sessionDateTime=" + sessionDateTime +
                '}';
    }
}
