package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class VerifyCodeResponse {
    private String email;
    private boolean verified;

    public VerifyCodeResponse(String email) {
        this.email = email;
        this.verified = true;
    }
}
