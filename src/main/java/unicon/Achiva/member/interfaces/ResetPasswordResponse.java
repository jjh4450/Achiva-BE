package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class ResetPasswordResponse {
    private final String email;
    private final boolean reset;

    public ResetPasswordResponse(String email) {
        this.email = email;
        this.reset = true;
    }
}
