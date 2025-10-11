package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.ConfirmProfileImageUploadRequest;
import unicon.Achiva.member.interfaces.MemberResponse;
import unicon.Achiva.member.interfaces.SearchMemberCondition;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Boolean existsById(UUID memberId) {
        return memberRepository.existsById(memberId);
    }

    public MemberResponse getMemberInfo(UUID memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::fromEntity)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberResponse getMemberInfoByNickname(String nickname) {
        return memberRepository.findByNickName(nickname)
                .map(MemberResponse::fromEntity)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Page<MemberResponse> getMembers(SearchMemberCondition condition, Pageable pageable) {
        return memberRepository.findByNickNameContainingIgnoreCase(condition.getKeyword(), pageable)
                .map(MemberResponse::fromEntity);
    }

    /**
     * 주어진 회원의 프로필 이미지 URL을 갱신한다.
     *
     * @param memberId 회원 식별자
     * @param request  업로드 확인 요청(이미지 URL 포함)
     * @throws GeneralException 회원이 존재하지 않는 경우
     */
    @Transactional
    public void updateProfileImageUrl(UUID memberId, ConfirmProfileImageUploadRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.updateProfileImageUrl(request.getUrl());
    }
}
