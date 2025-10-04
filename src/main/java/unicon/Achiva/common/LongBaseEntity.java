package unicon.Achiva.common;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public class LongBaseEntity extends BaseEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
