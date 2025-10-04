package unicon.Achiva.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import unicon.Achiva.global.security.login.filter.UserInitializedAuthorizationManager;
import unicon.Achiva.global.security.login.handler.UserNotInitializedDeniedHandler;

import java.util.HashSet;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * SecurityFilterChain
     * Cognito OIDC JWT 검증을 표준 Resource Server로 활성화하고,
     * 기존 인가 규칙은 그대로 유지한다.
     */
    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http,
                                           JwtDecoder jwtDecoder,
                                           Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter,
                                           UserInitializedAuthorizationManager initManager,
                                           UserNotInitializedDeniedHandler deniedHandler) throws Exception {

        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(f -> f.disable()))
                .authorizeHttpRequests(auth -> auth
                        // 정적/유틸
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // 문서
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 공개 인증 API (명시적 나열)
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/check-email",
                                "/api/auth/check-nickname",
                                "/api/auth/send-verification-code",
                                "/api/auth/verify-code"
                        ).permitAll()
                        // 공개 파일 업로드용 사전서명 URL
                        .requestMatchers("/api/members/presigned-url").permitAll()
                        // CORS preflight 안정성
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 그 외 보호
                        .anyRequest().access(initManager)
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthConverter)
                        )
                ).exceptionHandling(ex -> ex
                        .accessDeniedHandler(deniedHandler)
                );

        return http.build();
    }

    /**
     * jwtDecoder
     * Cognito Issuer 기반으로 NimbusJwtDecoder를 생성하고,
     * 기본 검증(서명/만료/발급자)에 선택적으로 audience 검증을 추가한다.
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuer,
                                 @Value("${app.security.expected-audience:}") String expectedAudience) {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);

        if (expectedAudience == null || expectedAudience.isBlank()) {
            decoder.setJwtValidator(withIssuer);
        } else {
            OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<>("aud", aud -> {
                if (aud instanceof String a) return a.equals(expectedAudience);
                if (aud instanceof java.util.Collection<?> c) return c.contains(expectedAudience);
                return false;
            });
            decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        }
        return decoder;
    }

    /**
     * jwtAuthConverter
     * scope(claim) 및 cognito:groups(claim)를 Spring Security 권한으로 변환한다.
     * - scope -> SCOPE_xxx
     * - cognito:groups -> ROLE_<GROUP>
     */
    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
        scopes.setAuthorityPrefix("SCOPE_");

        return jwt -> {
            var authorities = new HashSet<>(scopes.convert(jwt));
            var groups = jwt.getClaimAsStringList("cognito:groups");
            if (groups != null) {
                groups.forEach(g ->
                        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + g))
                );
            }
            String principalName = resolveUsername(jwt);
            return new JwtAuthenticationToken(jwt, authorities, principalName);
        };
    }

    /**
     * resolveUsername
     * Principal 표시용 사용자명을 email -> cognito:username -> sub 순으로 결정한다.
     */
    private String resolveUsername(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email != null) return email;
        String username = jwt.getClaimAsString("cognito:username");
        if (username != null) return username;
        return jwt.getSubject();
    }

    /**
     * corsConfigurationSource
     * CORS 설정을 적용한다. 운영 환경에 맞는 Origin 화이트리스트를 권장한다.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 쿠키 포함 허용
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
