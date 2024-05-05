package mszalewicz.trygghet;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;

public class DB {
    public class DatabaseReturn {
        public Exception exception = null;
        public boolean queryExecuted = true;
        public long count = Long.MIN_VALUE;
        public QueryType type;
        public String info = null;

       DatabaseReturn(QueryType type)  {
           this.type = type;
       }
    }

    public static enum QueryType {
        COUNT
    }

    public static enum Tables {
        PASSWORDS("passwords"),
        PASSWORDS_COPY("passwords_tmp_copy"),
        MASTER("master"),
        CRYPTO("crypto");

        public final String name;

        private Tables(String name) {
            this.name = name;
        }
    }

    private final String dbFilePathname;
    private final Connection connection;

    DB(String pathname) {
        File f = new File(pathname);
        boolean fileDoesNotExists = !f.exists();
        boolean fileIsNotAFile = !f.isFile();
        boolean isNewDb = false;
        boolean createdDBWithScheme = true;

        if (fileDoesNotExists || fileIsNotAFile) {
            // TODO better logging
            System.out.println("creating new db under path: " + pathname);
            isNewDb = true;
        }

        String url = "jdbc:sqlite:" + pathname;
        Connection conn = null;

        try  {
            conn = DriverManager.getConnection(url);

            if (conn == null) {
                //TODO better logging
                System.err.println("could not open db connection");
                System.exit(1);
            }
        } catch (SQLException e) {
            //TODO better logging
            System.err.println("db connection error during db boostrapping");
            System.err.println("sql error code: " + e.getErrorCode());
            e.printStackTrace();
            System.exit(1);
        }

        if (isNewDb) {
            createdDBWithScheme = bootstrapDB(conn);
        }

        if (!createdDBWithScheme) {
            //TODO better logging
            System.err.println("could not boostrap db with scheme");
            System.exit(1);
        }

        this.connection = conn;
        this.dbFilePathname = pathname;
    }

    private boolean bootstrapDB(Connection conn) {
        Statement statement = null;

        try {
            statement =  conn.createStatement();

            String createPasswordsTable = """
                    CREATE TABLE passwords (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT UNIQUE NOT NULL,
                        password TEXT UNIQUE NOT NULL,
                        created_at TIMESTAMP NULL,
                        updated_at TIMESTAMP NULL
                    );
                """;

            statement.execute(createPasswordsTable);
            // -----------------------------------------------------

            String createPasswordsTmpCopyTable = """
                    CREATE TABLE passwords_tmp_copy (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT UNIQUE NOT NULL,
                        password TEXT UNIQUE NOT NULL,
                        created_at TIMESTAMP NULL,
                        updated_at TIMESTAMP NULL
                    );
                """;

            statement.execute(createPasswordsTmpCopyTable);
            // -----------------------------------------------------

            String createMasterTable = """
                    CREATE TABLE master (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        password TEXT UNIQUE NOT NULL,
                        created_at TIMESTAMP NULL,
                        updated_at TIMESTAMP NULL
                    );
                """;

            statement.execute(createMasterTable);
            // -----------------------------------------------------

            String createCryptoTable = """
                    CREATE TABLE crypto (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        secret_key TEXT UNIQUE NOT NULL,
                        iv TEXT UNIQUE NOT NULL,
                        created_at TIMESTAMP NULL,
                        updated_at TIMESTAMP NULL
                    );
                """;

            statement.execute(createCryptoTable);

        } catch (SQLException e) {
            //TODO better logging
            System.err.println("error during db bootstrapping");
            System.err.println("sql error code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //TODO better logging
                    System.err.println("could not close sql statement after bootstrapping db with scheme");
                    System.err.println("sql error code: " + e.getErrorCode());
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public DatabaseReturn countAllEntriesOfTable(Tables table) {
        Statement statement = null;
        DatabaseReturn dbret = new DatabaseReturn(QueryType.COUNT);

        try {
            String sqlQuery = "SELECT COUNT(*) AS count FROM " + table.name + ";";
            statement = this.connection.createStatement();
            boolean queryReturnedValue = statement.execute(sqlQuery);

            if (queryReturnedValue) {
                var queryResultSet = statement.getResultSet();

                if ( queryResultSet != null && queryResultSet.next()) {
                    dbret.count = queryResultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            // todo add better logging
            System.err.println("could not execute count query");
            e.printStackTrace();
            dbret.exception = e;
            dbret.queryExecuted = false;
            return dbret;
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
               // todo add better logging
                System.err.println("could not close db statement");
            }
        }

        return dbret;
    }

    public boolean masterPasswordExists() {
        int masterPasswordOccurrences = Integer.MIN_VALUE;
        String sqlQuery = "SELECT count(*) AS count FROM master;";

        try {
            Statement statement = connection.createStatement();
            statement.execute(sqlQuery);
            masterPasswordOccurrences = statement.getResultSet().getInt("count");
        } catch (SQLException e) {
            // todo better logging
            System.err.println("could not execute query for masterPasswordExists");
            e.printStackTrace();
        }

        return masterPasswordOccurrences == 1;
    }

    public boolean insertFirstMasterPassword(String newPassword) {
        try {
            String sqlQuery = "INSERT INTO master (password, created_at, updated_at) VALUES (?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, newPassword);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected != 1) {
                System.err.println("insert statement executed incorrectly, expected insert of 1 row, received insert of " + rowsAffected + " rows");
                System.exit(1);
            }
        } catch (SQLException e) {
            //TODO add better logging
            //TODO add better logging
            System.err.println("could not insert new master passwords to db");
            System.err.println("sql error code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertCryptoPrimitives(String secretKey, String iv) {
        String sqlQuery = """
               INSERT INTO crypto
                   (secret_key, iv, created_at, updated_at)
               VALUES
                   (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
               """;

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, secretKey);
            preparedStatement.setString(2, iv);
            int rowsAffected = preparedStatement.executeUpdate();
            // TODO add better logging
            System.err.println("inserted " + rowsAffected + " row(s) into crypto table");
        } catch (SQLException e) {
            //TODO add better logging
            System.err.println("could not insert crypto primitives");
            return false;
        }
        return true;
    }
}




















