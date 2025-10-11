package unicon.Achiva.member.interfaces;

import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
public class ConfirmProfileImageUploadRequest {
    @URL(protocol = "https")
    private String url;
}
