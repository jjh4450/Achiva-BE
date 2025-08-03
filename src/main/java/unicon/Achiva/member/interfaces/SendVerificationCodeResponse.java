package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class SendVerificationCodeResponse {
    private String email;
    private boolean sended;

    public SendVerificationCodeResponse(String email) {
        this.email = email;
        this.sended = true;
    }
}
