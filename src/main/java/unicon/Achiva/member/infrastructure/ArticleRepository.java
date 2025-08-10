package unicon.Achiva.member.infrastructure;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unicon.Achiva.member.domain.Article;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
}
