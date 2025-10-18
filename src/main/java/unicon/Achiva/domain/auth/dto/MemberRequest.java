package unicon.Achiva.domain.auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;
import unicon.Achiva.domain.category.Category;
import unicon.Achiva.domain.member.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MemberRequest {
    @URL(protocol = "https")
    private String profileImageUrl;
    @NotNull
    private LocalDate birth;
    private Gender gender;
    @Size(max = 50)
    private String region;
    private List<Category> categories;
}
