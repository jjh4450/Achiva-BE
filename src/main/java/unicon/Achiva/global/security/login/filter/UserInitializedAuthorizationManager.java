package unicon.Achiva.global.security.login.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import unicon.Achiva.member.domain.MemberService;

import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UserInitializedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final MemberService memberService;

    /**
     * JWT의 sub가 Member에 없으면 접근 거부 결정을 반환합니다.
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth instanceof JwtAuthenticationToken token) {
            UUID sub = UUID.fromString(token.getToken().getSubject());
            return new AuthorizationDecision(memberService.existsById(sub));
        }
        return new AuthorizationDecision(true);
    }
}
