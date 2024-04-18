package org.example.restaurant.servicelayer;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PasswordHasherImpl implements PasswordHasher {
    private final SecretKeyFactory secretKeyFactory;

    public PasswordHasherImpl() throws NoSuchAlgorithmException {
        secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    }

    @Override
    public String hash(String pass, String salt) {
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt.getBytes(StandardCharsets.UTF_8),
                65536, 1024);
        try {
            return Base64.getEncoder().encodeToString(secretKeyFactory.generateSecret(spec).getEncoded());
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean check(String candidate, String salt, String hashedPass) {
        KeySpec spec = new PBEKeySpec(candidate.toCharArray(), salt.getBytes(StandardCharsets.UTF_8),
                65536, 1024);
        try {
            return Base64.getEncoder()
                    .encodeToString(secretKeyFactory.generateSecret(spec).getEncoded())
                    .equals(hashedPass);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
