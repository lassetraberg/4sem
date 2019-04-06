package authentication.config.authConfig;

import authentication.util.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import commonAuthentication.config.authConfig.Role;
import io.javalin.Context;
import io.javalin.ForbiddenResponse;
import io.javalin.Handler;

import java.util.Set;

public class AuthConfig {

    private JwtProvider jwtProvider;

    public AuthConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public void configure(Handler handler, Context ctx, Set<io.javalin.security.Role> permittedRoles) throws Exception {
        DecodedJWT jwtToken = AuthUtil.getJwtTokenHeader(ctx.header(AuthUtil.headerTokenName), jwtProvider);
        io.javalin.security.Role userRole = AuthUtil.getUserRole(jwtToken);
        if (userRole == null) {
            userRole = Role.ANYONE;
        }
        if (!permittedRoles.contains(userRole)) {
            throw new ForbiddenResponse();
        }
        ctx.attribute("username", AuthUtil.getUsername(jwtToken));
        handler.handle(ctx);
    }
}
