package unicon.Achiva.global.security.login.filter;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import unicon.Achiva.member.infrastructure.MemberRepository;

import java.util.UUID;
import java.util.function.Supplier;

@Component
public class UserInitializedAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final MemberRepository memberRepository;

    public UserInitializedAuthorizationManager(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * JWT의 sub가 Member에 없으면 접근 거부 결정을 반환합니다.
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth instanceof JwtAuthenticationToken token) {
            UUID sub = UUID.fromString(token.getToken().getSubject());
            boolean exists = memberRepository.existsById(sub);
            return new AuthorizationDecision(exists);
        }
        // 비JWT는 여기서 판단하지 않음(다른 체인에 위임)
        return new AuthorizationDecision(true);
    }
}
