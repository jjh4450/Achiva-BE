package unicon.Achiva.member.infrastructure;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unicon.Achiva.member.domain.MemberCategoryCounter;
import unicon.Achiva.member.domain.MemberCategoryKey;

import java.util.Optional;

public interface MemberCategoryCounterRepository
        extends JpaRepository<MemberCategoryCounter, MemberCategoryKey> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from MemberCategoryCounter c where c.id = :id")
    Optional<MemberCategoryCounter> lockById(@Param("id") MemberCategoryKey id);
}
