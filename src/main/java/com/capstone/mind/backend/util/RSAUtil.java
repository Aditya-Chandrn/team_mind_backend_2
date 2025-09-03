package com.capstone.mind.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class RSAUtil {
    private final String privateKey;

    public RSAUtil(@Value("${rsa.private-key:}") String privateKey) {
        this.privateKey = privateKey;
    }

    public String decrypt(String encryptedData) throws Exception {
        if (privateKey == null || privateKey.isEmpty()) {
            // For development/testing, return the input as-is
            return encryptedData;
        }

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        PrivateKey privateKeyObj = getPrivateKey(this.privateKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKeyObj);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    private PrivateKey getPrivateKey(String base64PrivateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public boolean isRSAEnabled() {
        return privateKey != null && !privateKey.isEmpty();
    }
}