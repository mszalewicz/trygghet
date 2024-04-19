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
    private Entries entries;

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

        assert result != null : "Opening/parsing of file" + this.settingsPath + " failed. Result is a null object.";

        if (result.hasErrors()) {
            System.err.println("file " + this.settingsPath + " contains errors with regards to TOML syntax");
        } else {
            long PBEIterationCountFromFile = result.getLong("PBEIterationCount");

            this.entries.PBEIterationCount = PBEIterationCountFromFile;
        }
    }
}