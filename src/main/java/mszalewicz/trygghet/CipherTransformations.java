package mszalewicz.trygghet;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CipherTransformations {
    private String settingsPath;
    private int PBEIterationCount;
    private static String algorithm = "AES/GCM/NoPadding";

    CipherTransformations(String settingsPath){
       this.settingsPath = settingsPath;
    }

    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

   public SecretKey getKeyFromPassword(String password, String salt) throws Exception {
       SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
       KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), this.PBEIterationCount, 256);
       SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
       return secret;
   }

    public static String encrypt(String plaintext, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(ciphertext);
    }

    public static String decrypt() throws Exception {

        Cipher cipher = Cipher.getInstance(algorithm);

        // TODO return real decrypted plaintext of password
        return "error";
    }
}