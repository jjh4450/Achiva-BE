package unicon.Achiva.global.security.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import unicon.Achiva.global.response.ApiResponseForm;

import java.io.IOException;

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
        var status = 428;
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        ApiResponseForm<?> failResponse = ApiResponseForm.error(
                status,
                "USER_NOT_INITIALIZED plz request user singing endpoint"
        );
        om.writeValue(response.getWriter(), failResponse);
    }
}