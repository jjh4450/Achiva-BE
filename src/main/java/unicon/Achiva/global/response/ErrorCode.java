package unicon.Achiva.global.response;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    Integer getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
