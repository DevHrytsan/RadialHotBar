package github.devhrytsan.radialhotbar.menu;

import github.devhrytsan.radialhotbar.utils.KeyInputUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

//? neoforge {
/*import net.neoforged.neoforge.client.settings.KeyConflictContext;
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

	//? neoforge {
	/*public void updateMovementKeyContextNeoForge(boolean allowInScreen) {
		if (this.client == null || this.client.player == null) return;

		KeyConflictContext context = allowInScreen
				? KeyConflictContext.UNIVERSAL
				: KeyConflictContext.IN_GAME;

		for (KeyMapping key : keysToKeep) {
			key.setKeyConflictContext(context);
		}
	}
    *///?}

}
