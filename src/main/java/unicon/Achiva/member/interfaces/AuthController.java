package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unicon.Achiva.global.response.ApiResponseForm;
import unicon.Achiva.member.domain.AuthService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "자체 회원가입. presigned URL 발급 및 업로드가 선행되어야 함. - JWT 필요 X")
    @PostMapping("api/auth/register")
    public ResponseEntity<ApiResponseForm<CreateMemberResponse>> signup(@RequestBody CreateMemberRequest requestDto) {
        CreateMemberResponse createMemberResponse = authService.signup(requestDto);
        return ResponseEntity.ok(ApiResponseForm.created(createMemberResponse, "회원가입 성공"));
    }

    @Operation(summary = "회원탈퇴(유저 정보 삭제)")
    @DeleteMapping("api/auth/delete")
    public ResponseEntity<ApiResponseForm<Void>> deleteMember(HttpServletRequest request) {
        Long memberId = authService.getMemberIdFromToken(request);
        authService.deleteMember(memberId);
        return ResponseEntity.ok(ApiResponseForm.success(null, "회원 탈퇴 성공"));
    }

    @Operation(summary = "이메일 중복 체크 - JWT 필요 X")
    @GetMapping("api/auth/check-email")
    public ResponseEntity<ApiResponseForm<CheckEmailResponse>> checkEmailDuplication(@RequestParam String email) {
        CheckEmailResponse checkEmailResponse = authService.validateDuplicateEmail(email);
        return ResponseEntity.ok(ApiResponseForm.success(checkEmailResponse, "이메일 중복 확인 성공"));
    }

    @Operation(summary = "닉네임 중복 체크 - JWT 필요 X")
    @GetMapping("api/auth/check-nickname")
    public ResponseEntity<ApiResponseForm<CheckNicknameResponse>> checkNicknameDuplication(@RequestParam String nickname) {
        CheckNicknameResponse checkNicknameResponse = authService.validateDuplicateNickName(nickname);
        return ResponseEntity.ok(ApiResponseForm.success(checkNicknameResponse, "닉네임 중복 확인 성공"));
    }

    @Operation(summary = "이메일로 인증코드 전송 - JWT 필요 X")
    @PostMapping("api/auth/send-verification-code")
    public ResponseEntity<ApiResponseForm<SendVerificationCodeResponse>> sendCode(@RequestParam String email) {
        SendVerificationCodeResponse sendVerificationCodeResponse = authService.sendVerificationCode(email);
        return ResponseEntity.ok(ApiResponseForm.success(sendVerificationCodeResponse ,"인증 코드 전송 성공"));
    }

    @Operation(summary = "받은 이메일 인증코드로 인증 - JWT 필요 X")
    @PostMapping("api/auth/verify-code")
    public ResponseEntity<ApiResponseForm<VerifyCodeResponse>> verifyCode(@RequestParam String email,
                                             @RequestParam String code) {
        VerifyCodeResponse verifyCodeResponse = authService.verifyCode(email, code);
        return ResponseEntity.ok(ApiResponseForm.success(verifyCodeResponse, "인증 코드 확인 성공"));
    }

    @Operation(summary = "비밀번호 초기화")
    @PostMapping("api/auth/reset-password")
    public ResponseEntity<ApiResponseForm<ResetPasswordResponse>> resetPassword(@RequestBody ResetPasswordRequest request) {
        ResetPasswordResponse resetPasswordResponse = authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponseForm.success(resetPasswordResponse, "비밀번호 재설정 성공"));
    }

    @Operation(summary = "로그인 - JWT 필요 X")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일반 로그인 성공", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n  \"status\": \"success\", \"code\": 200, \"message\": \"로그인 성공\", \"data\": {\"id\": 1, \"email\": \"user@example.com\", \"nickname\": \"user\", \"birth\": \"2000-01-01\", \"gender\": \"MALE\", \"categories\": [\"공부\", \"운동\"], \"createdAt\": \"2023-01-01T00:00:00.000000\"}}"))),
    })
    @PostMapping("/api/auth/login")
    public String login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "로그인 요청 JSON 데이터",
                    required = true,
                    content = @Content(
                            schema = @Schema(type = "object", example = "{\"email\": \"user@example.com\", \"password\": \"password1234\"}")
                    )
            )
            @RequestBody Map<String, String> loginRequest) {
        return "로그인 성공"; // 실제 로그인 처리는 Security 필터에서 수행
    }
}
