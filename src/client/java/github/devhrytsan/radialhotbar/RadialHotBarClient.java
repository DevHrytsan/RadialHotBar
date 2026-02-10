package github.devhrytsan.radialhotbar;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import github.devhrytsan.radialhotbar.config.FileConfigHandler;
import github.devhrytsan.radialhotbar.menu.RadialMenuScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class RadialHotBarClient implements ClientModInitializer {

    private static KeyBinding openRadialMenuKey; //https://wiki.fabricmc.net/tutorial:keybinds
    private static boolean isMenuOpen = false;
    private static boolean wasKeyPressed = false;

    private static boolean lastRadialMenuState;

    @Override
    public void onInitializeClient() {
        Constants.MAIN_LOGGER.info("Initializing Radial Hot Bar");

        FileConfigHandler.loadConfig();

        openRadialMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.category.radialhotbar.openkey",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                Constants.KEYBIND_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (!FileConfigHandler.CONFIG_INSTANCE.modEnabled) return;
            if (client.player == null) return; //Safety Checks

            HandleUpdate(client);
        });
    }

    public void HandleUpdate(MinecraftClient client) {
        var clientWindow = client.getWindow();
        long windowHandle = clientWindow.getHandle();

        InputUtil.Key boundKey = KeyBindingHelper.getBoundKeyOf(openRadialMenuKey);
        int keyCode = boundKey.getCode();

        // Seriously,
        // due how Minecraft handles inputs when a Screen is open. So when a Menu opens, Minecraft stops updating gameplay keys.
        // It assumes you are typing in chat or searching inventory, so it forces all gameplay keys to False(aka prevents walking)
        // So for it, I need a hardware check of the button.
        // Thanks to mod called "MineMenu" I learnt how to deal with it.

        if (keyCode >= 0) {
            boolean radialMenuKeyDown = (boundKey.getCategory() == InputUtil.Type.MOUSE ?
                    GLFW.glfwGetMouseButton(windowHandle, keyCode) == 1 : InputUtil.isKeyPressed(clientWindow.getHandle(), keyCode));

            if (radialMenuKeyDown != lastRadialMenuState) {
                if (radialMenuKeyDown != RadialMenuScreen.INSTANCE.active) {
                    if (radialMenuKeyDown) {
                        if (client.currentScreen == null || client.currentScreen instanceof RadialMenuScreen) {
                            RadialMenuScreen.INSTANCE.activate();
                        }
                    } else {
                        var window = client.getWindow();
                        RadialMenuScreen.INSTANCE.selectItem(client.mouse.getX() / client.getWindow().getScaleFactor(), client.mouse.getY() / client.getWindow().getScaleFactor(), 0);
                        RadialMenuScreen.INSTANCE.deactivate();
                    }
                }
            }
            lastRadialMenuState = radialMenuKeyDown;
        }

    }

}