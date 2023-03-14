package me.panda_studios.mcmod.exemple.recipe;

import me.panda_studios.mcmod.core.recipe.CraftingRecipe;
import me.panda_studios.mcmod.exemple.setup.ItemSetup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UncraftingTableCraftingRecipe extends CraftingRecipe {

	public UncraftingTableCraftingRecipe() {
		super(2, 2,
				new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT),
				new ItemStack(Material.OAK_PLANKS), new ItemStack(Material.OAK_PLANKS));
	}

	@Override
	protected ItemStack output() {
		return ItemSetup.UNCRAFTING_TABLE.getItem();
	}
}
