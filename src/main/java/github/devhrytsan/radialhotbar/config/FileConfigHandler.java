package github.devhrytsan.radialhotbar.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
                CONFIG_INSTANCE = baseGson.fromJson(reader, RadialHotBarConfig.class);
            } catch (IOException e) {
                RadialHotBarMod.MAIN_LOGGER.error("Could not load Radial Hotbar config!", e);
            }
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
