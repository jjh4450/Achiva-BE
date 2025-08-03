package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class CheckEmailResponse {
    boolean available;

    public CheckEmailResponse(boolean available) {
        this.available = available;
    }
}
