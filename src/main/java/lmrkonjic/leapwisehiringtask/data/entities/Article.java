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

    private String articleTitle;

    private  String articleURL;

    private String rssSiteURL;

    public Article() {  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleID, article.articleID) && Objects.equals(mainNews, article.mainNews) && Objects.equals(articleTitle, article.articleTitle) && Objects.equals(articleURL, article.articleURL) && Objects.equals(rssSiteURL, article.rssSiteURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleID, mainNews, articleTitle, articleURL, rssSiteURL);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleID=" + articleID +
                ", mainNews=" + mainNews +
                ", articleTitle='" + articleTitle + '\'' +
                ", articleURL='" + articleURL + '\'' +
                ", rssSiteURL='" + rssSiteURL + '\'' +
                '}';
    }
}
