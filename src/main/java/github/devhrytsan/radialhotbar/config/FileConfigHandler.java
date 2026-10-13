package github.devhrytsan.radialhotbar.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import github.devhrytsan.radialhotbar.Constants;
import github.devhrytsan.radialhotbar.RadialHotBarMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileConfigHandler {
    public static RadialHotBarConfig CONFIG_INSTANCE = new RadialHotBarConfig();
	protected static File settingsFile = RadialHotBarMod.PLATFORM.getConfigDirectory().resolve("radialhotbar.json").toFile();
    protected static Gson baseGson = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        if (settingsFile.exists()) {
            try (FileReader reader = new FileReader(settingsFile)) {
                RadialHotBarConfig loaded = baseGson.fromJson(reader, RadialHotBarConfig.class);
                if (loaded != null) {
                    CONFIG_INSTANCE = loaded;
                } else {
                    // Empty file. Defaults are used and the file is left untouched
                    RadialHotBarMod.MAIN_LOGGER.warn("Radial Hotbar config is empty, using defaults.");
                }
            } catch (IOException | JsonParseException e) {
                // Broken JSON. Defaults are used and the file is left untouched so it can be fixed by hand
                RadialHotBarMod.MAIN_LOGGER.error("Could not load Radial Hotbar config, using defaults!", e);
            }
            CONFIG_INSTANCE.scaleFactor = Math.max(Constants.MIN_SCALE_FACTOR, Math.min(Constants.MAX_SCALE_FACTOR, CONFIG_INSTANCE.scaleFactor));
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            baseGson.toJson(CONFIG_INSTANCE, writer);
        } catch (IOException e) {
			RadialHotBarMod.MAIN_LOGGER.error("Could not save Radial Hotbar config!", e);
        }
    }

}
