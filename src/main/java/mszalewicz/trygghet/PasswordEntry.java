package mszalewicz.trygghet;

import javax.crypto.Cipher;
import java.io.File;

public class PasswordEntry {
    public boolean deleted = false;
    private String  passwordHash;
    private String name;
    private File storage;

    void deleteEntry() {
        // TODO delete given line from the passwords file

        // Clean up to not leave information in memory

        name = "";
        passwordHash = "";
        deleted = true;
    }

    String getName() {
       return name;
    }

    String getPassword(String adminPassword) throws Exception {
        // TODO unhash AES 256 GCM
        Cipher cipher = Cipher.getInstance("AES");
        return passwordHash;
    }

    boolean isDeleted() {
        return deleted;
    }
}