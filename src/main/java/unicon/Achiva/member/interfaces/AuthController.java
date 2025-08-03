package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @PostMapping("api/auth/register")
    public ResponseEntity<ApiResponseForm<CreateMemberResponse>> signup(@RequestBody CreateMemberRequest requestDto) {
        CreateMemberResponse createMemberResponse = authService.signup(requestDto);
        return ResponseEntity.ok(ApiResponseForm.created(createMemberResponse, "회원가입 성공"));
    }

    @GetMapping("api/auth/check-email")
    public ResponseEntity<ApiResponseForm<CheckEmailResponse>> checkEmailDuplication(@RequestParam String email) {
        CheckEmailResponse checkEmailResponse = authService.validateDuplicateEmail(email);
        return ResponseEntity.ok(ApiResponseForm.success(checkEmailResponse, "이메일 중복 확인 성공"));
    }

    @GetMapping("api/auth/check-nickname")
    public ResponseEntity<ApiResponseForm<CheckNicknameResponse>> checkNicknameDuplication(@RequestParam String nickname) {
        CheckNicknameResponse checkNicknameResponse = authService.validateDuplicateNickName(nickname);
        return ResponseEntity.ok(ApiResponseForm.success(checkNicknameResponse, "닉네임 중복 확인 성공"));
    }

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
