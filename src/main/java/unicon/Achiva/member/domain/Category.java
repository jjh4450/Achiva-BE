package unicon.Achiva.member.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    STUDY("공부"),
    WORKOUT("운동"),
    CAREER("커리어"),
    READING("독서"),
    SELF_DEVELOPMENT("자기계발"),
    HOBBY("취미"),
    INVESTMENT("투자"),
    ROUTINE("루틴"),
    MINDSET("마인드셋")
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

    public static String getDisplayName(Category category) {
        return category.getDescription();
    }
}
