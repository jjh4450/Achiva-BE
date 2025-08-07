package unicon.Achiva.member.domain;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.global.security.jwt.utils.JwtTokenProvider;
import unicon.Achiva.member.infrastructure.EmailVerificationRepository;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public CreateMemberResponse signup(CreateMemberRequest requestDto) {
        validateDuplication(requestDto.getNickName(), requestDto.getEmail());
        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new GeneralException(MemberErrorCode.PASSWORD_MISMATCH);
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickName(requestDto.getNickName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .profileImageUrl(requestDto.getProfileImageUrl())
                .birth(LocalDate.parse(requestDto.getBirth()))
                .gender(requestDto.getGender() != null ? Gender.valueOf(requestDto.getGender().toUpperCase()) : null)
                .region(requestDto.getRegion() != null ? requestDto.getRegion() : null)
                .categories(requestDto.getCategories().stream()
                        .map(Category::fromDisplayName)
                        .toList())
                .role(Role.USER)
                .build();

        Member savedMember = memberRepository.save(member);

        emailVerificationRepository.findByEmail(requestDto.getEmail())
                .ifPresent(emailVerificationRepository::delete);

        return CreateMemberResponse.fromEntity(savedMember);
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

    @Transactional
    public SendVerificationCodeResponse sendVerificationCode(String email) {
        validateDuplicateEmail(email);

        String code = String.format("%04d", new Random().nextInt(9999));

        EmailVerification verification = emailVerificationRepository.findByEmail(email)
                .orElse(new EmailVerification());

        verification.startVerification(email, code);

        emailVerificationRepository.save(verification);

        emailService.sendCode(email, code);

        return new SendVerificationCodeResponse(email);
    }

    @Transactional
    public VerifyCodeResponse verifyCode(String email, String code) {
        EmailVerification verification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(MemberErrorCode.VERIFICATION_NOT_FOUND));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new GeneralException(MemberErrorCode.VERIFICATION_EXPIRED);
        }
        if (!verification.getCode().equals(code)) {
            throw new GeneralException(MemberErrorCode.VERIFICATION_CODE_MISMATCH);
        }

        verification.endVerification();
        emailVerificationRepository.save(verification);

        return new VerifyCodeResponse(email);
    }

    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new GeneralException(MemberErrorCode.VERIFICATION_NOT_FOUND));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new GeneralException(MemberErrorCode.PASSWORD_MISMATCH);
        }

        String newPassword = request.getNewPassword();
        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        return new ResetPasswordResponse(member.getEmail());
    }

    public Long getMemberIdFromToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").substring(7);
        return jwtTokenProvider.extractUserId(accessToken).orElseThrow(() -> new GeneralException(MemberErrorCode.INVALID_TOKEN));
    }
}
