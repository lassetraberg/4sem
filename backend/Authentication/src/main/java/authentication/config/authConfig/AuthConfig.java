package authentication.config.authConfig;

import authentication.util.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.Context;
import io.javalin.ForbiddenResponse;
import io.javalin.Handler;
import io.javalin.security.Role;

import java.util.Set;

public class AuthConfig {
    private final String headerTokenName = "Authorization";

    private JwtProvider jwtProvider;

    public AuthConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public void configure(Handler handler, Context ctx, Set<Role> permittedRoles) throws Exception {
        DecodedJWT jwtToken = getJwtTokenHeader(ctx);
        Role userRole = getUserRole(jwtToken);
        if (userRole == null) {
            userRole = Roles.ANYONE;
        }
        if (!permittedRoles.contains(userRole)) {
            throw new ForbiddenResponse();
        }
        ctx.attribute("username", getUsername(jwtToken));
        handler.handle(ctx);
    }

    private DecodedJWT getJwtTokenHeader(Context ctx) {
        String authHeader = ctx.header(headerTokenName);
        if (authHeader == null) {
            return null;
        }

        String[] tokenParts = authHeader.split("Bearer");

        if (tokenParts.length == 2) {
            String tokenHeader = tokenParts[1].trim();
            return jwtProvider.decodedJWT(tokenHeader);
        } else {
            return null;
        }
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
