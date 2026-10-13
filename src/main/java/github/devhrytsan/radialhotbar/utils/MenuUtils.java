package github.devhrytsan.radialhotbar.utils;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
//? }

public class MenuUtils {

	/**
	 * Assigns a priority index based on requested order:
	 * 0: Tools
	 * 1: Weapons
	 * 2: Food
	 * 3: Potions
	 * 4: Blocks
	 * 5: Everything else
	 * 6: Empty
	 */
	public static int getItemCategoryOrder(ItemStack stack) {
		if (stack.isEmpty()) return 6;

		var item = stack.getItem();

		// Now little hell begins

		//? if >=1.21.5 {
		// Modern logic using Data Components.
		// Every tool and sword has both TOOL and WEAPON, so weapons are checked first by tag/type.
		boolean isWeapon = stack.is(ItemTags.SWORDS)
				|| (stack.has(DataComponents.WEAPON) && !stack.has(DataComponents.TOOL)) // mace, spear
				|| item instanceof ProjectileWeaponItem
				|| item instanceof TridentItem;
		if (isWeapon) {
			return 1;
		}
		if (stack.has(DataComponents.TOOL)) {
			return 0;
		}
		if (stack.has(DataComponents.FOOD)) {
			return 2;
		}
		if (stack.has(DataComponents.POTION_CONTENTS)) {
			return 3;
		}

		//? } else {

		/*// Legacy logic for 1.21.4 and below...

		if (item instanceof DiggerItem || item instanceof ShearsItem) {
			return 0;
		}

		if (item instanceof SwordItem || item instanceof ProjectileWeaponItem || item instanceof TridentItem) {
			return 1;
		}

		//? if >=1.20.5 {

		if (stack.has(DataComponents.FOOD)) {
			return 2;
		}

		//? } else {

		/^if (item.isEdible()) {
			return 2;
		}

		^///? }

		if (item instanceof PotionItem || item instanceof BottleItem || item instanceof TippedArrowItem) {
			return 3;
		}

		*///? }

		// BlockItem check (works in all versions)
		if (item instanceof BlockItem) {
			return 4;
		}

		return 5;
	}

	public static boolean canBeEquipped(ItemStack stack) {
		if (stack.isEmpty()) return false;

		//? if >=1.21.5 {
		// Modern logic
		if (stack.has(DataComponents.EQUIPPABLE)) {
			return true;
		}
		return false;

		//? } else {

		/*// Legacy logic
		var item = stack.getItem();
		return item instanceof ArmorItem || item instanceof ElytraItem;

		*///? }

	}

	// Carved pumpkins and mob heads are wearable but are blocks the player wants to place,
	// and animal armor (BODY slot) has no slot in the player's inventory menu.
	public static boolean shouldAutoEquip(ItemStack stack, EquipmentSlot slot) {
		if (slot == null || !slot.isArmor()) return false;
		if (stack.getItem() instanceof BlockItem) return false;
		return getArmorSlot(slot) != -1;
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

	public static List<Component> getTooltipLines(ItemStack itemStack, Minecraft client){
		var player = client.player;

		//? if >=1.20.5 {
		return itemStack.getTooltipLines(Item.TooltipContext.of(client.level), player, TooltipFlag.NORMAL);
		//? } else {
		/*
        return itemStack.getTooltipLines(player, TooltipFlag.Default.NORMAL);
        */
		//? }
	}

}
