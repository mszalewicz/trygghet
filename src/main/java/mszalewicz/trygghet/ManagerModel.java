package mszalewicz.trygghet;

import java.util.ArrayList;
import java.util.List;

public class ManagerModel {
    private final DB db;

    ManagerModel(DB appDB) {
       this.db = appDB;
    }

    public static enum BootstrapDB {
        DONE,
        ERROR,
        NOT_DONE;
    }

    public final BootstrapDB checkIfNewDBAndPopulate() {
        long masterCount = Long.MIN_VALUE;
        long passwordsCount = Long.MIN_VALUE;
        long passwordsCopyCount = Long.MIN_VALUE;
        long cryptoCount = Long.MIN_VALUE;

        DB.DatabaseReturn returnedCountMasterTable = this.db.countAllEntriesOfTable(DB.Tables.MASTER);
        if (returnedCountMasterTable.queryExecuted) { masterCount = returnedCountMasterTable.count; }

        DB.DatabaseReturn returnedCountPasswordsTable = this.db.countAllEntriesOfTable(DB.Tables.PASSWORDS);
        if (returnedCountPasswordsTable.queryExecuted) { passwordsCount = returnedCountPasswordsTable.count; }

        DB.DatabaseReturn returnedCountPasswordsCopyTable = this.db.countAllEntriesOfTable(DB.Tables.PASSWORDS_COPY);
        if (returnedCountPasswordsCopyTable.queryExecuted) { passwordsCopyCount = returnedCountPasswordsCopyTable.count; }

        DB.DatabaseReturn returnedCountCryptoTable = this.db.countAllEntriesOfTable(DB.Tables.CRYPTO);
        if (returnedCountCryptoTable.queryExecuted) { cryptoCount = returnedCountCryptoTable.count; }

        if (masterCount != 0 || passwordsCount != 0 || passwordsCopyCount != 0 || cryptoCount != 0) {
            System.out.println("some tables are not empty");
            return BootstrapDB.NOT_DONE;
        }

        if (masterCount == 0 && passwordsCount == 0 && passwordsCopyCount == 0 && cryptoCount == 0) {
            String secrectKey = CipherTransformations.generateKeyString();
            String iv = CipherTransformations.generateIvString();

            this.db.insertCryptoPrimitives(secrectKey, iv);
        }

        return BootstrapDB.DONE;
    }

    public boolean masterPasswordExists() {
        return this.db.masterPasswordExists();
    }

    public void insertFirstMasterPassword(String newPassword) {
        this.db.insertFirstMasterPassword(newPassword);
    }

    public List<String> getAllPasswordServiceNames() {
        List<String> serviceNames = new ArrayList<String>();
        serviceNames = this.db.getAllServiceName();
        return serviceNames;
    }
}
