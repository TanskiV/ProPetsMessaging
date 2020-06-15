package messaging.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT)
public class MessageException extends RuntimeException {
    public MessageException(String message) {
        super(message);
    }
}
