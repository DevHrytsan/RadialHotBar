package github.devhrytsan.radialhotbar.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
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

        if (stack.contains(DataComponentTypes.TOOL)) {
            return 0;
        }
        if (stack.contains(DataComponentTypes.WEAPON)) {
            return 1;
        }
        if (stack.contains(DataComponentTypes.FOOD)) {
            return 2;
        }
        if (stack.contains(DataComponentTypes.POTION_CONTENTS)) {
            return 3;
        }
        if (item instanceof BlockItem) {
            return 4;
        }
        return 5;
    }

    public static boolean canBeEquipped(ItemStack stack) {
        if (stack.isEmpty()) return false;

        if (stack.contains(DataComponentTypes.EQUIPPABLE)) {
            return true;
        }
        return false;
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
