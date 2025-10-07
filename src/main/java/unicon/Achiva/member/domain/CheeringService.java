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
import unicon.Achiva.member.interfaces.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheeringService {

    private static final long POINTS_PER_CHEER = 10L;

    private final CheeringRepository cheeringRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public CheeringResponse createCheering(CheeringRequest request, UUID memberId, Long articleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new GeneralException(ArticleErrorCode.ARTICLE_NOT_FOUND));

        Cheering cheering = Cheering.builder()
                .content(request.getContent())
                .cheeringCategory(request.getCheeringCategory())
                .article(article)
                .sender(member)
                .receiver(article.getMember())
                .build();

        cheeringRepository.save(cheering);

        return CheeringResponse.fromEntity(cheering);
    }

    @Transactional
    public CheeringResponse updateCheering(CheeringRequest request, Long cheeringId, UUID memberId) {
        Cheering cheering = cheeringRepository.findById(cheeringId)
                .orElseThrow(() -> new GeneralException(CheeringErrorCode.CHEERING_NOT_FOUND));

        if (!cheering.getSender().getId().equals(memberId)) {
            throw new GeneralException(CheeringErrorCode.UNAUTHORIZED_MEMBER);
        }

        cheering.updateContent(request.getContent());
        cheering.updateCheeringCategory(request.getCheeringCategory());

        return CheeringResponse.fromEntity(cheering);
    }

    @Transactional
    public void deleteCheering(Long cheeringId, UUID memberId) {
        Cheering cheering = cheeringRepository.findById(cheeringId)
                .orElseThrow(() -> new GeneralException(CheeringErrorCode.CHEERING_NOT_FOUND));

        if (!cheering.getSender().getId().equals(memberId)) {
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

    public UnreadCheeringResponse getUnreadCheeringCount(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        Long count = cheeringRepository.countByArticle_MemberAndIsReadFalse(member);
        return UnreadCheeringResponse.builder()
                .unreadCheeringCount(count)
                .build();
    }

    public Page<CheeringResponse> getCheeringsByMemberId(UUID memberId, Pageable pageable) {
        Page<Cheering> cheerings = cheeringRepository.findAllByArticle_Member_Id(memberId, pageable);

        return cheerings.map(CheeringResponse::fromEntity);
    }

    @Transactional
    public List<CheeringResponse> readCheering(CheeringReadRequest request) {
        List<Cheering> cheerings = cheeringRepository.findAllById(request.getCheeringIds());
        cheerings.stream()
                .filter(cheering -> !cheering.getIsRead())
                .forEach(Cheering::markAsRead);
        return cheerings.stream()
                .map(CheeringResponse::fromEntity)
                .toList();
    }

    public List<CategoryStatDto> getGivenStats(UUID memberId) {
        return cheeringRepository.givenStatsByCategory(memberId, POINTS_PER_CHEER);
    }

    public List<CategoryStatDto> getReceivedStats(UUID memberId) {
        return cheeringRepository.receivedStatsByCategory(memberId, POINTS_PER_CHEER);
    }

    public TotalSendingCheeringScoreResponse getTotalGivenPoints(UUID memberId) {
        return new TotalSendingCheeringScoreResponse(
                cheeringRepository.totalGivenCount(memberId) * POINTS_PER_CHEER
        );
    }

    public TotalReceivedCheeringScoreResponse getTotalReceivedPoints(UUID memberId) {
        return new TotalReceivedCheeringScoreResponse(
                cheeringRepository.totalReceivedCount(memberId) * POINTS_PER_CHEER
        );
    }

}
