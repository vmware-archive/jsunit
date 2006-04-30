package net.jsunit.captcha;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher {

    public static final String ENCRYPTION_TYPE = "AES";
    public static final String ENCODING = "UTF8";

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public AesCipher(String secretKey) {
        try {
            byte[] rawSecretKey = secretKey.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(rawSecretKey, ENCRYPTION_TYPE);
            this.encryptCipher = Cipher.getInstance(ENCRYPTION_TYPE);
            this.encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            this.decryptCipher = Cipher.getInstance(ENCRYPTION_TYPE);
            this.decryptCipher.init(Cipher.DECRYPT_MODE, keySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String string) {
        try {
            byte[] encryptedBytes = encryptCipher.doFinal(string.getBytes(ENCODING));
            return new BASE64Encoder().encode(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String string) {
        try {
            byte[] decodedBytes = new BASE64Decoder().decodeBuffer(string);
            byte[] decryptedBytes = decryptCipher.doFinal(decodedBytes);
            return new String(decryptedBytes, ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}