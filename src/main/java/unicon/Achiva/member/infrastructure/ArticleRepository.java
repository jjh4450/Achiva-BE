package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicon.Achiva.member.domain.Article;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.interfaces.SearchArticleCondition;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {


    @Query("SELECT a.category, COUNT(a) FROM Article a WHERE a.member.id = :memberId GROUP BY a.category")
    List<Object[]> countArticlesByCategoryForMember(@Param("memberId") UUID memberId);

    @EntityGraph(attributePaths = "member")
    @Query(value = """
            select a
            from Article a
            where a.member.id in :friendIds
               or a.member.id in :cheererIds
            order by
               case when a.member.id in :friendIds then 0 else 1 end,
               a.createdAt desc
            """,
            countQuery = """
                    select count(a)
                    from Article a
                    where a.member.id in :friendIds
                       or a.member.id in :cheererIds
                    """)
    Page<Article> findCombinedFeed(
            @Param("friendIds") Collection<UUID> friendIds,
            @Param("cheererIds") Collection<UUID> cheererIds,
            Pageable pageable
    );

    @Modifying(flushAutomatically = true)
    @Query("""
              update Article a
                 set a.authorCategorySeq = a.authorCategorySeq - 1
               where a.member.id = :memberId
                 and a.category   = :category
                 and a.authorCategorySeq > :fromSeq
            """)
    int shiftLeft(@Param("memberId") UUID memberId,
                  @Param("category") Category category,
                  @Param("fromSeq") long fromSeq);


    Page<Article> searchByCondition(SearchArticleCondition condition, Pageable pageable);

    Page<Article> findAllByMemberId(UUID memberId, Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Article> findByCategoryIn(List<Category> categories, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.member.id = :memberId AND a.category = :category ORDER BY a.createdAt DESC")
    Page<Article> findByMemberIdWithCategory(UUID memberId, Category category, Pageable pageable);
}
