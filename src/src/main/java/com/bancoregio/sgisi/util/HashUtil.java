package com.bancoregio.sgisi.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Utilidad para generar hashes de contraseñas.
 *
 * El sistema no compara contraseñas en texto plano contra la base de datos: se
 * calcula SHA-256 y se busca el usuario por email + hash. Para un prototipo es
 * suficiente, aunque en sistemas reales se suele usar hash con salt y un
 * algoritmo específico para contraseñas.
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
