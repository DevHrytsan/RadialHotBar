package github.devhrytsan.radialhotbar.utils;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

//? if >= 26.1 {
/*import net.minecraft.world.inventory.ContainerInput;
*///? } else {
import net.minecraft.world.inventory.ClickType;
 //? }

public class ClientPlayerUtils {

	public static long GetWindowHandle(Window clientWindow) {
		//? if >=1.21.5 {
		return clientWindow.handle();
		//? } else {

		/*return clientWindow.getWindow();

		 *///? }
	}

	public static int getPlayerSelectedSlot(LocalPlayer player) {
		//? if >=1.21.5 {
		return player.getInventory().getSelectedSlot();
		//? } else {

		/*return player.getInventory().selected;

		 *///? }
	}

	public static void setPlayerSelectedSlot(LocalPlayer player, int slot) {

		//? if >=1.21.5 {
		player.getInventory().setSelectedSlot(slot);
		//? } else {

		/*player.getInventory().selected = slot;

		 *///? }
	}

	public static void handleMouseClickPickup(Minecraft client, int slotId) {
		if (client.gameMode == null || client.player == null) return;

        //? if >= 26.1 {
		/*client.gameMode.handleContainerInput(
				client.player.inventoryMenu.containerId,
				slotId,
				0,
				ContainerInput.PICKUP,
				client.player
		);
        *///? } else {
		client.gameMode.handleInventoryMouseClick(
				client.player.inventoryMenu.containerId,
				slotId,
				0,
				ClickType.PICKUP,
				client.player
		);
		 //? }
	}

	public static void handleMouseClickSwap(Minecraft client, int fromSlot, int toSlot) {
		if (client.gameMode == null || client.player == null) return;
		//? if >= 26.1 {
		/*client.gameMode.handleContainerInput(
				client.player.inventoryMenu.containerId,
				toSlot,
				fromSlot,
				ContainerInput.SWAP,
				client.player
		);
		*///? } else {
		client.gameMode.handleInventoryMouseClick(
				client.player.inventoryMenu.containerId,
				toSlot,
				fromSlot,
				ClickType.SWAP,
				client.player
		);
		 //? }
	}

	public static float getScaledMouseX(Minecraft client) {
		return (float) client.mouseHandler.xpos() * (float) client.getWindow().getGuiScaledWidth() / (float) client.getWindow().getScreenWidth();
	}

	public static float getScaledMouseY(Minecraft client) {
		return (float) client.mouseHandler.ypos() * (float) client.getWindow().getGuiScaledHeight() / (float) client.getWindow().getScreenHeight();
	}

}
