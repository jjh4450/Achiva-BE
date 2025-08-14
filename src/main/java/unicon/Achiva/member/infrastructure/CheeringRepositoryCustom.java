package unicon.Achiva.member.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unicon.Achiva.member.domain.Cheering;

public interface CheeringRepositoryCustom {
    Page<Cheering> findAllByArticleId(Long articleId, Pageable pageable);
}
