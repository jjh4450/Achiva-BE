package unicon.Achiva.member.domain;

import org.springframework.http.HttpStatus;
import unicon.Achiva.global.response.ErrorCode;

public enum ArticleErrorCode implements ErrorCode {
    ARTICLE_NOT_FOUND(2000, "해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_MEMBER(2001, "해당 게시글에 대한 수정/삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;

    ArticleErrorCode(Integer code, String message, HttpStatus httpStatus) {
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
