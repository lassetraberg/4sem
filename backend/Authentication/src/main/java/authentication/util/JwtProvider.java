package authentication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import commonAuthentication.domain.model.Account;
import io.javalin.security.Role;

import java.time.Instant;
import java.util.Date;

public class JwtProvider {
    private Algorithm algorithm = Algorithm.HMAC256("secret"); // TODO indsæt rigtig secret her :)

    private long tokenDuration = 1 * 24 * 60 * 60 * 1000; // 24 hours

    public DecodedJWT decodedJWT(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    public String createJWT(Account account, Role role) {
        return JWT.create()
                .withIssuedAt(new Date())
                .withSubject(account.getUsername())
                .withClaim("role", role.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenDuration))
                .sign(algorithm);
    }

    public Instant getTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + tokenDuration).toInstant();
    }
}
