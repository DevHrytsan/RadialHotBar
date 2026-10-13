package github.devhrytsan.radialhotbar.utils;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.KeyMapping;

//? if >=26.3 {
/*import org.lwjgl.sdl.SDLMouse;
import org.lwjgl.sdl.SDLScancode;
*///? } else {
import org.lwjgl.glfw.GLFW;
//? }

//? fabric {
import github.devhrytsan.radialhotbar.compatibility.FabricKeyHelperBridge;
//?}

public class KeyInputUtils {

	//? if >=26.3 {
	/*public static final InputConstants.Type KEYBOARD_TYPE = InputConstants.Type.KEYBOARD;
	public static final int DEFAULT_OPEN_MENU_KEY = SDLScancode.SDL_SCANCODE_R;
	*///? } else {
	public static final InputConstants.Type KEYBOARD_TYPE = InputConstants.Type.KEYSYM;
	public static final int DEFAULT_OPEN_MENU_KEY = GLFW.GLFW_KEY_R;
	//? }

	public static InputConstants.Key GetBoundKey(KeyMapping keyBinding) {
		//? fabric {
		return FabricKeyHelperBridge.getBoundKeyOf(keyBinding);
		//?} neoforge {
		/*return keyBinding.getKey(); // it may be wrong
		*///?} forge {

		/*return keyBinding.getKey();

		*///?}

	}

	public static boolean isKeyDown(Window window, int keyCode){
		//? if >=26.3 {
		/*return InputConstants.isKeyDown(keyCode);
		*///? } elif >=1.21.5 {
		return InputConstants.isKeyDown(window, keyCode);
		//? } else {

		/*long handle = ClientPlayerUtils.GetWindowHandle(window);
		return InputConstants.isKeyDown(handle, keyCode);

		*///? }
	}

	public static boolean isMouseButtonDown(Window window, int button) {
		//? if >=26.3 {
		/*// SDL mouse buttons are 1-based, SDL_BUTTON_MASK(b) = 1 << (b - 1)
		return (SDLMouse.SDL_GetMouseState(null, null) & (1 << (button - 1))) != 0;
		*///? } else {
		long handle = ClientPlayerUtils.GetWindowHandle(window);
		return GLFW.glfwGetMouseButton(handle, button) == GLFW.GLFW_PRESS;
		//? }
	}

	public static boolean isHardwareKeyPressed(KeyMapping keyMapping, Window window) {
		InputConstants.Key boundKey = GetBoundKey(keyMapping);
		int code = boundKey.getValue();

		if (code == -1) return false; // Skip unbound keys

		boolean isBaseKeyDown = false;

		if (boundKey.getType() == InputConstants.Type.MOUSE) {
			isBaseKeyDown = isMouseButtonDown(window, code);
		} else {
			isBaseKeyDown = KeyInputUtils.isKeyDown(window, code);
		}

		//? fabric {
		return isBaseKeyDown;
		//?} else {
		/*
		if (!isBaseKeyDown) return false;
		return keyMapping.getKeyModifier().isActive(null);
		*/
		//?}
	}
}
