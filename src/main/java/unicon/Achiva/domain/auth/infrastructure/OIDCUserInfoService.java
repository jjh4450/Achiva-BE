package unicon.Achiva.domain.auth.infrastructure;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class OIDCUserInfoService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;
    private String userInfoEndpoint;

    @PostConstruct
    public void init() {
        String wellKnownEndpoint = issuerUri + "/.well-known/openid-configuration";
        try {
            Map<String, String> configuration = restTemplate.getForObject(wellKnownEndpoint, Map.class);
            if (configuration != null && configuration.containsKey("userinfo_endpoint")) {
                this.userInfoEndpoint = configuration.get("userinfo_endpoint");
                log.info("Successfully loaded userinfo_endpoint: {}", this.userInfoEndpoint);
            } else {
                log.error("Could not find 'userinfo_endpoint' in OIDC configuration from {}", wellKnownEndpoint);
            }
        } catch (RestClientException e) {
            log.error("Failed to fetch OIDC configuration from {}", wellKnownEndpoint, e);
        }
    }

    /**
     * [UserInfo Endpoint] Access Token으로 UserInfo 엔드포인트를 호출하여 전체 사용자 정보를 가져옵니다.
     *
     * @param accessToken 사용자의 Access Token
     * @return 사용자 정보가 담긴 Map (sub, email, username 등)
     */
    public Optional<Map<String, Object>> getUserInfo(String accessToken) {
        if (userInfoEndpoint == null || userInfoEndpoint.isEmpty()) {
            log.error("userInfoEndpoint is not configured. Cannot fetch user info.");
            return Optional.empty();
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoEndpoint,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody());
            } else {
                log.warn("Failed to fetch user info. Status code: {}", response.getStatusCode());
            }
        } catch (RestClientException e) {
            // 401 Unauthorized (토큰 만료/무효) 등 RestTemplate 관련 예외 처리
            log.error("Error occurred while fetching user info from endpoint: {}", userInfoEndpoint, e);
        }
        return Optional.empty();
    }

    /**
     * [UserInfo Endpoint] Access Token을 사용해 UserInfo 엔드포인트에서 이메일만 추출합니다.
     * 내부적으로 getUserInfo(accessToken) 메서드를 호출하여 재사용합니다.
     *
     * @param accessToken 사용자의 Access Token
     * @return 이메일 주소
     */
    public Optional<String> getEmailFromUserInfo(String accessToken) {
        // 기존 getUserInfo 메서드를 호출하여 사용자 정보 Map을 가져온다.
        return getUserInfo(accessToken)
                .map(userInfo -> (String) userInfo.get("email"));
    }

    /**
     * [UserInfo Endpoint] Access Token을 사용해 UserInfo 엔드포인트에서 이메일만 추출합니다.
     * 내부적으로 getUserInfo(accessToken) 메서드를 호출하여 재사용합니다.
     * JWT 토큰은 SecurityContextHolder에서 자동으로 가져옵니다.
     *
     * @return 이메일 주소
     */
    public Optional<String> getEmailFromUserInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return Optional.empty();
        }
        String accessToken = jwtAuth.getToken().getTokenValue();
        return getEmailFromUserInfo(accessToken);
    }
}