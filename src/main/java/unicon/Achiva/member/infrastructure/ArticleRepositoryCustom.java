package unicon.Achiva.member.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import unicon.Achiva.member.domain.Article;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.domain.Question;
import unicon.Achiva.member.interfaces.SearchArticleCondition;

import java.util.ArrayList;
import java.util.List;


public interface ArticleRepositoryCustom {
    Page<Article> searchByCondition(SearchArticleCondition condition, Pageable pageable);
}
