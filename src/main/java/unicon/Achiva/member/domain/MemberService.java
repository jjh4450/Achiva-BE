package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.ConfirmProfileImageUploadRequest;
import unicon.Achiva.member.interfaces.MemberResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse getMemberInfo(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberResponse::fromEntity)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberResponse getMemberInfoByNickname(String nickname) {
        return memberRepository.findByNickName(nickname)
                .map(MemberResponse::fromEntity)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void updateProfileImageUrl(Long memberId, ConfirmProfileImageUploadRequest confirmProfileImageUploadRequest) {
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
