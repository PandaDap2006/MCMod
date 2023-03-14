package me.panda_studios.mcmod.core.utils;

import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	public static boolean hasSpace(ItemStack[] matrix, ItemStack itemStack) {
		if (itemStack != null) {
			int amount = itemStack.getAmount();
			for (int i = 0; i < 2; i++) {
				switch (i) {
					case 0 -> {
						for (ItemStack stack : matrix) {
							if (stack != null) {
								if (stack.getAmount() < stack.getMaxStackSize()) {
									amount += stack.getAmount() - stack.getMaxStackSize();
								}
							}
						}
					}
					case 1 -> {
						for (ItemStack stack : matrix) {
							if (stack == null) {
								amount -= itemStack.getMaxStackSize();
							}
						}
					}
				}
			}

			return amount >= 0;
		}
		return false;
	}
}
