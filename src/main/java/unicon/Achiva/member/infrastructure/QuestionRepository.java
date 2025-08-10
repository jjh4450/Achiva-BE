package unicon.Achiva.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import unicon.Achiva.member.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
