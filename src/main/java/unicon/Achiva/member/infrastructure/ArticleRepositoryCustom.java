package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unicon.Achiva.member.domain.Article;

public interface ArticleRepositoryCustom {
    Page<Article> findAllByMemberId(Long memberId, Pageable pageable);
}
