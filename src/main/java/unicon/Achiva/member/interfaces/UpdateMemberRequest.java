package unicon.Achiva.member.interfaces;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateMemberRequest {
    private String nickNameButDidntWork;
    private String profileImageUrl;
    private String birth;
    private String gender;
    private String region;
    private List<String> categories;
    private String description;
}
