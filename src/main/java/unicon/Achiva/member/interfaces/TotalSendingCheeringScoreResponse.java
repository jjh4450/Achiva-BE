package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class TotalSendingCheeringScoreResponse {
    private Long totalSendingCheeringScore;

    public TotalSendingCheeringScoreResponse(Long totalSendingCheeringScore) {
        this.totalSendingCheeringScore = totalSendingCheeringScore;
    }
}
