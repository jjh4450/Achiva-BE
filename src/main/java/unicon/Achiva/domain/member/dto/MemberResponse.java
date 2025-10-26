package unicon.Achiva.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.domain.auth.Role;
import unicon.Achiva.domain.category.Category;
import unicon.Achiva.domain.member.Gender;
import unicon.Achiva.domain.member.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class MemberResponse {
    private UUID id;
    private String email;
    private String nickName;
    private LocalDate birth;
    private Gender gender;
    private String region;
    private List<String> categories;
    private String profileImageUrl;
    private String description;
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
                .description(member.getDescription())
                .role(member.getRole())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
