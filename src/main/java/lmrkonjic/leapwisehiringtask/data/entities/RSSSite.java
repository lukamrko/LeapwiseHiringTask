package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
public class RSSSite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rssID;

    private String rssURL;

    @OneToMany(mappedBy = "rssSite", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles;

    public RSSSite() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RSSSite rssSite = (RSSSite) o;
        return Objects.equals(rssID, rssSite.rssID) && Objects.equals(rssURL, rssSite.rssURL) && Objects.equals(articles, rssSite.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rssID, rssURL, articles);
    }

    @Override
    public String toString() {
        return "RSSSite{" +
                "rssID=" + rssID +
                ", rssURL='" + rssURL + '\'' +
                ", articles=" + articles +
                '}';
    }
}
