package unicon.Achiva.global.security.login.handler;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Category;

import java.util.List;

@Getter
@Builder
public class LoginResponse {
    private Long id;
    private String email;
    private String nickName;
    private String birth;
    private String gender;
    private List<String> categories;
    private String createdAt;
}
