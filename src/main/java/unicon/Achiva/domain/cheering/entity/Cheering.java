package unicon.Achiva.domain.cheering.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicon.Achiva.domain.article.entity.Article;
import unicon.Achiva.domain.cheering.CheeringCategory;
import unicon.Achiva.domain.member.entity.Member;
import unicon.Achiva.global.common.LongBaseEntity;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "sender_id, cheering_category"),
        @Index(columnList = "receiver_id, cheering_category")
})
public class Cheering extends LongBaseEntity {

    @Lob
    private String content;

    // 응원카테고리 정의되면 enum으로 변경
    private CheeringCategory cheeringCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder.Default
    private boolean isRead = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCheeringCategory(CheeringCategory cheeringCategory) {
        this.cheeringCategory = cheeringCategory;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
