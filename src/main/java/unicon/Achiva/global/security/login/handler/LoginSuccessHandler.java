//package unicon.Achiva.global.security.login.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.transaction.annotation.Transactional;
//import unicon.Achiva.global.response.ApiResponseForm;
//import unicon.Achiva.global.security.jwt.utils.JwtTokenProvider;
//import unicon.Achiva.member.domain.Category;
//import unicon.Achiva.member.infrastructure.MemberRepository;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final MemberRepository memberRepository;
//
//    @Value("${jwt.access.expiration}")
//    private String accessTokenExpiration;
//
//    @Override
//    @Transactional
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) {
//        log.info("login success handler 초기 진입");
//        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출
//        memberRepository.findByEmail(email)
//                .ifPresent(user -> {
//                    // email과 userId기반으로 엑세스토큰 생성
//                    String accessToken = jwtTokenProvider.createAccessToken(email, user.getId());
//                    jwtTokenProvider.sendAccessToken(response, accessToken);
//
//                    log.info("로그인에 성공하였습니다. 이메일 : {}", email);
//                    log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
//                    log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
//
//                    try {
//                        // 응답 Content-Type 설정
//                        response.setContentType("application/json");
//                        response.setCharacterEncoding("UTF-8");
//
//                        LoginResponse loginResponse = LoginResponse.builder()
//                                .id(user.getId())
//                                .email(user.getEmail())
//                                .nickName(user.getNickName())
//                                .birth(String.valueOf(user.getBirth()))
//                                .gender(String.valueOf(user.getGender()))
//                                .categories(
//                                        user.getCategories().stream()
//                                                .map(Category::getDescription)
//                                                .toList()
//                                )
//                                .createdAt(String.valueOf(user.getCreatedAt()))
//                                .build();
//
//                        ApiResponseForm loginApiResponse = ApiResponseForm.success(loginResponse, "로그인 성공");
//
//                        // 응답 바디에 작성
//                        ObjectMapper objectMapper = new ObjectMapper();
//                        objectMapper.writeValue(response.getWriter(), loginApiResponse);
//                    } catch (IOException e) {
//                        log.error("응답 바디 작성 중 오류 발생", e);
//                    }
//                });
//    }
//
//    private String extractUsername(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return userDetails.getUsername();
//    }
//}