package unicon.Achiva.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import unicon.Achiva.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship extends BaseEntity {

    @Column(nullable = false)
    private Long requesterId;

    @Column
    private Long receiverId;

    private FriendshipStatus status; // "PENDING", "ACCEPTED", "REJECTED"

    public void updateReceiverId(Long recieverId) {
        this.receiverId = recieverId;
    }

    public void updateStatus(FriendshipStatus acceptStatus) {
        this.status = acceptStatus;
    }
}
