package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicon.Achiva.member.domain.Cheering;

import java.util.List;

public interface CheeringRepository extends JpaRepository<Cheering, Long>, CheeringRepositoryCustom {
    Page<Cheering> findAllByArticleId(Long articleId, Pageable pageable);

    @Query("""
        select distinct c.member.id
        from Cheering c
        where c.article.member.id = :me
    """)
    List<Long> findDistinctCheererIdsWhoCheeredMyArticles(@Param("me") Long me);

}
