package unicon.Achiva.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "member_category_counter")
@Getter @Setter
@NoArgsConstructor
public class MemberCategoryCounter {

    @EmbeddedId
    private MemberCategoryKey id;

    @Version
    private long version;

    @Column(nullable = false)
    private long size = 0L;
}
