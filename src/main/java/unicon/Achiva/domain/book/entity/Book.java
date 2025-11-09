package unicon.Achiva.domain.book.entity;

import jakarta.persistence.*;
import lombok.*;
import unicon.Achiva.domain.article.entity.Article;
import unicon.Achiva.domain.member.entity.Member;
import unicon.Achiva.global.common.UuidBaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends UuidBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // ✅ Book 생성자 (소유자)

    private String title; // 책 제목
    private String description; // 설명

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "main_article_id")
    private Article mainArticle; // 첫 페이지 역할의 메인 아티클

    @OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("pageIndex ASC")
    @Builder.Default
    private List<Article> articles = new ArrayList<>();

    /** 메인 페이지 설정 */
    public void setMainArticle(Article article) {
        this.mainArticle = article;
        article.updateBook(this);
    }

    /** 새 페이지 추가 */
    public void addArticle(Article article, int index) {
        article.updateBook(this);
        article.updatePageIndex(index);
        this.articles.add(article);
    }

    /** 페이지 삭제 */
    public void removeArticle(Article article) {
        this.articles.remove(article);
        article.updateBook(null);
    }

    /** 제목/설명 변경 */
    public void update(String newTitle, String newDesc) {
        this.title = newTitle;
        this.description = newDesc;
    }
}
