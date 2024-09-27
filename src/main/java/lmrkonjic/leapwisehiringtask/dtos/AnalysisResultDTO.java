package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisResultDTO {
    private Long sessionID;
    private List<MainNewsDTO> hotTopics;
}
