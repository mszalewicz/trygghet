package mszalewicz.trygghet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CipherTransformations {

    private static String algorithm = "AES/GCM/NoPadding";


    public static void Encrypt() throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, );
    }

    public static String Decrypt() throws Exception {

        Cipher cipher = Cipher.getInstance(algorithm);

        // TODO return real decrypted plaintext of password
        return "error";
    }


}
