package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class TotalReceivedCheeringScoreResponse {
    private final Long totalReceivedCheeringScore;

    public TotalReceivedCheeringScoreResponse(Long totalReceivedCheeringScore) {
        this.totalReceivedCheeringScore = totalReceivedCheeringScore;
    }
}
