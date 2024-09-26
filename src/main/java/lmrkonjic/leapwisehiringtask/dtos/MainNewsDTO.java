package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

import java.util.List;

@Data
public class MainNewsDTO {
    private Long mainNewsID;
    private String mainNewsTitle;
    private List<ArticleDTO> articles;
}
