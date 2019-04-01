package authentication.config.authConfig;

import authentication.util.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import common.spi.IWebSocketAuthenticationService;
import io.javalin.security.Role;

import java.util.Set;

public class WebSocketAuthenticationProvider implements IWebSocketAuthenticationService {
    private JwtProvider jwtProvider;

    public WebSocketAuthenticationProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public boolean doesUserHaveRole(Set<Role> permittedRoles, String authMsg) {
        DecodedJWT decodedJWT = AuthUtil.getJwtTokenHeader(authMsg, jwtProvider);
        return permittedRoles.contains(AuthUtil.getUserRole(decodedJWT));
    }

    @Override
    public String getUsername(String authMsg) {
        DecodedJWT decodedJWT = AuthUtil.getJwtTokenHeader(authMsg, jwtProvider);
        return AuthUtil.getUsername(decodedJWT);
    }
}
