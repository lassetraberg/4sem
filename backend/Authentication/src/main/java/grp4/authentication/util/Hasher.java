package grp4.authentication.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Hasher implements IHasher {
    @Override
    public String hashPassword(String plaintextPassword) {
        return BCrypt.withDefaults().hashToString(12, plaintextPassword.toCharArray());
    }

    @Override
    public boolean isPasswordCorrect(String plaintextPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plaintextPassword.toCharArray(), hashedPassword).verified;
    }
}
