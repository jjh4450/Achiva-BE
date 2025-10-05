package unicon.Achiva.global.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class UserNotInitializedDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper om = new ObjectMapper();

    /**
     * 초기화 미완료 차단 시 428과 표준 JSON 바디를 응답합니다.
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(428);
        response.setContentType("application/json");
        Map<String, Object> body = Map.of(
                "code", "USER_NOT_INITIALIZED",
                "message", "Initialization required"
        );
        om.writeValue(response.getWriter(), body);
    }
}