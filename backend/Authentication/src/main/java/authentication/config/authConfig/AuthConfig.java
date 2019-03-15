package authentication.config.authConfig;

import authentication.util.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.Context;
import io.javalin.ForbiddenResponse;
import io.javalin.Javalin;
import io.javalin.security.Role;

public class AuthConfig {
    private final String headerTokenName = "Authorization";

    private JwtProvider jwtProvider;

    public AuthConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public void configure(Javalin app) {
        app.accessManager((handler, ctx, roles) -> {
            DecodedJWT jwtToken = getJwtTokenHeader(ctx);
            Role userRole = getUserRole(jwtToken);
            if (userRole == null) {
                userRole = Roles.ANYONE;
            }
            if (!roles.contains(userRole)) {
                throw new ForbiddenResponse();
            }
            ctx.attribute("username", getUsername(jwtToken));
            handler.handle(ctx);
        });
    }

    private DecodedJWT getJwtTokenHeader(Context ctx) {
        if (ctx.header(headerTokenName) == null) {
            return null;
        }

        String tokenHeader = ctx.header(headerTokenName).split("Token")[1].trim();

        return jwtProvider.decodedJWT(tokenHeader);
    }

    private String getUsername(DecodedJWT jwtToken) {
        if (jwtToken == null) {
            return null;
        }
        return jwtToken.getSubject();
    }

    private Role getUserRole(DecodedJWT jwtToken) {
        if (jwtToken == null) {
            return null;
        }
        String role = jwtToken.getClaim("role").asString();
        if (role == null) {
            return null;
        }

        return Roles.valueOf(role);
    }


}
