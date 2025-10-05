package unicon.Achiva.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    ;

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    /**
     * JSON 역직렬화 시 문자열("남성" 또는 "MALE")을 Gender enum으로 변환합니다.
     *
     * @param name 입력 문자열 (한글 설명 또는 영문 상수명)
     * @return 일치하는 Gender enum 상수
     * @throws IllegalArgumentException 잘못된 문자열이 주어질 경우 발생
     */
    @JsonCreator
    public static Gender fromDisplayName(String name) {
        return Arrays.stream(values())
                .filter(g -> g.description.equals(name) || g.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 성별: " + name));
    }

    @JsonValue
    public String toValue() {
        return this.description;
    }
}