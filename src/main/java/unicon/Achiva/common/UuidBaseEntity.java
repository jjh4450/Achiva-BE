package unicon.Achiva.common;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.util.UUID;

@Getter
@MappedSuperclass
public class UuidBaseEntity extends BaseEntity {

    @Getter
    @Id
    protected UUID id;

}
