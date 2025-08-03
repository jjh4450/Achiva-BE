package unicon.Achiva.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
}
