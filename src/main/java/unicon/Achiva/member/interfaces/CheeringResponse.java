package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Cheering;

import java.time.LocalDateTime;

@Getter
@Builder
public class CheeringResponse {
    private Long id;
    private String content;
    private String cheeringCategory;
    private Long memberId;
    private String memberName;
    private Long articleId;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CheeringResponse fromEntity(Cheering cheering) {
        return CheeringResponse.builder()
                .id(cheering.getId())
                .content(cheering.getContent())
                .cheeringCategory(cheering.getCheeringCategory())
                .memberId(cheering.getMember().getId())
                .memberName(cheering.getMember().getNickName())
                .articleId(cheering.getArticle().getId())
                .isRead(cheering.getIsRead())
                .createdAt(cheering.getCreatedAt())
                .updatedAt(cheering.getUpdatedAt())
                .build();
    }
}
