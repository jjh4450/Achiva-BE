package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class SendVerificationCodeResponse {
    private final String email;
    private final boolean sended;

    public SendVerificationCodeResponse(String email) {
        this.email = email;
        this.sended = true;
    }
}
