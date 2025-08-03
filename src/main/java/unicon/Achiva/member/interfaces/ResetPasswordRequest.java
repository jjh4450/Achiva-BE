package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String email;
    private String newPassword;
    private String confirmPassword;

    public ResetPasswordRequest(String email, String newPassword, String confirmPassword) {
        this.email = email;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

}
