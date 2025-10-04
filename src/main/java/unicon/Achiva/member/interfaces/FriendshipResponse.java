package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Friendship;
import unicon.Achiva.member.domain.FriendshipStatus;

import java.util.UUID;

@Getter
@Builder
public class FriendshipResponse {
    private Long id;
    private UUID requesterId;
    private UUID receiverId;
    private FriendshipStatus status;

    public static FriendshipResponse fromEntity(Friendship friendship) {
        return FriendshipResponse.builder()
                .id(friendship.getId())
                .requesterId(friendship.getRequesterId())
                .receiverId(friendship.getReceiverId())
                .status(friendship.getStatus())
                .build();
    }
}
