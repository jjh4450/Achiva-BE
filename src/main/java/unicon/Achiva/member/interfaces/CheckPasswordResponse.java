package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class CheckPasswordResponse {
    private boolean isMatch;

    public CheckPasswordResponse(boolean isMatch) {
        this.isMatch = isMatch;
    }
}
