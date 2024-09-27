package lmrkonjic.leapwisehiringtask.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MainNewsDTO {
    private Long mainNewsID;
    private String mainNewsTitle;
    private List<ArticleDTO> articles;

    public int getFrequency() {
        return articles != null ? articles.size() : 0;
    }
}