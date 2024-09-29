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
    
    //Some URL-s are simply to big for standard varchar size
    @Column(length = 1023)
    private String articleURL;
    
    private String rssSiteURL;

    public Article() { }
}
