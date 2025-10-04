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

    @Transactional
    public void updateProfileImageUrl(UUID memberId, ConfirmProfileImageUploadRequest confirmProfileImageUploadRequest) {
        memberRepository.findById(memberId).ifPresentOrElse(
                member -> {
                    member.updateProfileImageUrl(confirmProfileImageUploadRequest.getUrl());
                    memberRepository.save(member);
                },
                () -> {
                    throw new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND);
                }
        );
    }
}
