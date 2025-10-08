package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.domain.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MemberRequest {
    private String profileImageUrl;
    private LocalDate birth;
    private Gender gender;
    private String region;
    private List<Category> categories;
}
