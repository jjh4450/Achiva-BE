package unicon.Achiva.domain.friendship.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FriendshipRequest {
    @org.hibernate.validator.constraints.UUID
    private UUID recieverId;
}
