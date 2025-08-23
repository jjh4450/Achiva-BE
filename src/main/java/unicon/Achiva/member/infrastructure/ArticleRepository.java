package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unicon.Achiva.member.domain.Article;
import unicon.Achiva.member.domain.Category;
import unicon.Achiva.member.interfaces.SearchArticleCondition;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {


    @Query("SELECT a.category, COUNT(a) FROM Article a WHERE a.member.id = :memberId GROUP BY a.category")
    List<Object[]> countArticlesByCategoryForMember(@Param("memberId") Long memberId);

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
            @Param("friendIds") Collection<Long> friendIds,
            @Param("cheererIds") Collection<Long> cheererIds,
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
    int shiftLeft(@Param("memberId") Long memberId,
                  @Param("category") Category category,
                  @Param("fromSeq") long fromSeq);


    Page<Article> searchByCondition(SearchArticleCondition condition, Pageable pageable);

    Page<Article> findAllByMemberId(Long memberId, Pageable pageable);
}
