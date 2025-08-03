package unicon.Achiva.member.domain;

import lombok.Getter;

@Getter
public enum Category {
    STUDY("공부"),
    WORKOUT("운동"),
    ;

    private final String description;

    Category(String description) {
        this.description = description;
    }
}
