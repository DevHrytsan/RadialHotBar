package github.devhrytsan.radialhotbar.menu;

import github.devhrytsan.radialhotbar.utils.KeyInputUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

//? neoforge {
/*import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
*///?} forge {
/*import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
*///?}

public class RadialMenuMovementController {
	private final Minecraft client = Minecraft.getInstance();

	private KeyMapping[] keysToKeep = new KeyMapping[]{
			this.client.options.keyUp,
			this.client.options.keyDown,
			this.client.options.keyLeft,
			this.client.options.keyRight,
			this.client.options.keyJump,
			this.client.options.keySprint,
			this.client.options.keyShift
	};

	public static final RadialMenuMovementController INSTANCE = new RadialMenuMovementController();

	public void handleMovement() {

		if (this.client == null || this.client.player == null) return;
		var clientWindow = client.getWindow();

		for (KeyMapping key : keysToKeep) {
			boolean isDown = KeyInputUtils.isHardwareKeyPressed(key, clientWindow);
			key.setDown(isDown);
		}

	}

	//? if neoforge || forge {
	/*// Contexts the movement keys had before the menu overrode them, or null when not overridden
	private IKeyConflictContext[] savedKeyContexts;

	// Called every tick: switches movement keys to UNIVERSAL while the menu is open, then restores the originals
	public void updateMovementKeyContextNeoForge(boolean allowInScreen) {
		boolean isOverridden = savedKeyContexts != null;
		if (allowInScreen == isOverridden) return;

		if (allowInScreen) {
			savedKeyContexts = new IKeyConflictContext[keysToKeep.length];
			for (int i = 0; i < keysToKeep.length; i++) {
				savedKeyContexts[i] = keysToKeep[i].getKeyConflictContext();
				keysToKeep[i].setKeyConflictContext(KeyConflictContext.UNIVERSAL);
			}
		} else {
			for (int i = 0; i < keysToKeep.length; i++) {
				keysToKeep[i].setKeyConflictContext(savedKeyContexts[i]);
			}
			savedKeyContexts = null;
		}
	}
    *///?}

}
