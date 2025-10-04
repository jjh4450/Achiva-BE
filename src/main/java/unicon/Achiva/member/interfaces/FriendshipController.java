package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicon.Achiva.global.response.ApiResponseForm;
import unicon.Achiva.member.domain.AuthService;
import unicon.Achiva.member.domain.FriendshipService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final AuthService authService;

    @Operation(summary = "친구 신청")
    @PostMapping("/api/friendships")
    public ResponseEntity<ApiResponseForm<FriendshipResponse>> sendFriendRequest(
            HttpServletRequest request,
            @RequestBody FriendshipRequest friendshipRequest
    ) {
        UUID memberId = authService.getMemberIdFromToken(request);
        FriendshipResponse response = friendshipService.sendFriendRequest(friendshipRequest, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "친구 신청 성공"));
    }

    @Operation(summary = "친구 신청 수락")
    @PatchMapping("/api/friendships/{friendshipId}/accept")
    public ResponseEntity<ApiResponseForm<FriendshipResponse>> acceptFriendRequest(
            HttpServletRequest request,
            @RequestParam Long friendshipId
    ) {
        UUID memberId = authService.getMemberIdFromToken(request);
        FriendshipResponse response = friendshipService.acceptFriendRequest(friendshipId, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "친구 신청 수락 성공"));
    }

    @Operation(summary = "친구 신청 거절")
    @PatchMapping("/api/friendships/{friendshipId}/reject")
    public ResponseEntity<ApiResponseForm<FriendshipResponse>> rejectFriendRequest(
            HttpServletRequest request,
            @RequestParam Long friendshipId
    ) {
        UUID memberId = authService.getMemberIdFromToken(request);
        FriendshipResponse response = friendshipService.rejectFriendRequest(friendshipId, memberId);
        return ResponseEntity.ok(ApiResponseForm.success(response, "친구 신청 거절 성공"));
    }

    @Operation(summary = "친구 목록 조회")
    @GetMapping("/api/friendships")
    public ResponseEntity<ApiResponseForm<List<FriendshipResponse>>> getFriendList(
            HttpServletRequest request
    ) {
        UUID memberId = authService.getMemberIdFromToken(request);
        List<FriendshipResponse> friendList = friendshipService.getFriends(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(friendList, "친구 목록 조회 성공"));
    }

    @Operation(summary = "특정 유저 친구 목록 조회")
    @GetMapping("/api/friendships/{memberId}")
    public ResponseEntity<ApiResponseForm<List<FriendshipResponse>>> getFriendListByMemberId(
            @PathVariable UUID memberId
    ) {
        List<FriendshipResponse> friendList = friendshipService.getFriends(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(friendList, "특정 유저 친구 목록 조회 성공"));
    }

    @Operation(summary = "특정 유저 친구 목록 조회 (닉네임으로)")
    @GetMapping("/api2/friendships/{nickname}")
    public ResponseEntity<ApiResponseForm<List<FriendshipResponse>>> getFriendListByNickname(
            @PathVariable String nickname
    ) {
        List<FriendshipResponse> friendList = friendshipService.getFriendsByNickname(nickname);
        return ResponseEntity.ok(ApiResponseForm.success(friendList, "닉네임으로 친구 목록 조회 성공"));
    }

    @Operation(summary = "내게 온 친구 신청 목록 조회")
    @GetMapping("/api/friendships/requests")
    public ResponseEntity<ApiResponseForm<List<FriendshipResponse>>> getFriendRequests(
            HttpServletRequest request
    ) {
        UUID memberId = authService.getMemberIdFromToken(request);
        List<FriendshipResponse> friendRequests = friendshipService.getFriendRequests(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(friendRequests, "친구 신청 목록 조회 성공"));
    }

    @Operation(summary = "내가 보낸 친구 신청 목록 조회")
    @GetMapping("/api/friendships/sent-requests")
    public ResponseEntity<ApiResponseForm<List<FriendshipResponse>>> getSentFriendRequests(
            HttpServletRequest request
    ) {
        UUID memberId = authService.getMemberIdFromToken(request);
        List<FriendshipResponse> sentRequests = friendshipService.getSentFriendRequests(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(sentRequests, "보낸 친구 신청 목록 조회 성공"));

    }
}
