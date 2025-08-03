package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class ResetPasswordResponse {
    private String email;
    private boolean reset;

    public ResetPasswordResponse(String email) {
        this.email = email;
        this.reset = true;
    }
}
