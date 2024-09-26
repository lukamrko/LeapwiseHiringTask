package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

import java.util.List;

@Data
class FrequencyResponseDTO {
    private List<HotTopicDTO> topThreeTopics;
}