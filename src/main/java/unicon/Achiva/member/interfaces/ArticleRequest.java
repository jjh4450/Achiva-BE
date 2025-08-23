package unicon.Achiva.member.interfaces;

import lombok.Builder;
import lombok.Getter;
import unicon.Achiva.member.domain.Question;

import java.util.List;

@Getter
public class ArticleRequest {
    private String photoUrl;
    private String title;
    private String category;
    private List<QuestionDTO> question;
    private String backgroundColor;

    public ArticleRequest(String photoUrl, String title, String category, List<QuestionDTO> question, String backgroundColor) {
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
