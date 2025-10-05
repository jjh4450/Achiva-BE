package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateMemberResponse {
    private UUID id;
    private String email;
    private String nickName;
    private String profileImageUrl;
    private String birth;
    private String gender;
    private String region;
    private List<String> categories;
    private LocalDateTime createdAt;

    public static CreateMemberResponse fromEntity(Member member) {
        return CreateMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .profileImageUrl(member.getProfileImageUrl())
                .birth(member.getBirth().toString())
                .gender(member.getGender() != null ? member.getGender().name() : null)
                .region(member.getRegion() != null ? member.getRegion() : null)
                .categories(
                        member.getCategories().stream()
                                .map(Category::getDescription)
                                .toList()
                )
                .createdAt(member.getCreatedAt())
                .build();
    }
}
