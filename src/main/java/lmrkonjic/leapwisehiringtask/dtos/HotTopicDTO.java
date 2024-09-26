package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

import java.util.List;

@Data
public class HotTopicDTO {
    private String topic;
    private int frequency;
    private List<ArticleDTO> relatedArticles;
}
