package boomerang.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Arrays;

public class WebNotificationException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public WebNotificationException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;

    }

    @Override
    public String toString() {
        return message + " : " + Arrays.toString(getStackTrace());
    }
}
