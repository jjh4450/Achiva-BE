package unicon.Achiva.member.interfaces;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginRequest) {
        return "로그인 성공"; // 실제 로그인 처리는 Security 필터에서 수행
    }
}
