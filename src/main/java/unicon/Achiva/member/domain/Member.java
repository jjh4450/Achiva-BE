package unicon.Achiva.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import unicon.Achiva.common.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    private String profileImageUrl;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String region;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    private List<Category> categories;

    @Enumerated(EnumType.STRING)
    private Role role;
}