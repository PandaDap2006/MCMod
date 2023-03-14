package me.panda_studios.mcmod.core.recipe;

import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.inventory.ItemStack;

public abstract class Recipe extends Behavior {
	protected abstract ItemStack output();
}
