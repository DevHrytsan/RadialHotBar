package github.devhrytsan.radialhotbar;

import github.devhrytsan.radialhotbar.config.RadialHotBarConfig;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {

    // Mod
    public static final String MOD_ID = "radialhotbar";
    public static final String MOD_NAME = "Radial Hot Bar";
    public static final Logger MAIN_LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String KEYBIND_CATEGORY = "key.category." + MOD_ID + ".general";
    // UI
    public static final int DEFAULT_SCALE_FACTOR = 3;
    public static final int MIN_SCALE_FACTOR = 1;
    public static final int MAX_SCALE_FACTOR = 5;

}
