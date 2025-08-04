package unicon.Achiva.member.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import unicon.Achiva.member.application.MemberService;
import unicon.Achiva.member.domain.AuthService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/api/members/me")
    public ResponseEntity<MemberResponse> getMyInfo(HttpServletRequest request) {
        Long memberId = authService.getMemberIdFromToken(request);
        MemberResponse memberResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberResponse);
    }

}
