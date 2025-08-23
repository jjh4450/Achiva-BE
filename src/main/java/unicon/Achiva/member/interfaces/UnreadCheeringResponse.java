package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnreadCheeringResponse {
    private Long unreadCheeringCount;
}
