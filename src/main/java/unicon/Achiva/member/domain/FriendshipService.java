package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.FriendshipRepository;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.FriendshipRequest;
import unicon.Achiva.member.interfaces.FriendshipResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public FriendshipResponse sendFriendRequest(FriendshipRequest friendshipRequest, UUID fromMemberId) {
        UUID toMemberId = friendshipRequest.getRecieverId();

        if (!memberRepository.existsById(fromMemberId) || !memberRepository.existsById(toMemberId)) {
            throw new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Friendship friendship = Friendship.builder()
                .requesterId(fromMemberId)
                .receiverId(toMemberId)
                .status(FriendshipStatus.PENDING)
                .build();

        friendshipRepository.save(friendship);
        return FriendshipResponse.fromEntity(friendship);
    }

    @Transactional
    public FriendshipResponse acceptFriendRequest(Long friendshipId, UUID memberId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new GeneralException(FriendshipErrorCode.FRIENDSHIP_NOT_FOUND));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new GeneralException(FriendshipErrorCode.FRIENDSHIP_ALREADY_PROCESSED);
        }

        if (!friendship.getReceiverId().equals(memberId)) {
            throw new GeneralException(FriendshipErrorCode.FRIENDSHIP_NOT_RECEIVER);
        }

        friendship.updateStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
        return FriendshipResponse.fromEntity(friendship);
    }

    @Transactional
    public FriendshipResponse rejectFriendRequest(Long friendshipId, UUID memberId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new GeneralException(FriendshipErrorCode.FRIENDSHIP_NOT_FOUND));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new GeneralException(FriendshipErrorCode.FRIENDSHIP_ALREADY_PROCESSED);
        }

        if (!friendship.getReceiverId().equals(memberId)) {
            throw new GeneralException(FriendshipErrorCode.FRIENDSHIP_NOT_RECEIVER);
        }

        friendship.updateStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
        return FriendshipResponse.fromEntity(friendship);
    }

    public List<FriendshipResponse> getFriendRequests(UUID memberId) {
        return friendshipRepository.findByReceiverIdAndStatus(memberId, FriendshipStatus.PENDING)
                .stream()
                .map(FriendshipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FriendshipResponse> getSentFriendRequests(UUID memberId) {
        return friendshipRepository.findByRequesterIdAndStatus(memberId, FriendshipStatus.PENDING)
                .stream()
                .map(FriendshipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FriendshipResponse> getFriends(UUID memberId) {
        return friendshipRepository.findByRequesterIdOrReceiverId(memberId, memberId)
                .stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED)
                .map(FriendshipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FriendshipResponse> getFriendsByNickname(String nickname) {
        UUID memberId = memberRepository.findByNickName(nickname)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND))
                .getId();

        return friendshipRepository.findByRequesterIdOrReceiverId(memberId, memberId)
                .stream()
                .filter(friendship -> friendship.getStatus() == FriendshipStatus.ACCEPTED)
                .map(FriendshipResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFriendship(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new GeneralException(FriendshipErrorCode.FRIENDSHIP_NOT_FOUND));

        friendshipRepository.delete(friendship);
    }
}
