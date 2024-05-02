package mszalewicz.trygghet;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
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
            e.printStackTrace();
            System.exit(1);
        }

        createdDBWithScheme = bootstrapDB(conn);

        if (!createdDBWithScheme) {
            System.err.println("could not boostrap db with scheme");
            System.exit(1);
        }

        this.connection = conn;
        this.dbFilePathname = pathname;
    }

    public boolean bootstrapDB(Connection conn) {
        try (Statement statement = conn.createStatement()) {

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

        } catch (SQLException e) {
            // TODO add logging
            System.err.println("error during db bootstrapping");
            e.printStackTrace();

           // TODO delete db and create new one but only during bootstrapping phase

            return false;
        }
        return true;
    }

    public boolean test() {
        try
        (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbFilePathname);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);

            return false;
        }

        return true;
    }
}

/*
File f = new File(filePathString);
if(f.exists() && f.isFile()) {
    //do something ...
}
 */





















