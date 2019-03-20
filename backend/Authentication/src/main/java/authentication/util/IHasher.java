package authentication.util;

public interface IHasher {
    String hashPassword(String plaintextPassword);

    boolean isPasswordCorrect(String plaintextPassword, String hashedPassword);
}
