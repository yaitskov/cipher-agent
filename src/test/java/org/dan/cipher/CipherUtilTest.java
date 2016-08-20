package org.dan.cipher;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CipherUtilTest {
    private static final String PASSWORD = "hello world! 123";
    private static final String ORIGIN = "asasdfasdfasdfasdfasdf";
    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";

    @Test
    public void symmetric() throws Exception {
        Profile profile = new Profile();
        profile.setBaseAlgorithm("AES");
        profile.setAlgorithm(ALGORITHM);
        profile.setPassword(PASSWORD.getBytes());
        String encrypted = CipherUtil.encrypt(profile, ORIGIN);
        assertFalse(ORIGIN.equals(encrypted));
        String decrypted = CipherUtil.decrypt(profile, encrypted);
        assertEquals(ORIGIN, decrypted);
    }
}
