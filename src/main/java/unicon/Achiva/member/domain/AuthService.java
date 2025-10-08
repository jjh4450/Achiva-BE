package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.*;

import java.time.LocalDate;
//import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final OIDCUserInfoService oidcUserInfoService;

    @Transactional
    public CreateMemberResponse signup(MemberRequest requestDto) {
        String email = getEmailFromToken().orElse(oidcUserInfoService.getEmailFromUserInfo().orElseThrow(() -> new GeneralException(MemberErrorCode.INVALID_TOKEN)));
        String nickName = getNickNameFromToken().orElseThrow(()-> new GeneralException(MemberErrorCode.INVALID_TOKEN));

        validateDuplication(nickName, email);

        Member member = Member.builder()
                .email(email)
                .nickName(nickName)
                .profileImageUrl(requestDto.getProfileImageUrl())
                .birth(requestDto.getBirth())
                .gender(requestDto.getGender() != null ? requestDto.getGender() : null)
                .region(requestDto.getRegion() != null ? requestDto.getRegion() : null)
                .categories(requestDto.getCategories())
                .role(Role.USER)
                .build();

        member.dangerFunctionOnlyInitUserId(getMemberIdFromToken());

        Member savedMember = memberRepository.save(member);


        return CreateMemberResponse.fromEntity(savedMember);
    }

    /**
     * 부분 갱신(PATCH) 형태의 회원 정보 업데이트.
     * null 이 아닌 필드만 엔티티에 반영하며, 닉네임은 변경 시 중복 검증을 수행한다.
     *
     * @param memberId   업데이트할 회원 식별자
     * @param requestDto 변경 요청 DTO (null 허용 필드는 선택 적용)
     * @return 갱신된 회원 응답 DTO
     * @throws GeneralException MEMBER_NOT_FOUND: 회원 없음
     */
    @Transactional
    public MemberResponse updateMember(UUID memberId, UpdateMemberRequest requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));

//        Optional.ofNullable(requestDto.getNickName())
//                .filter(n -> !Objects.equals(n, member.getNickName()))
//                .ifPresent(n -> {
//                    validateDuplicateNickName(n);
//                    member.updateNickName(n);
//                });

        Optional.ofNullable(requestDto.getProfileImageUrl())
                .ifPresent(member::updateProfileImageUrl);

        Optional.ofNullable(requestDto.getBirth())
                .map(LocalDate::parse)
                .ifPresent(member::updateBirth);

        Optional.ofNullable(requestDto.getGender())
                .map(Gender::valueOf)
                .ifPresent(member::updateGender);

        Optional.ofNullable(requestDto.getRegion())
                .ifPresent(member::updateRegion);

        Optional.ofNullable(requestDto.getCategories())
                .map(list -> list.stream()
                        .map(Category::fromDisplayName)
                        .toList())
                .ifPresent(member::updateCategories);

        Optional.ofNullable(requestDto.getDescription())
                .ifPresent(member::updateDescription);

        // JPA 영속성 컨텍스트 내 변경 감지로 flush 되므로 save 호출 불필요
        return MemberResponse.fromEntity(member);
    }

    @Transactional
    public void deleteMember(UUID memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
        memberRepository.delete(member);
    }

    public void validateDuplication(String email, String nickName) {
        validateDuplicateEmail(email);
        validateDuplicateNickName(nickName);
    }

    public CheckEmailResponse validateDuplicateEmail(String email) {
        boolean isExists = memberRepository.existsByEmail(email);
        if (isExists) {
            throw new GeneralException(MemberErrorCode.DUPLICATE_EMAIL);
        }
        return new CheckEmailResponse(true);
    }

    public CheckNicknameResponse validateDuplicateNickName(String nickName) {
        boolean isExists = memberRepository.existsByNickName(nickName);
        if (isExists) {
            throw new GeneralException(MemberErrorCode.DUPLICATE_NICKNAME);
        }
        return new CheckNicknameResponse(true);
    }

//    @Transactional
//    public SendVerificationCodeResponse sendVerificationCode(String email) {
//        validateDuplicateEmail(email);
//
//        String code = String.format("%04d", new Random().nextInt(9999));
//
//        EmailVerification verification = emailVerificationRepository.findByEmail(email)
//                .orElse(new EmailVerification());
//
//        verification.startVerification(email, code);
//
//        emailVerificationRepository.save(verification);
//
//        emailService.sendCode(email, code);
//
//        return new SendVerificationCodeResponse(email);
//    }

//    @Transactional
//    public VerifyCodeResponse verifyCode(String email, String code) {
//        EmailVerification verification = emailVerificationRepository.findByEmail(email)
//                .orElseThrow(() -> new GeneralException(MemberErrorCode.VERIFICATION_NOT_FOUND));
//
//        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
//            throw new GeneralException(MemberErrorCode.VERIFICATION_EXPIRED);
//        }
//        if (!verification.getCode().equals(code)) {
//            throw new GeneralException(MemberErrorCode.VERIFICATION_CODE_MISMATCH);
//        }
//
//        verification.endVerification();
//        emailVerificationRepository.save(verification);
//
//        return new VerifyCodeResponse(email);
//    }

//    @Transactional
//    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
//        Member member = memberRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new GeneralException(MemberErrorCode.VERIFICATION_NOT_FOUND));
//
//        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
//            throw new GeneralException(MemberErrorCode.PASSWORD_MISMATCH);
//        }
//
//        String newPassword = request.getNewPassword();
//        member.updatePassword(passwordEncoder.encode(newPassword));
//        memberRepository.save(member);
//
//        return new ResetPasswordResponse(member.getEmail());
//    }

    /**
     * Extracts the memberId from the JWT subject ("sub") claim.
     *
     * @return the UUID parsed from the JWT subject claim
     * @throws GeneralException if authentication is missing, not a JwtAuthenticationToken,
     *                          or the subject is not a valid UUID
     */
    public UUID getMemberIdFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new GeneralException(MemberErrorCode.INVALID_TOKEN);
        }

        String sub = jwtAuth.getToken().getSubject();
        try {
            return UUID.fromString(sub);
        } catch (IllegalArgumentException e) {
            throw new GeneralException(MemberErrorCode.INVALID_TOKEN);
        }
    }

    public Optional<String> getNickNameFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new GeneralException(MemberErrorCode.INVALID_TOKEN);
        }

        String nickname = jwtAuth.getToken().getClaimAsString("username");

        return Optional.ofNullable(nickname);
    }

    public Optional<String> getEmailFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new GeneralException(MemberErrorCode.INVALID_TOKEN);
        }

        String email = jwtAuth.getToken().getClaimAsString("email");

        return Optional.ofNullable(email);
    }

//    public CheckPasswordResponse checkPassword(CheckPasswordRequest request) {
//        Member member = memberRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new GeneralException(MemberErrorCode.MEMBER_NOT_FOUND));
//
//        boolean isMatch = passwordEncoder.matches(request.getPassword(), member.getPassword());
//        if (!isMatch) {
//            throw new GeneralException(MemberErrorCode.INVALID_PASSWORD);
//        }
//        return new CheckPasswordResponse(true);
//    }
}
