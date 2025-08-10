package unicon.Achiva.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.member.domain.Friendship;
import unicon.Achiva.member.domain.FriendshipStatus;

import java.util.Collection;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByReceiverIdAndStatus(Long memberId, FriendshipStatus friendshipStatus);

    List<Friendship> findByRequesterIdOrReceiverId(Long memberId, Long memberId1);

    List<Friendship> findByRequesterIdAndStatus(Long memberId, FriendshipStatus friendshipStatus);
}
