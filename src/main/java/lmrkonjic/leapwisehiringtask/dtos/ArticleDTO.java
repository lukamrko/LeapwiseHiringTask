package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

@Data
public class ArticleDTO {
    private Long articleID;
    private String articleTitle;
    private String articleURL;
    private String rssSiteURL;
}
