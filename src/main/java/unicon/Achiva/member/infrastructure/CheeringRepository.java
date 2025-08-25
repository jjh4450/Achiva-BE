package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicon.Achiva.member.domain.Cheering;
import unicon.Achiva.member.domain.Member;
import unicon.Achiva.member.interfaces.CategoryStatDto;

import java.util.List;

public interface CheeringRepository extends JpaRepository<Cheering, Long>, CheeringRepositoryCustom {
    Page<Cheering> findAllByArticleId(Long articleId, Pageable pageable);

    @Query("""
        select distinct c.sender.id
        from Cheering c
        where c.article.member.id = :me
    """)
    List<Long> findDistinctCheererIdsWhoCheeredMyArticles(@Param("me") Long me);

    Long countByArticle_MemberAndIsReadFalse(Member member);

    Page<Cheering> findAllByArticle_Member_Id(Long memberId, Pageable pageable);


    // 내가 "보낸" 응원: 카테고리별 개수/점수
    // CategoryStatDto 하드코딩해서 안좋은 설계방식이긴함..
    @Query("""
        select new unicon.Achiva.member.interfaces.CategoryStatDto(c.cheeringCategory, count(c), count(c) * :pt)
        from Cheering c
        where c.sender.id = :memberId
        group by c.cheeringCategory
    """)
    List<CategoryStatDto> givenStatsByCategory(@Param("memberId") Long memberId,
                                               @Param("pt") long pointsPerCheer);

    // 내가 "받은" 응원: 카테고리별 개수/점수
    @Query("""
        select new unicon.Achiva.member.interfaces.CategoryStatDto(c.cheeringCategory, count(c), count(c) * :pt)
        from Cheering c
        where c.receiver.id = :memberId
        group by c.cheeringCategory
    """)
    List<CategoryStatDto> receivedStatsByCategory(@Param("memberId") Long memberId,
                                                  @Param("pt") long pointsPerCheer);

    // 멤버의 총 준 응원 개수
    @Query("select count(c) from Cheering c where c.sender.id = :memberId")
    long totalGivenCount(@Param("memberId") Long memberId);

    // 멤버의 총 받은 응원 개수
    @Query("select count(c) from Cheering c where c.receiver.id = :memberId")
    long totalReceivedCount(@Param("memberId") Long memberId);
}
