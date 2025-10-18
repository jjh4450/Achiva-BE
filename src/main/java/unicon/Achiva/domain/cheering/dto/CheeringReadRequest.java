package unicon.Achiva.domain.cheering.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CheeringReadRequest {
    private List<Long> cheeringIds;
}
