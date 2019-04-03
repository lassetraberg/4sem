package authentication.util;

import commonAuthentication.domain.model.Account;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.security.Role;

import java.util.Date;

public class JwtProvider {
    private Algorithm algorithm = Algorithm.HMAC256("secret"); // TODO indsæt rigtig secret her :)

    public DecodedJWT decodedJWT(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    public String createJWT(Account account, Role role) {
        return JWT.create()
                .withIssuedAt(new Date())
                .withSubject(account.getUsername())
                .withClaim("role", role.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000)) // 24 hours
                .sign(algorithm);
    }
}
