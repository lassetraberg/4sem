package grp4.authentication.config.authConfig;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import grp4.authentication.util.JwtProvider;
import grp4.common.spi.IWebSocketAuthenticationService;
import io.javalin.security.Role;

import java.util.Set;

public class WebSocketAuthenticationProvider implements IWebSocketAuthenticationService {
    private JwtProvider jwtProvider;

    public WebSocketAuthenticationProvider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public boolean doesUserHaveRole(Set<Role> permittedRoles, String authMsg) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = AuthUtil.getJwtTokenHeader(authMsg, jwtProvider);
        } catch (SignatureVerificationException decodeEx) {
            return false;
        }
        return permittedRoles.contains(AuthUtil.getUserRole(decodedJWT));
    }

    @Override
    public String getUsername(String authMsg) {
        DecodedJWT decodedJWT = AuthUtil.getJwtTokenHeader(authMsg, jwtProvider);
        return AuthUtil.getUsername(decodedJWT);
    }
}
