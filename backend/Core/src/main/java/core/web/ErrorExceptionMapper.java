package core.web;

import io.javalin.BadRequestResponse;
import io.javalin.Javalin;
import io.javalin.UnauthorizedResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;

public class ErrorExceptionMapper {
    private static Logger log = LoggerFactory.getLogger(ErrorExceptionMapper.class);

    public static void register(Javalin app) {
        app.exception(Exception.class, (e, ctx) -> {
            log.error(String.format("Exception ocurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("Unknown error", Collections.singletonList(e.toString())));
            ctx.json(err).status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        });

        app.exception(SQLException.class, (e, ctx) -> {
            log.error(String.format("SQL Exception ocurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("Unknown error", Collections.singletonList(e.toString())));
            ctx.json(err).status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        });

        app.exception(BadRequestResponse.class, (e, ctx) -> {
            log.error(String.format("BadRequestResponse ocurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("body", Collections.singletonList("can't be empty or invalid")));
            ctx.json(err).status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        });

        app.exception(UnauthorizedResponse.class, (e, ctx) -> {
            log.error(String.format("UnauthorizedResponse ocurred for req -> %s", e));
            ErrorResponse err = new ErrorResponse(Collections.singletonMap("login", Collections.singletonList("Account not authenticated")));
            ctx.json(err).status(HttpStatus.UNAUTHORIZED_401);
        });

    }
}
