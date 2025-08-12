package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.ArticleRepository;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.ArticleResponse;
import unicon.Achiva.member.interfaces.ArticleRequest;
import unicon.Achiva.member.interfaces.CategoryCountResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArticleResponse createArticle(ArticleRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        Article article = Article.builder()
                .photoUrl(request.getPhotoUrl())
                .title(request.getTitle())
                .category(Category.fromDisplayName(request.getCategory()))
                .questions(request.getQuestion().stream()
                        .map(ArticleRequest.QuestionDTO::toEntity)
                        .toList())
                .member(member)
                .build();

        article.getQuestions().forEach(q -> q.setArticle(article));

        articleRepository.save(article);

        return ArticleResponse.fromEntity(article);
    }

    @Transactional
    public ArticleResponse updateArticle(ArticleRequest request, Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ArticleErrorCode.ARTICLE_NOT_FOUND));

        if (!article.getMember().getId().equals(memberId)) {
            throw new GeneralException(ArticleErrorCode.UNAUTHORIZED_MEMBER);
        }

        article.update(request);

        return ArticleResponse.fromEntity(article);
    }

    // 하드 딜리트
    @Transactional
    public void deleteArticle(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ArticleErrorCode.ARTICLE_NOT_FOUND));

        if (!article.getMember().getId().equals(memberId)) {
            throw new GeneralException(ArticleErrorCode.UNAUTHORIZED_MEMBER);
        }

        articleRepository.delete(article);
    }

    public ArticleResponse getArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ArticleErrorCode.ARTICLE_NOT_FOUND));
        return ArticleResponse.fromEntity(article);
    }

    public Page<ArticleResponse> getArticlesByMember(Long memberId, Pageable pageable) {
        return articleRepository.findAllByMemberId(memberId, pageable)
                .map(ArticleResponse::fromEntity);
    }

    public CategoryCountResponse getArticleCountByCategory(Long memberId) {
        List<Object[]> result = articleRepository.countArticlesByCategoryForMember(memberId);

        // 결과를 Map으로 변환 (key: Category, value: Long)
        Map<Category, Long> categoryCountMap = result.stream()
                .collect(Collectors.toMap(
                        row -> (Category) row[0],
                        row -> (Long) row[1]
                ));

        // 모든 카테고리를 순회하면서 없는 건 0L로 추가
        for (Category category : Category.values()) {
            categoryCountMap.putIfAbsent(category, 0L);
        }

        // 다시 List<Object[]> 형태로 변환하면서 getDisplayName 적용
        List<Object[]> completeResult = categoryCountMap.entrySet().stream()
                .map(entry -> new Object[]{
                        Category.getDisplayName(entry.getKey()),
                        entry.getValue()
                })
                .collect(Collectors.toList());

        return CategoryCountResponse.fromObjectList(completeResult);
    }


}