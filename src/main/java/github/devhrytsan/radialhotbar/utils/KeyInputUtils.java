package github.devhrytsan.radialhotbar.utils;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

//? fabric {
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//?}

public class KeyInputUtils {

	public static InputConstants.Key GetBoundKey(KeyMapping keyBinding) {
		//? fabric {
		return KeyBindingHelper.getBoundKeyOf(keyBinding);
		//?} neoforge {

		/*return keyBinding.getKey(); // it may be wrong

		*///?} forge {

		/*return keyBinding.getKey();

		*///?}

	}

	public static boolean isKeyDown(Window window, int keyCode){
		//? if >=1.21.5 {
		return InputConstants.isKeyDown(window, keyCode);
		//? } else {

		/*long handle = ClientPlayerUtils.GetWindowHandle(window);
		return InputConstants.isKeyDown(handle, keyCode);

		*///? }
	}

	public static boolean isHardwareKeyPressed(KeyMapping keyMapping, Window window) {
		InputConstants.Key boundKey = GetBoundKey(keyMapping);
		int code = boundKey.getValue();

		if (code == -1) return false; // Skip unbound keys

		if (boundKey.getType() == InputConstants.Type.MOUSE) {
			long handle = ClientPlayerUtils.GetWindowHandle(window);
			return GLFW.glfwGetMouseButton(handle, code) == GLFW.GLFW_PRESS;
		} else {
			return KeyInputUtils.isKeyDown(window, code);
		}
	}
}
