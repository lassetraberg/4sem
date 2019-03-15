package authentication.config.authConfig;

import authentication.util.JwtProvider;

public class AuthConfig {
    private final String headerTokenName = "Authorization";

    private JwtProvider jwtProvider;

    public AuthConfig(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


}
