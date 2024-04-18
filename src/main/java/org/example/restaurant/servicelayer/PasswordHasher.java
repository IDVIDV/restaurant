package org.example.restaurant.servicelayer;

public interface PasswordHasher {
    String hash(String pass, String salt);
    boolean check(String candidate, String salt, String hashedPass);
}
