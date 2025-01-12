package mszalewicz.trygghet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Decryptor {

    public static String decryptSymmetric128BitHexKeyUTF8(String ciphertext, String iv, String tag, String key) throws Exception {
        String algorithm = "AES/GCM/NoPadding";

        byte[] ivBytes = Base64.getDecoder().decode(iv);
        byte[] tagBytes = Base64.getDecoder().decode(tag);
        byte[] keyBytes = key.getBytes("UTF-8");

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
        cipher.updateAAD(tagBytes);

        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);

        return new String(decryptedBytes, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        String ciphertext = "2LPVwa7s4+xyd8KF94r07TOCaOdf4X90NWqhHQCpoJGT+T3TYjKCWf3V+A==";
        String iv = "S4/yimaF8bu0jQW9uagIJA==";
        String tag = "L6BtTic18dd4fNL2maytFA==";
        String key = "password1232asdfpassword1232asdf";

        String decryptedText = decryptSymmetric128BitHexKeyUTF8(ciphertext, iv, tag, key);
        System.out.println(decryptedText);
    }
}