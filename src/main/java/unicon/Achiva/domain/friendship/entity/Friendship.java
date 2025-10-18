package unicon.Achiva.domain.friendship.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicon.Achiva.domain.friendship.FriendshipStatus;
import unicon.Achiva.global.common.LongBaseEntity;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship extends LongBaseEntity {

    @Column(nullable = false)
    private UUID requesterId;

    @Column
    private UUID receiverId;

    private FriendshipStatus status; // "PENDING", "ACCEPTED", "REJECTED"

    public void updateReceiverId(UUID recieverId) {
        this.receiverId = recieverId;
    }

    public void updateStatus(FriendshipStatus acceptStatus) {
        this.status = acceptStatus;
    }
}
