package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.member.domain.Cheering;

public interface CheeringRepository extends JpaRepository<Cheering, Long>, CheeringRepositoryCustom {
    Page<Cheering> findAllByArticleId(Long articleId, Pageable pageable);
}
