package unicon.Achiva.domain.friendship.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicon.Achiva.domain.friendship.FriendshipStatus;
import unicon.Achiva.domain.friendship.entity.Friendship;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByReceiverIdAndStatus(UUID memberId, FriendshipStatus friendshipStatus);

    List<Friendship> findByRequesterIdOrReceiverId(UUID memberId, UUID memberId1);

    List<Friendship> findByRequesterIdAndStatus(UUID memberId, FriendshipStatus friendshipStatus);

    @Query("""
            select case
                when f.requesterId = :me then f.receiverId
                else f.requesterId
            end
            from Friendship f
            where (f.requesterId = :me or f.receiverId = :me)
              and f.status = :status
            """)
    List<UUID> findFriendIdsOf(@Param("me") UUID me,
                               @Param("status") FriendshipStatus status);
}
