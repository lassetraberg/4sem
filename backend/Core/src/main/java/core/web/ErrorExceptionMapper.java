package core.web;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.javalin.*;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;

public class ErrorExceptionMapper {
    private static Logger log = LoggerFactory.getLogger(ErrorExceptionMapper.class);

    public static void register(Javalin app) {
        app.exception(BadRequestResponse.class, (e, ctx) -> {
            log.error(String.format("BadRequestResponse occurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("body", Collections.singletonList("can't be empty or invalid")));
            ctx.json(err).status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        });

        app.exception(UnauthorizedResponse.class, (e, ctx) -> {
            log.error(String.format("UnauthorizedResponse occurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("login", Collections.singletonList(e.getMessage())));
            ctx.json(err).status(HttpStatus.UNAUTHORIZED_401);
        });

        app.exception(ForbiddenResponse.class, (e, ctx) -> {
            log.error(String.format("ForbiddenResponse occurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("login", Collections.singletonList("Account doesn't have permission to do this")));
            ctx.json(err).status(HttpStatus.FORBIDDEN_403);
        });

        app.exception(JWTVerificationException.class, (e, ctx) -> {
            log.error(String.format("JWTVerificationException occurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("token", Collections.singletonList("Invalid JWT token")));
            ctx.json(err).status(HttpStatus.FORBIDDEN_403);
        });

        app.exception(NotFoundResponse.class, (e, ctx) -> {
            log.error(String.format("NotFoundResponse occurred for req -> %s", e));
            String msg = !e.getMessage().isEmpty() ? e.getMessage() : "404 Not Found";
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("body", Collections.singletonList(msg)));
            ctx.json(err).status(HttpStatus.NOT_FOUND_404);
        });

        app.exception(HttpResponseException.class, (e, ctx) -> {
            log.error(String.format("HttpResponseException occurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("body", Collections.singletonList(e.getMessage())));
            ctx.json(err).status(e.getStatus());
        });

    }
}
