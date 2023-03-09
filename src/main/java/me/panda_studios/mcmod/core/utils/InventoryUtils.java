package me.panda_studios.mcmod.core.utils;

import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static boolean HasSpace(ItemStack[] inventoryContent) {
        for (ItemStack item: inventoryContent) {
            if (item == null || item.getType().isAir()) {
                return true;
            }
        }
        return false;
    }
}
