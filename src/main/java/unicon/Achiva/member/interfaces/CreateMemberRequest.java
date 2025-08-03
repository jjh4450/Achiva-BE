package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CreateMemberRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String nickName;
    private String profileImageUrl;
    private String birth;
    private String gender;
    private String region;
    private List<String> categories;
}
