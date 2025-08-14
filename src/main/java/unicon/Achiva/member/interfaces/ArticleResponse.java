package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Article;
import unicon.Achiva.member.domain.Category;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ArticleResponse {
    private Long id;
    private String photoUrl;
    private String title;
    private Category category;
    private List<ArticleRequest.QuestionDTO> question;
    private Long memberId;
    private String memberNickName;
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
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}
