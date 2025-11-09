package unicon.Achiva.domain.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {
    STUDY("공부"),
    WORKOUT("운동"),
    CAREER("커리어"),
    READING("독서"),
    MINDSET("루틴/다짐"),
    HOBBY("패션"),
    INVESTMENT("투자/머니로그"),
    TRAVEL("여행"),
    DIET("식단"),
    FAMILY("가족/친구"),
    JOURNAL("생각/일기"),
    CULTURE("문화/취미"),
    PET("반려동물")
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }

    @JsonCreator
    public static Category fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(c -> c.description.equals(name) || c.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 카테고리: " + name));
    }

    public static String getDisplayName(Category category) {
        return category.getDescription();
    }

    @JsonValue
    public String toValue() {
        return this.description;
    }
}
