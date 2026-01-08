package github.devhrytsan.radialhotbar.menu;

import github.devhrytsan.radialhotbar.Constants;
import github.devhrytsan.radialhotbar.config.FileConfigHandler;
import github.devhrytsan.radialhotbar.utils.MathUtils;
import github.devhrytsan.radialhotbar.utils.MenuUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.ArrayList;

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

    public static final RadialMenuScreen INSTANCE = new RadialMenuScreen();
    public boolean active = false;

    public RadialMenuScreen() {
        super(Text.translatable("main.radialhotbar.title"));
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        boolean isEnabled = FileConfigHandler.CONFIG_INSTANCE.modEnabled;
        boolean hasScreen = client.currentScreen != null;
        boolean isPaused = !client.isPaused();

        if (isEnabled && hasScreen && isPaused) {
            prepareSlots(context, mouseX, mouseY, delta);
            //RenderBackgrounds(context, mouseX, mouseY, delta);
            renderItems(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public void removed() { // Cleanup logic that runs automatically when Radial Menu closes

        super.removed();
        active = false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    public void activate() {
        if (MinecraftClient.getInstance().currentScreen == null) {
            // So basically it works it only allows when the player is playing the game and not looking at another menu.
            active = true;
            MinecraftClient.getInstance().setScreen(INSTANCE);
        }
    }

    public void deactivate() {
        active = false;
        if (MinecraftClient.getInstance().currentScreen == INSTANCE) {
            MinecraftClient.getInstance().setScreen(null);
        }
    }

    public void selectItem(double mouseX, double mouseY, int button) {
        if (active) {
            int centerX = client.getWindow().getScaledWidth() / 2;
            int centerY = client.getWindow().getScaledHeight() / 2;

            float radius = GLOBAL_UI_SCALE_FACTOR * FileConfigHandler.CONFIG_INSTANCE.scaleFactor * BASE_ITEM_RADIUS;
            float minRadiusIgnore = radius * MIN_RADIUS_IGNORE_MOUSE_FACTOR;
            float maxRadiusIgnore = radius * MAX_RADIUS_IGNORE_MOUSE_FACTOR;

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
                    handleInventorySwap(realSlotIndex);
                }

            }

        }
    }

    private void prepareSlots(DrawContext context, int mouseX, int mouseY, float delta) {
        PlayerEntity player = this.client.player;

        slotsToDraw.clear();

        for (int i = 0; i < MAX_SLOTS_COUNT; i++) {
            if (!FileConfigHandler.CONFIG_INSTANCE.hideEmptySlots || !player.getInventory().getStack(i).isEmpty()) {
                slotsToDraw.add(i);
            }
        }

        if (FileConfigHandler.CONFIG_INSTANCE.useAutoSortSlots) {
            slotsToDraw.sort((slotIndexA, slotIndexB) -> {
                ItemStack stackA = player.getInventory().getStack(slotIndexA);
                ItemStack stackB = player.getInventory().getStack(slotIndexB);

                // Get category priority (lower number = appears first)
                int categoryA = MenuUtils.getItemCategoryOrder(stackA);
                int categoryB = MenuUtils.getItemCategoryOrder(stackB);

                if (categoryA != categoryB) {
                    return Integer.compare(categoryA, categoryB);
                }
                // If categories are the same, sort by item name to keep it
                return stackA.getName().getString().compareTo(stackB.getName().getString());
            });
        }

        totalItemsToDraw = slotsToDraw.size();
    }

    private void renderItems(DrawContext context, int mouseX, int mouseY, float delta) {

        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;

        PlayerEntity player = this.client.player;
        TextRenderer textRenderer = client.textRenderer;

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

            mouseIn = (MathUtils.betweenTwoValues(distanceFromCenter, minRadiusIgnore, maxRadiusIgnore)) ?
                    MathUtils.isAngleBetween(adjustedMouseAngle, checkStart, checkEnd) : false;

            float scale = mouseIn ? BASE_ITEM_SCALE_FACTOR * SELECTED_ITEM_SCALE_FACTOR : BASE_ITEM_SCALE_FACTOR * NOT_SELECTED_ITEM_SCALE_FACTOR;

            ItemStack stack = player.getInventory().getStack(realSlotIndex);

            if (mouseIn) {
                selectedStack = stack;
            }

            context.getMatrices().pushMatrix();

            context.getMatrices().translate(renderX + 8, renderY + 8);

            context.getMatrices().scale(scale, scale);
            context.getMatrices().translate(-8, -8);

            context.drawItem(stack, 0, 0);
            context.drawStackOverlay(textRenderer, stack, 0, 0);

            context.getMatrices().popMatrix();

        }

        //Render selected preview
        if (FileConfigHandler.CONFIG_INSTANCE.useCenterItemPreview) {
            renderCenterItem(context, selectedStack);
        }

    }

    private void renderCenterItem(DrawContext context, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            float centerScale = CENTER_ITEM_SCALE_FACTOR;
            float itemSize = centerScale * 16;
            float halfItemSize = itemSize * 0.5f;

            int centerX = client.getWindow().getScaledWidth() / 2;
            int centerY = client.getWindow().getScaledHeight() / 2;

            context.getMatrices().pushMatrix();

            context.getMatrices().translate(centerX, centerY);

            context.getMatrices().scale(centerScale, centerScale);

            context.getMatrices().translate(-8, -8);

            context.drawItem(itemStack, 0, 0);
            context.drawStackOverlay(client.textRenderer, itemStack, 0, 0);

            context.getMatrices().popMatrix();

            String itemName = itemStack.getName().getString();
            int textWidth = client.textRenderer.getWidth(itemName);

            context.drawText(
                    client.textRenderer,
                    itemName,
                    centerX - (textWidth / 2),
                    centerY + (int) halfItemSize + 5, // Half the item height, +5 for padding
                    0xFFFFFFFF,
                    true
            );
        }
    }

    private void handleMovement() {
        if (this.client == null || this.client.player == null) return;
        var clientWindow = client.getWindow();
        long windowHandle = clientWindow.getHandle();

        KeyBinding[] keysToKeep = new KeyBinding[]{
                this.client.options.forwardKey,
                this.client.options.backKey,
                this.client.options.leftKey,
                this.client.options.rightKey,
                this.client.options.jumpKey,
                this.client.options.sprintKey,
                this.client.options.sneakKey
        };

        for (KeyBinding key : keysToKeep) {
            InputUtil.Key boundKey = KeyBindingHelper.getBoundKeyOf(key);
            int code = boundKey.getCode();

            // Skip unbound keys
            if (code == -1) continue;

            boolean isDown = false;

            if (boundKey.getCategory() == InputUtil.Type.MOUSE) { // Similar logic from opening radial menu.
                isDown = GLFW.glfwGetMouseButton(windowHandle, code) == GLFW.GLFW_PRESS;
            } else {
                isDown = InputUtil.isKeyPressed(clientWindow, code);
            }

            key.setPressed(isDown);
        }

    }

    private void handleInventorySwap(int sourceSlot) {

        if (sourceSlot >= 0 && sourceSlot < 9) {

            if (FileConfigHandler.CONFIG_INSTANCE.useAutoEquipArmor) {

                ItemStack selectedStack = client.player.getInventory().getStack(sourceSlot);
                EquippableComponent equippable = selectedStack.get(DataComponentTypes.EQUIPPABLE);

                if (equippable != null) {
                    EquipmentSlot slotType = equippable.slot();
                    boolean isArmorSlot = slotType != null && slotType.isArmorSlot(); //|| slotType == EquipmentSlot.OFFHAND);
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

                        int armorSlotId = MenuUtils.getArmorSlot(slotType);

                        client.interactionManager.clickSlot(
                                client.player.playerScreenHandler.syncId,
                                validSlotId,
                                0,
                                SlotActionType.PICKUP,
                                client.player
                        );

                        client.interactionManager.clickSlot(
                                client.player.playerScreenHandler.syncId,
                                armorSlotId,
                                0,
                                SlotActionType.PICKUP,
                                client.player
                        );

                        client.interactionManager.clickSlot(
                                client.player.playerScreenHandler.syncId,
                                validSlotId,
                                0,
                                SlotActionType.PICKUP,
                                client.player
                        );

                        //TODO: Make that player can auto equip and select(with some states) in radial menu.
                    } else {
                        HandleInteraction(sourceSlot);
                    }
                } else {
                    HandleInteraction(sourceSlot);
                }
            } else {
                HandleInteraction(sourceSlot);
            }

        } else {
            // That logic is just placeholder
            int currentSlot = client.player.getInventory().getSelectedSlot();

            client.interactionManager.clickSlot(
                    client.player.playerScreenHandler.syncId,
                    sourceSlot,
                    currentSlot,
                    SlotActionType.SWAP,
                    client.player
            );
        }
    }

    private void HandleInteraction(int sourceSlot) {
        client.player.getInventory().setSelectedSlot(sourceSlot);
        client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(sourceSlot));
    }

}
