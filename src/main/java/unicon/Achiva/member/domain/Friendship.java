package unicon.Achiva.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import unicon.Achiva.common.LongBaseEntity;

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
