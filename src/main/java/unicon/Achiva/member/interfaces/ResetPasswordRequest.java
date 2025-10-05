package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private final String email;
    private final String newPassword;
    private final String confirmPassword;

    public ResetPasswordRequest(String email, String newPassword, String confirmPassword) {
        this.email = email;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

}
