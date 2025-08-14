package unicon.Achiva.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CheeringServiceRepository extends JpaRepository<Cheering, Long> {
    // Define any custom query methods if needed
}
