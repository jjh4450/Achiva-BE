package unicon.Achiva.domain.article.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicon.Achiva.domain.article.dto.ArticleRequest;
import unicon.Achiva.domain.category.Category;
import unicon.Achiva.domain.cheering.entity.Cheering;
import unicon.Achiva.domain.member.entity.Member;
import unicon.Achiva.global.common.LongBaseEntity;
import unicon.Achiva.global.common.UuidBaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article extends UuidBaseEntity {

    private String photoUrl;

    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String backgroundColor;

    @Column(name = "author_category_seq", nullable = false)
    private Long authorCategorySeq;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Cheering> cheerings = new ArrayList<>();

    public void update(ArticleRequest request) {
        this.photoUrl = request.getPhotoUrl();
        this.title = request.getTitle();
        this.backgroundColor = request.getBackgroundColor();

        this.questions.clear();
        for (ArticleRequest.QuestionDTO questionDTO : request.getQuestion()) {
            Question question = ArticleRequest.QuestionDTO.toEntity(questionDTO);
            question.setArticle(this);
            this.questions.add(question);
        }
    }

    public void changeCategoryAndSeq(Category newCategory, long newSeq) {
        this.category = newCategory;
        this.authorCategorySeq = newSeq;
    }
}
