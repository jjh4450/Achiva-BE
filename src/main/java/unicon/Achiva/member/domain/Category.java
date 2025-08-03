package unicon.Achiva.member.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    STUDY("공부"),
    WORKOUT("운동")
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }

    public static Category fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(c -> c.description.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 카테고리: " + name));
    }
}
