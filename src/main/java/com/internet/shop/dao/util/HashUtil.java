package com.internet.shop.dao.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
    private static final String HASH_ALGORITHM = "SHA-512";

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashedPassword = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(password.getBytes());
            for (byte b : digest) {
                hashedPassword.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't hash password", e);
        }
        return hashedPassword.toString();
    }

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
