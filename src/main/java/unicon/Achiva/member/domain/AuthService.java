package unicon.Achiva.member.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unicon.Achiva.global.response.GeneralException;
import unicon.Achiva.member.infrastructure.MemberRepository;
import unicon.Achiva.member.interfaces.CreateMemberRequest;
import unicon.Achiva.member.interfaces.CreateMemberResponse;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CreateMemberResponse signup(CreateMemberRequest requestDto) {
        validateDuplicateAuthId(requestDto.getEmail());

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .nickName(requestDto.getNickName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .profileImageUrl(requestDto.getProfileImageUrl())
                .birth(LocalDate.parse(requestDto.getBirth()))
                .gender(Gender.valueOf(requestDto.getGender().toUpperCase()))
                .region(requestDto.getRegion())
                .categories(requestDto.getCategories().stream()
                        .map(Category::fromDisplayName)
                        .toList())
                .role(Role.GUEST)
                .build();

        Member savedMember = memberRepository.save(member);

        return CreateMemberResponse.fromEntity(savedMember);
    }

    private void validateDuplicateAuthId(String email) {
        boolean isExists = memberRepository.existsByEmail(email);
        if (isExists) {
            throw new GeneralException(MemberErrorCode.DUPLICATE_EMAIL);
        }
    }
}
