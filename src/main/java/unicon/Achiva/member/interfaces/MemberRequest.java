package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberRequest {
    private String nickName;
    private String profileImageUrl;
    private String birth;
    private String gender;
    private String region;
    private List<String> categories;
}
