package unicon.Achiva.domain.auth.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
public class UpdateMemberRequest {
    @Size(min = 2, max = 20)
    private String nickName;
    @URL(protocol = "https")
    private String profileImageUrl;
    private String birth;
    private String gender;
    private String region;
    private List<String> categories;
    private String description;
}
