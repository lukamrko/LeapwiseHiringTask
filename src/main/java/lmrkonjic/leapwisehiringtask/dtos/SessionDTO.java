package lmrkonjic.leapwisehiringtask.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SessionDTO {
    private Long sessionID;
    private LocalDateTime sessionDateTime;
    private List<MainNewsDTO> mainNews;
}
