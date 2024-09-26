package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class RSSSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rssID;

    private String rssURL;

    public RSSSite() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RSSSite rssSite = (RSSSite) o;
        return Objects.equals(rssID, rssSite.rssID) && Objects.equals(rssURL, rssSite.rssURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rssID, rssURL);
    }

    @Override
    public String toString() {
        return "RSSSite{" +
                "rssID=" + rssID +
                ", rssURL='" + rssURL + '\'' +
                '}';
    }
}
