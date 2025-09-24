package com.a7.job.auth.security.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

public class KeyUtils {
    private static final Logger logger = Logger.getLogger(KeyUtils.class.getName());

    public static PrivateKey loadPrivateKey(String privateKeyPath) {
        try {
            final KeySpec keySpec = readAndFormKeySpec(privateKeyPath, false);
            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (Exception e) {
            logger.warning("Failed to load private key: " + e.getMessage());
            return null;
        }
    }

    public static PublicKey loadPublicKey(String publicKeyPath) {
        try {
            final KeySpec keySpec = readAndFormKeySpec(publicKeyPath, true);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            logger.warning("Failed to load public key: " + e.getMessage());
            return null;
        }
    }

    private static KeySpec readAndFormKeySpec(String keyPath, boolean isPublic) throws IOException {
        final String keyAsString = readKeyFromResource(keyPath).replaceAll("\\s", "");
        final byte[] keyBytes = Base64.getDecoder().decode(keyAsString);
        if (isPublic) {
            return new X509EncodedKeySpec(keyBytes);
        }
        return new PKCS8EncodedKeySpec(keyBytes);
    }

    private static String readKeyFromResource(String keyPath) throws IOException {
        try (InputStream inputStream = KeyUtils.class.getClassLoader().getResourceAsStream(keyPath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + keyPath);
            }
            return new String(inputStream.readAllBytes());
        }
    }

    private KeyUtils() {
    }
}
