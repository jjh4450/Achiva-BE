package unicon.Achiva.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String code;

    private boolean verified = false;

    private LocalDateTime expiryDate;

    public void startVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.verified = false;
        this.expiryDate = LocalDateTime.now().plusMinutes(10);
    }

    public void endVerification() {
        this.verified = true;
    }
}
