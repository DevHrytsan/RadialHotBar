package github.devhrytsan.radialhotbar.utils;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class MenuUtils {
    /**
     * Assigns a priority index based on requested order:
     * 0: Tools
     * 1: Weapons
     * 2: Food
     * 3: Blocks
     * 4: Potions
     * 5: Everything else
     * 6: Empty
     */
    public static int getItemCategoryOrder(ItemStack stack) {
        if (stack.isEmpty()) return 6;

        Item item = stack.getItem();

        if (item instanceof ToolItem || item instanceof MiningToolItem || item instanceof ShearsItem) {
            return 0;
        }
        if (item instanceof SwordItem || item instanceof RangedWeaponItem || item instanceof TridentItem) {
            return 1;
        }
        if (item.isFood()) {
            return 2;
        }
        if (item instanceof PotionItem || item instanceof GlassBottleItem) {
            return 3;
        }
        if (item instanceof BlockItem) {
            return 4;
        }
        return 5;
    }

    public static boolean canBeEquipped(ItemStack stack) {
        if (stack.isEmpty()) return false;

        Item item = stack.getItem();
        return item instanceof Equipment || item instanceof ElytraItem;
    }

    public static int getArmorSlot(EquipmentSlot slot) {
        int armorSlotId = -1;
        switch (slot) {
            case HEAD -> armorSlotId = 5;
            case CHEST -> armorSlotId = 6;
            case LEGS -> armorSlotId = 7;
            case FEET -> armorSlotId = 8;
            case OFFHAND -> armorSlotId = 45;
            default -> armorSlotId = -1;
        }
        return armorSlotId;
    }
}