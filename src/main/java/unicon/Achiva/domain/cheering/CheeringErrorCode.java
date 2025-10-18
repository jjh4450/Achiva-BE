package unicon.Achiva.domain.cheering;

import org.springframework.http.HttpStatus;
import unicon.Achiva.global.response.ErrorCode;

public enum CheeringErrorCode implements ErrorCode {
    CHEERING_NOT_FOUND(4001, "해당 응원을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_MEMBER(4002, "응원을 작성한 사용자가 아닙니다", HttpStatus.UNAUTHORIZED),
    ;


    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    CheeringErrorCode(Integer code, String message, HttpStatus httpStatus) {
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
}
