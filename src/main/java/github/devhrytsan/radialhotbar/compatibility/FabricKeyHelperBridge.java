package github.devhrytsan.radialhotbar.compatibility;

//? fabric {
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

//? if >=26.1 {
/*
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
*///? } else {
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//? }

public class FabricKeyHelperBridge { // Backward compatibility is pain
	//? if >=26.1 {
	/*
	public static KeyMapping register(KeyMapping keyMapping) {
		return KeyMappingHelper.registerKeyMapping(keyMapping);
	}
	*///? } else {
    public static KeyMapping register(KeyMapping keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }
	//? }

	//? if >=26.1 {
	/*
	public static InputConstants.Key getBoundKeyOf(KeyMapping keyMapping) {
		return KeyMappingHelper.getBoundKeyOf(keyMapping);
	}
	*///? } else {
    public static InputConstants.Key getBoundKeyOf(KeyMapping keyBinding) {
        return KeyBindingHelper.getBoundKeyOf(keyBinding);
    }
	//? }
}
//?}
