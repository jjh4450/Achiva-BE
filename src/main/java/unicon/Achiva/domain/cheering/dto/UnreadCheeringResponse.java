package unicon.Achiva.domain.cheering.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnreadCheeringResponse {
    private Long unreadCheeringCount;
}
