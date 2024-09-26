package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisRequestDTO {
    private List<String> rssUrls;
}
