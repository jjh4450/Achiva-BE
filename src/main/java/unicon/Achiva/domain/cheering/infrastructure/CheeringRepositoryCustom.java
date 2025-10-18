package unicon.Achiva.domain.cheering.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import unicon.Achiva.domain.cheering.entity.Cheering;

public interface CheeringRepositoryCustom {
    Page<Cheering> findAllByArticleId(Long articleId, Pageable pageable);
}
