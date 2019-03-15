package authentication.util;

import com.auth0.jwt.algorithms.Algorithm;

public class Cipher {

    private static Algorithm algorithm = Algorithm.HMAC256("secret"); // TODO indsæt rigtig secret her :)

    public static byte[] encrypt(String data) {
        return algorithm.sign(data.getBytes());
    }

    public static Algorithm getAlgorithm() {
        return algorithm;
    }
}
