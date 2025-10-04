package unicon.Achiva.member.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicon.Achiva.common.LongBaseEntity;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "sender_id, category"),
        @Index(columnList = "receiver_id, category")
})
public class Cheering extends LongBaseEntity {

    @Lob
    private String content;

    // 응원카테고리 정의되면 enum으로 변경
    private String cheeringCategory;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "sender_id", nullable=false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "receiver_id", nullable=false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Builder.Default
    private boolean isRead = false;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateCheeringCategory(String cheeringCategory) {
        this.cheeringCategory = cheeringCategory;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
