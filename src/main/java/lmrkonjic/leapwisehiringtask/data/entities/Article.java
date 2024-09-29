package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleID;

    @ManyToOne
    @JoinColumn(name = "mainNewsID", nullable = false)
    private MainNews mainNews;

    private String articleTitle;
    
    @Column(length = 1000)
    private String articleURL;
    private String rssSiteURL;

    public Article() { }
}
