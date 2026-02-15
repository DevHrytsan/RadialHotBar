package github.devhrytsan.radialhotbar;

import com.mojang.blaze3d.platform.InputConstants;
import github.devhrytsan.radialhotbar.config.FileConfigHandler;
import github.devhrytsan.radialhotbar.config.RadialHotBarConfigScreen;
import github.devhrytsan.radialhotbar.menu.RadialMenuController;
import github.devhrytsan.radialhotbar.platform.Platform;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import github.devhrytsan.radialhotbar.platform.fabric.FabricPlatform;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

//?} neoforge {
/*import github.devhrytsan.radialhotbar.platform.neoforge.NeoforgePlatform;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
 *///?} forge {
/*import github.devhrytsan.radialhotbar.platform.forge.ForgePlatform;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
*///?}

@SuppressWarnings("LoggingSimilarMessage")
public class RadialHotBarMod {

	// General
	public static final String MOD_ID = /*$ mod_id*/ "radialhotbar";
	public static final String MOD_VERSION = /*$ mod_version*/ "0.3.0";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Radial Hot Bar";
	public static final Logger MAIN_LOGGER = LoggerFactory.getLogger(MOD_ID);
	// Keys
	public static KeyMapping OPEN_RADIAL_MENU_KEY;

	//? if >=1.21.5 {
	public static final KeyMapping.Category KEYBIND_CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(RadialHotBarMod.MOD_ID, "general"));
	 //? } else {

	/*public static final String KEYBIND_CATEGORY = "key.category." + MOD_ID + ".general";

	*///? }

	public static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		MAIN_LOGGER.info("Initializing {} on {}", MOD_ID, RadialHotBarMod.xplat().loader());
		MAIN_LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static void onInitializeClient() {
		MAIN_LOGGER.info("Initializing {} Client on {}", MOD_ID, RadialHotBarMod.xplat().loader());
		MAIN_LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);

		FileConfigHandler.loadConfig();
	}

	//? fabric {
	public static void InitializeModFabric() {
		OPEN_RADIAL_MENU_KEY = new KeyMapping(
				"key.category.radialhotbar.openkey",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				KEYBIND_CATEGORY
		);

		KeyBindingHelper.registerKeyBinding(OPEN_RADIAL_MENU_KEY);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			RadialMenuController.INSTANCE.HandleUpdate(client);
		});
	}
	//?} neoforge {

	/*public static void RegisterKeysNeoForge(RegisterKeyMappingsEvent event) {
		OPEN_RADIAL_MENU_KEY = new KeyMapping(
				"key.category.radialhotbar.openkey",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				KEYBIND_CATEGORY
		);

		event.register(OPEN_RADIAL_MENU_KEY);
	}

	public static void InitializeModNeoForge(FMLClientSetupEvent setupEvent) {

		Minecraft clientI = Minecraft.getInstance();

		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
			// Call your update logic here
			if (clientI.player != null) {
				RadialMenuController.INSTANCE.HandleUpdate(clientI);
			}
		});

		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> {
			return RadialHotBarConfigScreen.createConfigScreen(parent);
		}); //TODO: Rewrite that because of deprecated API.
	}

	*///?} forge {

	/*public static void RegisterKeysForge(RegisterKeyMappingsEvent event) {
		OPEN_RADIAL_MENU_KEY = new KeyMapping(
				"key.category.radialhotbar.openkey",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				KEYBIND_CATEGORY
		);
		event.register(OPEN_RADIAL_MENU_KEY);
	}

	public static void InitializeModForge(FMLClientSetupEvent setupEvent) {

		Minecraft clientI = Minecraft.getInstance();

		MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
			if (event.phase == TickEvent.Phase.END) {
				RadialMenuController.INSTANCE.HandleUpdate(clientI);
			}
		});

		ModLoadingContext.get().registerExtensionPoint(
				ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> {
					return RadialHotBarConfigScreen.createConfigScreen(parent);
				})
		); //TODO: Rewrite that because of deprecated API.
	}

	*///?}

	static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		 //?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		*///?}
	}
}
