package me.panda_studios.mcmod.exemple.loottables;

import me.panda_studios.mcmod.core.features.Loottable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UncraftingTableLoot extends Loottable {
	public UncraftingTableLoot() {
		this.itemPools.add(new ItemPool(1, 4,
				new ItemEntry(new ItemStack(Material.OAK_PLANKS), 5),
				new ItemEntry(new ItemStack(Material.IRON_INGOT), 1)
				)
		);
	}
}
