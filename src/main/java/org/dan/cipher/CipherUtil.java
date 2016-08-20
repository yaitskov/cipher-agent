package org.dan.cipher;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtil {
    public static String encrypt(Profile profile, String value) throws Exception {
        Cipher cipher = Cipher.getInstance(profile.getAlgorithm());
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        SecretKeySpec skeySpec = new SecretKeySpec(
                profile.getPassword(),
                profile.getBaseAlgorithm()); // "AES"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
                new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(value.getBytes());
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(iv) + " " + encoder.encodeToString(encrypted);
    }

    public static String decrypt(Profile profile, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(profile.getAlgorithm());
        SecretKeySpec skeySpec = new SecretKeySpec(
                profile.getPassword(),
                profile.getBaseAlgorithm());
        byte[] iv = new byte[cipher.getBlockSize()];
        final Base64.Decoder decoder = Base64.getDecoder();
        int space = encrypted.indexOf(' ');
        if (space < 0) {
            throw new IllegalArgumentException("No delimiter between iv and data");
        }
        String encodedIv = encrypted.substring(0, space);
        String data = encrypted.substring(space + 1);
        decoder.decode(encodedIv.getBytes(), iv);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        byte[] original = cipher.doFinal(decoder.decode(data));
        return new String(original);
    }
}
