package unicon.Achiva.member.domain;

import org.springframework.http.HttpStatus;
import unicon.Achiva.global.response.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
    DUPLICATE_EMAIL("1001", "이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_NICKNAME("1002", "이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    MemberErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
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