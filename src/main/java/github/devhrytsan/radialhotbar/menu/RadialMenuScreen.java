package github.devhrytsan.radialhotbar.menu;

import com.mojang.blaze3d.platform.InputConstants;
import github.devhrytsan.radialhotbar.config.FileConfigHandler;
import github.devhrytsan.radialhotbar.utils.ClientPlayerUtils;
import github.devhrytsan.radialhotbar.utils.GuiGraphicsUtils;
import github.devhrytsan.radialhotbar.utils.KeyInputUtils;
import github.devhrytsan.radialhotbar.utils.MathUtils;
import github.devhrytsan.radialhotbar.utils.MenuUtils;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
//? }

//? if <1.21.1 {

/*import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

*///? }

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"ConstantConditions", "DataFlowIssue", "FieldMayBeFinal", "unused"})
public class RadialMenuScreen extends Screen {

	protected static final float GLOBAL_UI_SCALE_FACTOR = 1f;
	protected static final float BASE_ITEM_RADIUS = 35f;
	protected static final float BASE_ITEM_SCALE_FACTOR = 1.5f;
	protected static final float CENTER_ITEM_SCALE_FACTOR = 3f;

	protected static final float NOT_SELECTED_ITEM_SCALE_FACTOR = 1f;
	protected static final float SELECTED_ITEM_SCALE_FACTOR = 1.5f;

	protected static final float MIN_RADIUS_IGNORE_MOUSE_FACTOR = 0.2f;
	protected static final float MAX_RADIUS_IGNORE_MOUSE_FACTOR = 6f;

	protected static final int MAX_SLOTS_COUNT = 9;

	private List<Integer> slotsToDraw;
	private int totalItemsToDraw = 0;

	private int lastUsedSlot = 0;
	private int initialSlot = 0;
	private boolean itemSelected = false;

	private final Minecraft client = Minecraft.getInstance();

	// Maybe should move it to some Service Locator rather than Singleton.
	// But for a project of that size, it’s not really necessary.
	public static final RadialMenuScreen INSTANCE = new RadialMenuScreen();

	public boolean active = false;

	public RadialMenuScreen() {
		super(Component.translatable("main.radialhotbar.title"));
		slotsToDraw = new ArrayList<>(MAX_SLOTS_COUNT);
	}

	@Override
	public void init() {

	}

	@Override
	public void tick() {
		super.tick();

		if (FileConfigHandler.CONFIG_INSTANCE.allowMovementWhileOpen) {
			handleMovement();
		}
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		//boolean isEnabled = FileConfigHandler.CONFIG_INSTANCE.modEnabled;
		boolean hasScreen = client.screen != null;
		boolean isPaused = !client.isPaused();

		if (hasScreen && isPaused) {
			super.render(context, mouseX, mouseY, delta);

			prepareSlots(context, mouseX, mouseY, delta);
			renderBackgrounds(context, mouseX, mouseY, delta);
			renderItems(context, mouseX, mouseY, delta);
		}
	}

