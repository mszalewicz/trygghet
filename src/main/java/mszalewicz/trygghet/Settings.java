package mszalewicz.trygghet;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {
    public class Entries {
        public long PBEIterationCount;
        public String dbFilePath;
    }

    private final String settingsPath;
    public Entries entries = new Entries();

    Settings(String settingsPath) {
        this.settingsPath = settingsPath;
        readSettings();
    }

    private void readSettings() {
        Path source = Paths.get(this.settingsPath);
        TomlParseResult result = null;

        try {
            result = Toml.parse(source);
        } catch (IOException e) {
            // TODO implement true logging to file, instead of err.println
            System.err.println("could not open/parse file " + this.settingsPath);
            e.printStackTrace();
            System.exit(1);
        }

        try {
            assert result.isEmpty() : "file" + this.settingsPath + "has no entries.";
        } catch (AssertionError e) {
           e.printStackTrace();
           System.exit(1);
        }

        if (result.hasErrors()) {
           // TODO implement logging
            System.err.println("file " + this.settingsPath + " contains errors with regards to TOML syntax");
            System.exit(1);
        } else {
           try {
               assert result.contains("PBEIterationCount") : "settings TOML does not contain PBEIterationCount entry.";
               assert result.contains("DBFilePath") : "settings TOML does not contain DBFilePath entry.";
           } catch (AssertionError e)  {
               System.err.println("settings file does not contain one of the expected entries");
               e.printStackTrace();
               System.exit(1);
           }
           this.entries.PBEIterationCount = result.getLong("PBEIterationCount");
           this.entries.dbFilePath = result.getString("DBFilePath");
        }
    }
}