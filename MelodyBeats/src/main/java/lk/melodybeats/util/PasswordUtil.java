/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lk.melodybeats.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

    private PasswordUtil() {
        // Prevent object creation
    }

    public static String hash(String password) {

        if (password == null) {
            throw new IllegalArgumentException(
                    "Password cannot be null."
            );
        }

        try {
            MessageDigest messageDigest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes =
                    messageDigest.digest(
                            password.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte hashedByte : hashedBytes) {
                result.append(
                        String.format(
                                "%02x",
                                hashedByte & 0xff
                        )
                );
            }

            return result.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 is not available.",
                    exception
            );
        }
    }
}
