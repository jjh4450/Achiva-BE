package unicon.Achiva.domain.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchArticleCondition {
    private String keyword;
    private String category;
}
