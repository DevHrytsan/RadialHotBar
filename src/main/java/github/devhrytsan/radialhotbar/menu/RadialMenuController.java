package github.devhrytsan.radialhotbar.menu;

import com.mojang.blaze3d.platform.InputConstants;
import github.devhrytsan.radialhotbar.RadialHotBarMod;

import github.devhrytsan.radialhotbar.config.FileConfigHandler;
import github.devhrytsan.radialhotbar.utils.ClientPlayerUtils;
import github.devhrytsan.radialhotbar.utils.KeyInputUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;


public class RadialMenuController {

	public static final RadialMenuController INSTANCE = new RadialMenuController();

	private boolean isMenuOpen = false;
	private boolean wasKeyPressed = false;
	private boolean lastRadialMenuState;

	public void HandleUpdate(Minecraft client) {
		boolean isEnabled = FileConfigHandler.CONFIG_INSTANCE.modEnabled;

		var clientWindow = client.getWindow();

		// Seriously,
		// due how Minecraft handles inputs when a Screen is open. So when a Menu opens, Minecraft stops updating gameplay keys.
		// It assumes you are typing in chat or searching inventory, so it forces all gameplay keys to False(aka prevents walking)
		// So for it, I need a hardware check of the button.
		// Thanks to mod called "MineMenu" I learnt how to deal with it.
		if (isEnabled) {
				boolean radialMenuKeyDown = KeyInputUtils.isHardwareKeyPressed(RadialHotBarMod.OPEN_RADIAL_MENU_KEY, clientWindow);

				if (radialMenuKeyDown != lastRadialMenuState) {
					if (radialMenuKeyDown != RadialMenuScreen.INSTANCE.active) {

						if (radialMenuKeyDown) {
							if ((client.screen == null || client.screen instanceof RadialMenuScreen)) {
								RadialMenuScreen.INSTANCE.activate();
							}
						} else {
							var window = client.getWindow();

							double scaledMouseX = ClientPlayerUtils.getScaledMouseX(client);
							double scaledMouseY = ClientPlayerUtils.getScaledMouseY(client);

							RadialMenuScreen.INSTANCE.selectItem(scaledMouseX, scaledMouseY, 0);
							RadialMenuScreen.INSTANCE.deactivate(scaledMouseX, scaledMouseY);
						}
					}
				}
				lastRadialMenuState = radialMenuKeyDown;
		} else {
			if (RadialMenuScreen.INSTANCE.active) RadialMenuScreen.INSTANCE.deactivate(0, 0);
		}
	}
}
