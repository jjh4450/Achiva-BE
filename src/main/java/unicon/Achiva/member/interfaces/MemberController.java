package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicon.Achiva.common.S3Service;
import unicon.Achiva.global.response.ApiResponseForm;
import unicon.Achiva.member.application.MemberService;
import unicon.Achiva.member.domain.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final S3Service s3Service;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/api/members/me")
    public ResponseEntity<ApiResponseForm<MemberResponse>> getMyInfo(HttpServletRequest request) {
        Long memberId = authService.getMemberIdFromToken(request);
        MemberResponse memberResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(memberResponse, "내 정보 조회 성공"));
    }

    @Operation(summary = "유저 프로필 사진 저장용 presigned URL 발급")
    @GetMapping("/api/members/presigned-url")
    public ResponseEntity<ApiResponseForm<Map<String, String>>> getPresignedUrl(
            @RequestParam(defaultValue = "application/octet-stream") String contentType
    ) {
        String url = s3Service.generatePresignedUrl(contentType);
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(ApiResponseForm.success(response, "Presigned URL 발급 성공"));
    }

    @Operation(summary = "presigned URL로 프로필 사진 업데이트 후, 이미지 url(presignedURL에서 쿼리파라미터 뗀 주소) BE로 전달")
    @PostMapping("/api/members/confirm-upload")
    public ResponseEntity<ApiResponseForm<Map<String, Boolean>>> confirmProfileImageUpload(
            @RequestBody ConfirmProfileImageUploadRequest confirmProfileImageUploadRequest,
            HttpServletRequest request
    ) {
        Long memberId = authService.getMemberIdFromToken(request);
        memberService.updateProfileImageUrl(memberId, confirmProfileImageUploadRequest);
        Map<String, Boolean> response = new HashMap<>();
        response.put("updated", true);
        return ResponseEntity.ok(ApiResponseForm.success(response, "프로필 사진 업데이트 성공"));
    }
}