	@Override
	public void removed() { // Cleanup logic that runs automatically when Radial Menu closes

		super.removed();
		active = false;
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	public void activate() {

		if (client.screen == null) {
			if (client != null && client.player != null) {
				int currentSlot = ClientPlayerUtils.getPlayerSelectedSlot(client.player);

				// If the player moved to a new slot (via scroll wheel or numbers)
				// since the last time they used the menu, update our 'Alt' slot
				// to be the one they just left.
				if (currentSlot != initialSlot) {
					lastUsedSlot = initialSlot;
				}

				this.initialSlot = currentSlot;
			}

			this.itemSelected = false;
			// So basically it works it only allows when the player is playing the game and not looking at another menu.
			active = true;
			client.setScreen(INSTANCE);
		}
	}

	public void deactivate(double mouseX, double mouseY) {
		active = false;

		if (client != null && client.player != null) {

			if (FileConfigHandler.CONFIG_INSTANCE.useSwapToRecentOnNoSelect) {

				int centerX = client.getWindow().getGuiScaledWidth() / 2;
				int centerY = client.getWindow().getGuiScaledHeight() / 2;
				double distanceFromCenter = MathUtils.calculateDistanceBetweenPoints(centerX, centerY, mouseX, mouseY);

				float radius = GLOBAL_UI_SCALE_FACTOR * FileConfigHandler.CONFIG_INSTANCE.scaleFactor * BASE_ITEM_RADIUS;
				float minRadiusIgnore = radius * MIN_RADIUS_IGNORE_MOUSE_FACTOR;

				boolean isInsideMinRadius = MathUtils.betweenTwoValues(distanceFromCenter, 0, minRadiusIgnore);

				if (!itemSelected && isInsideMinRadius) {
					int currentSlot = ClientPlayerUtils.getPlayerSelectedSlot(client.player);

					int target = lastUsedSlot;
					lastUsedSlot = currentSlot;
					initialSlot = target;

					handleInteraction(target);
				}
			}
		}
		if (client.screen == INSTANCE) {
			client.setScreen(null);
		}
	}

	public void selectItem(double mouseX, double mouseY, int button) {
		if (client == null || client.player == null) return;

		int centerX = client.getWindow().getGuiScaledWidth() / 2;
		int centerY = client.getWindow().getGuiScaledHeight() / 2;

		float radius = GLOBAL_UI_SCALE_FACTOR * FileConfigHandler.CONFIG_INSTANCE.scaleFactor * BASE_ITEM_RADIUS;
		float minRadiusIgnore = radius * MIN_RADIUS_IGNORE_MOUSE_FACTOR;
		float maxRadiusIgnore = radius * MAX_RADIUS_IGNORE_MOUSE_FACTOR;

		if (totalItemsToDraw == 0 || slotsToDraw.isEmpty()) {
			return;
		}

		double anglePerItem = 360.0 / totalItemsToDraw;
		double halfAnglePerItem = anglePerItem * 0.5;

		for (int i = 0; i < totalItemsToDraw; i++) {

			double angleDeg = anglePerItem * i - 90.0;

			int realSlotIndex = slotsToDraw.get(i);

			double checkStart = MathUtils.normalizeAngle(angleDeg - halfAnglePerItem);
			double checkEnd = MathUtils.normalizeAngle(checkStart + anglePerItem);
			double adjustedMouseAngle = MathUtils.RelativeAngle(centerX, centerY, mouseX, mouseY);

			double distanceFromCenter = MathUtils.calculateDistanceBetweenPoints(centerX, centerY, mouseX, mouseY);

			boolean mouseIn = (MathUtils.betweenTwoValues(distanceFromCenter, minRadiusIgnore, maxRadiusIgnore)) ?

					MathUtils.isAngleBetween(adjustedMouseAngle, checkStart, checkEnd) : false;

			if (mouseIn) {
				this.itemSelected = true;
				handleInventorySwap(realSlotIndex);
				this.lastUsedSlot = initialSlot;
				this.initialSlot = realSlotIndex;
				break;
			}

		}
	}

	private void prepareSlots(GuiGraphics context, int mouseX, int mouseY, float delta) {
		Player player = this.client.player;
		Inventory inventory = player.getInventory();

		slotsToDraw.clear();

		for (int i = 0; i < MAX_SLOTS_COUNT; i++) {
			if (!FileConfigHandler.CONFIG_INSTANCE.hideEmptySlots || !inventory.getItem(i).isEmpty()) {
				slotsToDraw.add(i);
			}
		}

		if (FileConfigHandler.CONFIG_INSTANCE.useAutoSortSlots) {
			slotsToDraw.sort((slotIndexA, slotIndexB) -> {
				ItemStack stackA = inventory.getItem(slotIndexA);
				ItemStack stackB = inventory.getItem(slotIndexB);

				// Get category priority (lower number = appears first)
				int categoryA = MenuUtils.getItemCategoryOrder(stackA);
				int categoryB = MenuUtils.getItemCategoryOrder(stackB);

				if (categoryA != categoryB) {
					return Integer.compare(categoryA, categoryB);
				}
				// If categories are the same, sort by item name to keep it
				return stackA.getHoverName().getString().compareTo(stackB.getHoverName().getString());
			});
		}

		totalItemsToDraw = slotsToDraw.size();
	}

	private void renderBackgrounds(GuiGraphics context, int mouseX, int mouseY, float delta) {
		//? if <1.21.1 {
			

			/*int color = 0x80000000;

			GuiGraphicsUtils.PushMatrix(context);
	        context.fill(0, 0, width, height, color);
			GuiGraphicsUtils.PopMatrix(context);
			
		*///? }
	}
	private void renderItems(GuiGraphics context, int mouseX, int mouseY, float delta) {

		var clientWindow = client.getWindow();
		Player player = this.client.player;
		Inventory inventory = player.getInventory();
		var textRenderer = client.font;

		int centerX = clientWindow.getGuiScaledWidth() / 2;
		int centerY = clientWindow.getGuiScaledHeight() / 2;

		float radius = GLOBAL_UI_SCALE_FACTOR * FileConfigHandler.CONFIG_INSTANCE.scaleFactor * BASE_ITEM_RADIUS;
		float minRadiusIgnore = radius * MIN_RADIUS_IGNORE_MOUSE_FACTOR;
		float maxRadiusIgnore = radius * MAX_RADIUS_IGNORE_MOUSE_FACTOR;

		double anglePerItem = 360.0 / totalItemsToDraw;
		double halfAnglePerItem = anglePerItem * 0.5;

		ItemStack selectedStack = ItemStack.EMPTY; //Maybe I should do with ID

		for (int i = 0; i < totalItemsToDraw; i++) {

			//double angleRad = (2 * Math.PI / totalItemsToDraw) * i - (Math.PI / 2);// Radians
			//double angleDeg = anglePerItem * i - 90.0; // Degrees

			double angleDeg = anglePerItem * i - 90.0;
			double angleRad = Math.toRadians(angleDeg);

			int realSlotIndex = slotsToDraw.get(i);

			int x = (int) (centerX + radius * Math.cos(angleRad));
			int y = (int) (centerY + radius * Math.sin(angleRad));

			int renderX = x - 8;
			int renderY = y - 8;

			double checkStart = MathUtils.normalizeAngle(angleDeg - halfAnglePerItem);
			double checkEnd = MathUtils.normalizeAngle(checkStart + anglePerItem);
			boolean mouseIn;

			double adjustedMouseAngle = MathUtils.RelativeAngle(centerX, centerY, mouseX, mouseY);

			double distanceFromCenter = MathUtils.calculateDistanceBetweenPoints(centerX, centerY, mouseX, mouseY);

			mouseIn = MathUtils.betweenTwoValues(distanceFromCenter, minRadiusIgnore, maxRadiusIgnore) ?
					MathUtils.isAngleBetween(adjustedMouseAngle, checkStart, checkEnd) : false;

			float scale = mouseIn ? BASE_ITEM_SCALE_FACTOR * SELECTED_ITEM_SCALE_FACTOR : BASE_ITEM_SCALE_FACTOR * NOT_SELECTED_ITEM_SCALE_FACTOR;

			ItemStack stack = inventory.getItem(realSlotIndex);

			if (mouseIn) {
				selectedStack = stack;
			}

			GuiGraphicsUtils.PushMatrix(context);

			GuiGraphicsUtils.TranslateMatrix(context, renderX + 8, renderY + 8, 0);

			GuiGraphicsUtils.ScaleMatrix(context, scale, scale, 1);
			GuiGraphicsUtils.TranslateMatrix(context, -8, -8, 0);

			GuiGraphicsUtils.RenderItem(context, stack, 0, 0);
			GuiGraphicsUtils.RenderItemDecoration(context, textRenderer, stack, 0, 0);

			GuiGraphicsUtils.PopMatrix(context);

			//Render selected preview
			if (FileConfigHandler.CONFIG_INSTANCE.useCenterItemPreview) {
				renderCenterItem(context, selectedStack);
			}
		}

	}

	private void renderCenterItem(GuiGraphics context, ItemStack itemStack) {
		if (!itemStack.isEmpty()) {

			var clientWindow = client.getWindow();
			Player player = this.client.player;
			Inventory inventory = player.getInventory();
			var textRenderer = client.font;

			float centerScale = CENTER_ITEM_SCALE_FACTOR;
			float itemSize = centerScale * 16;
			float halfItemSize = itemSize * 0.5f;

			int centerX = client.getWindow().getGuiScaledWidth() / 2;
			int centerY = client.getWindow().getGuiScaledHeight() / 2;

			GuiGraphicsUtils.PushMatrix(context);

			GuiGraphicsUtils.TranslateMatrix(context, centerX, centerY, 0);

			GuiGraphicsUtils.ScaleMatrix(context, centerScale, centerScale, 1);

			GuiGraphicsUtils.TranslateMatrix(context, -8, -8, 0);

			GuiGraphicsUtils.RenderItem(context, itemStack, 0, 0);
			GuiGraphicsUtils.RenderItemDecoration(context, textRenderer, itemStack, 0, 0);

			GuiGraphicsUtils.PopMatrix(context);

			String itemName = itemStack.getHoverName().getString();
			int textWidth = textRenderer.width(itemName);

			GuiGraphicsUtils.DrawString(context,
					textRenderer,
					itemName,
					centerX - (textWidth / 2),
					centerY + (int) halfItemSize + 5,
					0xFFFFFFFF,
					true
			);
		}
	}

	private void handleInteraction(int sourceSlot) {

		ClientPlayerUtils.setPlayerSelectedSlot(client.player, sourceSlot);

		if (client.getConnection() != null) {
			client.getConnection().send(new ServerboundSetCarriedItemPacket(sourceSlot));
		}
	}

	private void handleInventorySwap(int sourceSlot) {

		if (sourceSlot >= 0 && sourceSlot < 9) {

			if (FileConfigHandler.CONFIG_INSTANCE.useAutoEquipArmor) {

				ItemStack selectedStack = client.player.getInventory().getItem(sourceSlot);

				EquipmentSlot targetSlotType = null;

				//? if >=1.21.5 {
				// Modern just using Data
				var equippable = selectedStack.get(DataComponents.EQUIPPABLE);
				if (equippable != null) {
					targetSlotType = equippable.slot();
				}
				//? } else {

				/*// Legacy: Asking the entity where this item belongs
				targetSlotType = client.player.getEquipmentSlotForItem(selectedStack);

				*///? }


				if (targetSlotType != null) {

					boolean isArmorSlot = targetSlotType != null && targetSlotType.isArmor(); //|| slotType == EquipmentSlot.OFFHAND);
					// EquipmentSlot.OFFHAND is used to be for check shields and similar items.

					if (isArmorSlot) {
						int validSlotId = sourceSlot;

						if (sourceSlot >= 0 && sourceSlot <= 8) {
							validSlotId = sourceSlot + 36;

							// If it's in main inventory (9-35) mapped ID is generally same (9-35) in PlayerScreenHandler
							// BUT if sourceSlot logic includes the hotbar or treats inv rows separately.
							// In Standard Player Inventory: main is 9-35, hotbar is 0-8.
							// In ScreenHandler: main is 9-35, hotbar is 36-44.
						}

						int armorSlotId = MenuUtils.getArmorSlot(targetSlotType);

						ClientPlayerUtils.handleMouseClickPickup(client, validSlotId);
						ClientPlayerUtils.handleMouseClickPickup(client, armorSlotId);
						ClientPlayerUtils.handleMouseClickPickup(client, validSlotId);

						//TODO: Make that player can auto equip and select(with some states) in radial menu.
					} else {
						handleInteraction(sourceSlot);
					}
				} else {
					handleInteraction(sourceSlot);
				}
			} else {
				handleInteraction(sourceSlot);
			}

		} else {
			// That logic is just placeholder
			int currentSlot = ClientPlayerUtils.getPlayerSelectedSlot(client.player);

			ClientPlayerUtils.handleMouseClickSwap(client, currentSlot, sourceSlot);
		}
	}

	private void handleMovement() {
		if (this.client == null || this.client.player == null) return;
		var clientWindow = client.getWindow();
		long windowHandle = ClientPlayerUtils.GetWindowHandle(clientWindow);

		KeyMapping[] keysToKeep = new KeyMapping[]{
				this.client.options.keyUp,
				this.client.options.keyDown,
				this.client.options.keyLeft,
				this.client.options.keyRight,
				this.client.options.keyJump,
				this.client.options.keySprint,
				this.client.options.keyShift
		};

		for (KeyMapping key : keysToKeep) {
			InputConstants.Key boundKey = KeyInputUtils.GetBoundKey(key);
			int code = boundKey.getValue();

			// Skip unbound keys
			if (code == -1) continue;

			boolean isDown = false;

			if (boundKey.getType() == InputConstants.Type.MOUSE) { // Similar logic from opening radial menu.
				isDown = GLFW.glfwGetMouseButton(windowHandle, code) == GLFW.GLFW_PRESS;
			} else {
				isDown = KeyInputUtils.isKeyDown(clientWindow, code);
			}

			key.setDown(isDown);
		}

	}

}
