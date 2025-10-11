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
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickName;

    private String profileImageUrl;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String region;

    @Column(length = 500)
    private String description;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    private List<Category> categories;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    private boolean pushEnabled;

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateBirth(LocalDate parse) {
        this.birth = parse;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateRegion(String region) {
        this.region = region;
    }

    public void updateCategories(List<Category> list) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        this.categories.clear();
        this.categories.addAll(list);
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}