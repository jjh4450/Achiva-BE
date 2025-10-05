package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private final String email;

    public ResetPasswordRequest(String email, String newPassword, String confirmPassword) {
        this.email = email;
    }

}
