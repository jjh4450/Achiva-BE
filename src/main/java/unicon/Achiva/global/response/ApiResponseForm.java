package unicon.Achiva.global.response;

import lombok.Getter;

@Getter
public class ApiResponseForm<T> {
    private final T data;
    // 제네릭 api 응답 객체
    private final String status;
    private final Integer code;
    private final String message;

    public ApiResponseForm(String status, Integer code, String message, T data) {
        this.status = status; // HttpResponse의 생성자 호출 (부모 클래스의 생성자 또는 메서드를 호출, 자식 클래스는 부모 클래스의 private 필드에 직접 접근 X)
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 성공 응답을 위한 메서드 (message를 받는 경우)
    public static <T> ApiResponseForm<T> success(T data, String message) {
        return new ApiResponseForm<>("success", 200, message, data);
    }

    // 성공 응답을 위한 메서드 (message를 받지 않는 경우)
    public static <T> ApiResponseForm<T> success(T data) {
        return new ApiResponseForm<>("success", 200, "OK", data);
    }

    // 성공 응답 for 생성
    public static <T> ApiResponseForm<T> created(T data, String message) {
        return new ApiResponseForm<>("success", 201, message, data);
    }

    // 오류 응답을 위한 메서드
    public static <T> ApiResponseForm<T> error(Integer code, String message) {
        return new ApiResponseForm<>("error", code, message, null);  // 오류의 경우 data는 null로 처리
    }
}
