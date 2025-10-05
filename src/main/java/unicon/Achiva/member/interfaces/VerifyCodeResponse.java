package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class VerifyCodeResponse {
    private final String email;
    private final boolean verified;

    public VerifyCodeResponse(String email) {
        this.email = email;
        this.verified = true;
    }
}
