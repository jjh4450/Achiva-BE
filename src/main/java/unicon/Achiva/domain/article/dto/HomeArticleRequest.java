package unicon.Achiva.domain.article.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HomeArticleRequest {
    @org.hibernate.validator.constraints.UUID
    private UUID memberId;
}
