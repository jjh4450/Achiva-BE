package unicon.Achiva.member.interfaces;

import lombok.Getter;

@Getter
public class CheckPasswordRequest {
    private String email;
    private String password;
}
