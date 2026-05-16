package com.bancoregio.sgisi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Utilidad para hashing SHA-256.
 */
public final class HashUtil {
    private HashUtil() {
    }

    /**
     * Genera hash SHA-256 hexadecimal de texto plano.
     */
    public static String sha256(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : out) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar hash", e);
        }
    }
}
