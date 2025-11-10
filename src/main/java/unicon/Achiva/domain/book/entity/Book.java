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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_article_id")
    private Article mainArticle; // 첫 페이지 역할의 메인 아티클

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("pageIndex ASC")
    private List<BookArticle> bookArticles = new ArrayList<>();

    /** 새 페이지 추가 */
    public void addArticle(Article article, int index) {
        // 중복 체크
        boolean alreadyExists = bookArticles.stream()
                .anyMatch(ba -> ba.getArticle().equals(article));

        if (!alreadyExists) {
            BookArticle bookArticle = BookArticle.builder()
                    .book(this)
                    .article(article)
                    .pageIndex(index)
                    .build();

            this.bookArticles.add(bookArticle);
            article.getBookArticles().add(bookArticle);
        }
    }

    /** 페이지 삭제 */
    public void removeArticle(Article article) {
        bookArticles.removeIf(ba -> ba.getArticle().equals(article));
    }

    /** 페이지 순서 변경 */
    public void updateArticleIndex(Article article, int newIndex) {
        bookArticles.stream()
                .filter(ba -> ba.getArticle().equals(article))
                .findFirst()
                .ifPresent(ba -> ba.updatePageIndex(newIndex));
    }

    /** 제목/설명 변경 */
    public void update(String newTitle, String newDesc) {
        this.title = newTitle;
        this.description = newDesc;
    }
}