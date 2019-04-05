package authentication.config.authConfig;

import authentication.util.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import commonAuthentication.config.authConfig.Roles;
import io.javalin.security.Role;

class AuthUtil {
    static final String headerTokenName = "Authorization";

    static DecodedJWT getJwtTokenHeader(String authHeader, JwtProvider jwtProvider) {
        if (authHeader == null || !authHeader.contains("Bearer ")) {
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

    static String getUsername(DecodedJWT jwtToken) {
        if (jwtToken == null) {
            return null;
        }
        return jwtToken.getSubject();
    }

    static Role getUserRole(DecodedJWT jwtToken) {
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
