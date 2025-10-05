package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicon.Achiva.common.S3Service;
import unicon.Achiva.global.response.ApiResponseForm;
import unicon.Achiva.member.domain.ArticleService;
import unicon.Achiva.member.domain.MemberService;
import unicon.Achiva.member.domain.AuthService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ArticleService articleService;
    private final AuthService authService;
    private final S3Service s3Service;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/api/members/me")
    public ResponseEntity<ApiResponseForm<MemberResponse>> getMyInfo() {
        UUID memberId = authService.getMemberIdFromToken();
        MemberResponse memberResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(memberResponse, "내 정보 조회 성공"));
    }

    @Operation(summary = "특정 유저 정보 조회")
    @GetMapping("/api/members/{memberId}")
    public ResponseEntity<ApiResponseForm<MemberResponse>> getMemberInfo(
            @PathVariable UUID memberId
    ) {
        MemberResponse memberResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(memberResponse, "유저 정보 조회 성공"));
    }

    @Operation(summary = "닉네임으로 유저 정보 조회")
    @GetMapping("/api2/members/{nickname}")
    public ResponseEntity<ApiResponseForm<MemberResponse>> getMemberInfoByNickname(
            @PathVariable String nickname
    ) {
        MemberResponse memberResponse = memberService.getMemberInfoByNickname(nickname);
        return ResponseEntity.ok(ApiResponseForm.success(memberResponse, "닉네임으로 유저 정보 조회 성공"));
    }

    @Operation(summary = "닉네임으로 유저 목록 검색")
    @GetMapping("/api/members")
    public ResponseEntity<ApiResponseForm<Page<MemberResponse>>> getMembers(
            SearchMemberCondition condition,
            Pageable pageable
    ) {
        Page<MemberResponse> members = memberService.getMembers(condition, pageable);
        return ResponseEntity.ok(ApiResponseForm.success(members, "닉네임으로 유저 검색 성공"));
    }

    @Operation(summary = "유저 프로필 사진 저장용 presigned URL 발급(이후 회원가입이나 프로필이미지 수정시 쿼리파라미터를 제외한 url을 BE에 보내야함.) - JWT 필요 X")
    @GetMapping("/api/members/presigned-url")
    public ResponseEntity<ApiResponseForm<Map<String, String>>> getPresignedUrl(
            @RequestParam(defaultValue = "application/octet-stream") String contentType
    ) {
        String url = s3Service.generatePresignedUrl(contentType);
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(ApiResponseForm.success(response, "Presigned URL 발급 성공"));
    }

    @GetMapping("/api/members/{memberId}/count-by-category")
    public ResponseEntity<ApiResponseForm<CategoryCountResponse>> getArticleCountByCategory(
            @RequestParam Long memberId
    ) {
        CategoryCountResponse result = articleService.getArticleCountByCategory(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(result, "카테고리별 작성 수 조회 성공"));
    }

//    @Operation(summary = "프로필 이미지 수정 API. presigned URL 발급 및 업로드가 선행되어야 함.")
//    @PutMapping("/api/members/confirm-upload")
//    public ResponseEntity<ApiResponseForm<Map<String, Boolean>>> confirmProfileImageUpload(
//            @RequestBody ConfirmProfileImageUploadRequest confirmProfileImageUploadRequest,
//            HttpServletRequest request
//    ) {
//        Long memberId = authService.getMemberIdFromToken(request);
//        memberService.updateProfileImageUrl(memberId, confirmProfileImageUploadRequest);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("updated", true);
//        return ResponseEntity.ok(ApiResponseForm.success(response, "프로필 사진 업데이트 성공"));
//    }
}
