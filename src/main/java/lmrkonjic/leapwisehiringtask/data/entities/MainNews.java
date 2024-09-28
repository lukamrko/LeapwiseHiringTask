package lmrkonjic.leapwisehiringtask.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
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

    public MainNews() { }
}
