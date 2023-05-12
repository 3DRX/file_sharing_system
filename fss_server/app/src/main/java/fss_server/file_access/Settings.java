package fss_server.file_access;

import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * This class is responsible for reading the settings file.
 */
public class Settings {
    final private String path = "src/main/resources/settings.json";
    private settingsRecord settings;
    private File root;
    private final Logger logger = LoggerFactory.getLogger(Settings.class);

    /**
     * Read settings file and create root directory if needed.
     */
    public Settings() {
        this.settings = readSettings();
        final File folder = new File(this.settings.root());
        if (!folder.exists()) {
            folder.mkdir();
            logger.info("Root path does not exist, creating it");
        } else if (!folder.isDirectory()) {
            logger.error("Root path is not a directory, exit program");
            System.exit(1);
        }
        this.root = folder;
        logger.info("Load root path success: " + this.settings.root());
    }

    private settingsRecord readSettings() {
        settingsRecord res = null;
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            res = new Gson().fromJson(reader, settingsRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @return root directory
     */
    public File getRoot() {
        return this.root;
    }
}

record settingsRecord(
        String root) {
}
