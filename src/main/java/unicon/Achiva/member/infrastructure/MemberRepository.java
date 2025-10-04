package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.member.domain.Member;

import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickname);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickName(String nickname);

    Page<Member> findByNickNameContainingIgnoreCase(String nickName, Pageable pageable);

    boolean existsBySub(UUID sub);
}
