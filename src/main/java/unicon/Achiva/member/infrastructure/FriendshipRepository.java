package unicon.Achiva.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicon.Achiva.member.domain.Friendship;
import unicon.Achiva.member.domain.FriendshipStatus;

import java.util.Collection;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByReceiverIdAndStatus(Long memberId, FriendshipStatus friendshipStatus);

    List<Friendship> findByRequesterIdOrReceiverId(Long memberId, Long memberId1);

    List<Friendship> findByRequesterIdAndStatus(Long memberId, FriendshipStatus friendshipStatus);

    @Query("""
        select case when f.requesterId = :me then f.receiverId else f.requesterId end
        from Friendship f
        where f.status = unicon.Achiva.member.domain.FriendshipStatus.ACCEPTED
          and (f.requesterId = :me or f.receiverId = :me)
    """)
    List<Long> findFriendIdsOf(@Param("me") Long me);
}
