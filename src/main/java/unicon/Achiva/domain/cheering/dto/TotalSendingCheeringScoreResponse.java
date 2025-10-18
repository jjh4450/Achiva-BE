package unicon.Achiva.domain.cheering.dto;

import lombok.Getter;

@Getter
public class TotalSendingCheeringScoreResponse {
    private final Long totalSendingCheeringScore;

    public TotalSendingCheeringScoreResponse(Long totalSendingCheeringScore) {
        this.totalSendingCheeringScore = totalSendingCheeringScore;
    }
}
