package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.ArticleRepository;
import unicon.Achiva.member.infrastructure.CheeringRepository;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.CheeringResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheeringService {

    private final CheeringRepository cheeringRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public CheeringResponse createCheering(CheeringRequest request, Long memberId, Long articleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ArticleErrorCode.ARTICLE_NOT_FOUND));

        Cheering cheering = Cheering.builder()
                .content(request.getContent())
                .cheeringCategory(request.getCheeringCategory())
                .article(article)
                .member(member)
                .build();

        cheeringRepository.save(cheering);

        return CheeringResponse.fromEntity(cheering);
    }

    @Transactional
    public CheeringResponse updateCheering(CheeringRequest request, Long cheeringId, Long memberId) {
        Cheering cheering = cheeringRepository.findById(cheeringId)
                .orElseThrow(() -> new GeneralException(CheeringErrorCode.CHEERING_NOT_FOUND));

        if (!cheering.getMember().getId().equals(memberId)) {
            throw new GeneralException(CheeringErrorCode.UNAUTHORIZED_MEMBER);
        }

        cheering.updateContent(request.getContent());
        cheering.updateCheeringCategory(request.getCheeringCategory());

        return CheeringResponse.fromEntity(cheering);
    }

    @Transactional
    public void deleteCheering(Long cheeringId, Long memberId) {
        Cheering cheering = cheeringRepository.findById(cheeringId)
                .orElseThrow(() -> new GeneralException(CheeringErrorCode.CHEERING_NOT_FOUND));

        if (!cheering.getMember().getId().equals(memberId)) {
            throw new GeneralException(CheeringErrorCode.UNAUTHORIZED_MEMBER);
        }

        cheeringRepository.delete(cheering);
    }

    public CheeringResponse getCheering(Long cheeringId) {
        Cheering cheering = cheeringRepository.findById(cheeringId)
                .orElseThrow(() -> new GeneralException(CheeringErrorCode.CHEERING_NOT_FOUND));
        return CheeringResponse.fromEntity(cheering);
    }

    public Page<CheeringResponse> getCheeringsByArticleId(Long articleId, Pageable pageable) {
        return cheeringRepository.findAllByArticleId(articleId, pageable)
                .map(CheeringResponse::fromEntity);
    }
}
