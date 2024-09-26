package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
public class MainNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long mainNewsID;

    @ManyToOne
    @JoinColumn(name = "sessionID", nullable = false)
    private Session session;

    @OneToMany(mappedBy = "mainNews", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles;

    private String mainNewsTitle;

    public MainNews() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainNews mainNews = (MainNews) o;
        return mainNewsID == mainNews.mainNewsID && Objects.equals(session, mainNews.session) && Objects.equals(mainNewsTitle, mainNews.mainNewsTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainNewsID, session, mainNewsTitle);
    }

    @Override
    public String toString() {
        return "MainNews{" +
                "mainNewsID=" + mainNewsID +
                ", session=" + session +
                ", mainNewsTitle='" + mainNewsTitle + '\'' +
                '}';
    }
}
