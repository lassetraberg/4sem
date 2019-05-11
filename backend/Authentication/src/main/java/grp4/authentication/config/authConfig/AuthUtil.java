package grp4.authentication.config.authConfig;

import grp4.authentication.util.JwtProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import grp4.commonAuthentication.config.authConfig.Role;

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

    static io.javalin.security.Role getUserRole(DecodedJWT jwtToken) {
        if (jwtToken == null) {
            return null;
        }
        String role = jwtToken.getClaim("role").asString();
        if (role == null) {
            return null;
        }

        return Role.valueOf(role);
    }
}
