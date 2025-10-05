package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unicon.Achiva.member.domain.Article;
import unicon.Achiva.member.interfaces.SearchArticleCondition;


public interface ArticleRepositoryCustom {
    Page<Article> searchByCondition(SearchArticleCondition condition, Pageable pageable);
}
