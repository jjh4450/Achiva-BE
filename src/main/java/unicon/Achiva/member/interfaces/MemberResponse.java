package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.domain.Gender;
import unicon.Achiva.member.domain.Member;
import unicon.Achiva.member.domain.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private String nickName;
    private LocalDate birth;
    private Gender gender;
    private String region;
    private List<String> categories;
    private String profileImageUrl;
    private Role role;
    private LocalDateTime createdAt;


    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .birth(member.getBirth())
                .gender(member.getGender())
                .region(member.getRegion())
                .categories(
                        member.getCategories()
                                .stream()
                                .map(Category::getDisplayName)
                                .toList()
                )
                .profileImageUrl(member.getProfileImageUrl())
                .role(member.getRole())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
