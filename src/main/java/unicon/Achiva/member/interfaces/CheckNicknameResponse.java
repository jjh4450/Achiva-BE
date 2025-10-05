package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class CheckNicknameResponse {
    private final boolean available;

    public CheckNicknameResponse(boolean available) {
        this.available = available;
    }
}
