package unicon.Achiva.domain.article.dto;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.domain.article.entity.Article;
import unicon.Achiva.domain.category.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ArticleResponse {
    private Long id;
    private String photoUrl;
    private String title;
    private Category category;
    private List<ArticleRequest.QuestionDTO> question;
    private UUID memberId;
    private String memberNickName;
    private String memberProfileUrl;
    private String backgroundColor;
    private Long authorCategorySeq;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ArticleResponse fromEntity(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .photoUrl(article.getPhotoUrl())
                .title(article.getTitle())
                .category(article.getCategory())
                .question(
                        article.getQuestions()
                                .stream()
                                .map(ArticleRequest.QuestionDTO::fromEntity)
                                .toList()
                )
                .memberId(article.getMember().getId())
                .memberNickName(article.getMember().getNickName())
                .memberProfileUrl(article.getMember().getProfileImageUrl())
                .authorCategorySeq(article.getAuthorCategorySeq())
                .backgroundColor(article.getBackgroundColor())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}
