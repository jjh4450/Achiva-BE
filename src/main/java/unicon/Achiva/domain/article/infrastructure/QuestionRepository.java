package unicon.Achiva.domain.article.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.domain.article.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
