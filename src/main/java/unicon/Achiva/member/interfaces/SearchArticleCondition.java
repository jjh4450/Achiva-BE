package unicon.Achiva.member.interfaces;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import unicon.Achiva.member.domain.Category;

@Getter
@Setter
@NoArgsConstructor
public class SearchArticleCondition {
    private String keyword;
    private String category;
}
