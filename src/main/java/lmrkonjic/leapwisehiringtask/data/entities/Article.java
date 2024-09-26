package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleID;

    @ManyToOne
    @JoinColumn(name="mainNewsID", nullable = false)
    private MainNews mainNews;

    @ManyToOne
    @JoinColumn(name = "rssID", nullable = false)
    private RSSSite rssSite;

    private String articleTitle;

    private  String articleURL;

    public Article() {  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleID, article.articleID) && Objects.equals(mainNews, article.mainNews) && Objects.equals(rssSite, article.rssSite) && Objects.equals(articleTitle, article.articleTitle) && Objects.equals(articleURL, article.articleURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleID, mainNews, rssSite, articleTitle, articleURL);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleID=" + articleID +
                ", mainNews=" + mainNews +
                ", rssSite=" + rssSite +
                ", articleTitle='" + articleTitle + '\'' +
                ", articleURL='" + articleURL + '\'' +
                '}';
    }
}
