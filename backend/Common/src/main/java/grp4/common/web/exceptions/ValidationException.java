package grp4.common.web.exceptions;

import io.javalin.HttpResponseException;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ValidationException extends HttpResponseException {
    public ValidationException(@NotNull String msg) {
        super(HttpStatus.BAD_REQUEST_400, msg, Collections.singletonMap("Validation error", msg));
    }
}
