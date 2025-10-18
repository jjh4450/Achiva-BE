package unicon.Achiva.domain.friendship;

import org.springframework.http.HttpStatus;
import unicon.Achiva.global.response.ErrorCode;

public enum FriendshipErrorCode implements ErrorCode {
    FRIENDSHIP_NOT_FOUND(3000, "친구 관계를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FRIENDSHIP_ALREADY_PROCESSED(3001, "이미 처리된 친구 요청입니다.", HttpStatus.BAD_REQUEST),
    FRIENDSHIP_NOT_RECEIVER(3002, "해당 친구 요청의 수신자가 아닙니다.", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    FriendshipErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public enum FRIENDSHIP_NOT_FOUND {}
}
