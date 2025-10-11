package unicon.Achiva.member.interfaces;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.global.validation.ValidHexColor;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.domain.Question;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Getter
public class ArticleRequest {

    @URL(protocol = "https")
    private final String photoUrl;

    @NotNull
    @Size(min = 1, max = 50)
    private final String title;
    private final Category category;
    private final List<QuestionDTO> question;

    @ValidHexColor
    private final String backgroundColor;

    public ArticleRequest(String photoUrl, String title, Category category, List<QuestionDTO> question, String backgroundColor) {
        this.photoUrl = photoUrl;
        this.title = title;
        this.category = category;
        this.question = question;
        this.backgroundColor = backgroundColor;
    }

    @Getter
    @Builder
    public static class QuestionDTO {
        private String question;
        private String content;

        public QuestionDTO(String question, String content) {
            this.question = question;
            this.content = content;
        }

        public static Question toEntity(QuestionDTO questionDTO) {
            return Question.builder()
                    .title(questionDTO.getQuestion())
                    .content(questionDTO.getContent())
                    .build();
        }

        public static QuestionDTO fromEntity(Question question) {
            return QuestionDTO.builder()
                    .question(question.getTitle())
                    .content(question.getContent())
                    .build();
        }
    }
}
