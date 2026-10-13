package github.devhrytsan.radialhotbar.compatibility;

import github.devhrytsan.radialhotbar.RadialHotBarMod;
import github.devhrytsan.radialhotbar.config.FileConfigHandler;
import net.minecraft.client.player.LocalPlayer;

// Compatibility with the "Tenth Slot" mod (Fabric only): https://modrinth.com/mod/tenth-slot
public class TenthSlotCompat {

	public static final String MOD_ID = "tenthslot";
	public static final int TENTH_SLOT_INDEX = 9;

	private static Boolean loaded;

	public static boolean isLoaded() {
		if (loaded == null) {
			loaded = RadialHotBarMod.PLATFORM.isModLoaded(MOD_ID);
		}
		return loaded;
	}

	// Slot 9 can be the selected slot whenever Tenth Slot is installed, even if the menu doesn't show it.
	public static boolean isTenthSlot(int slot) {
		return slot == TENTH_SLOT_INDEX && isLoaded();
	}

	public static boolean isShownInMenu() {
		return isLoaded() && FileConfigHandler.CONFIG_INSTANCE.useTenthSlotCompat;
	}

	public static void select(LocalPlayer player) {
		//? fabric {
		player.getInventory().selected = TENTH_SLOT_INDEX;
		//?}
	}
}
