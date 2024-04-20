package mszalewicz.trygghet;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {
    private static class Entries {
        public long PBEIterationCount;
    }

    private final String settingsPath;
    private Entries entries = new Entries();

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
            assert result.isEmpty() : "File" + this.settingsPath + "has no entries.";
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
               assert result.contains("PBEIterationCount") : "Settings TOML does not contain PBEIterationCount entry.";
           } catch (AssertionError e)  {
               e.printStackTrace();
               System.exit(1);
           }
           this.entries.PBEIterationCount = result.getLong("PBEIterationCount");
        }
    }
}