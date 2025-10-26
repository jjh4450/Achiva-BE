package unicon.Achiva.domain.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.domain.member.entity.Member;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickname);

    Optional<Member> findByNickName(String nickname);

    Page<Member> findByNickNameContainingIgnoreCase(String nickName, Pageable pageable);
}
