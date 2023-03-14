package me.panda_studios.mcmod.core.register;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.features.ItemTag;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.recipe.Recipe;

public class Registries {
	public static final RegistryKey<IItem> ITEM = new RegistryKey<>();
	public static final RegistryKey<IBlock> BLOCK = new RegistryKey<>();
	public static final RegistryKey<ItemTag> ITEM_TAG = new RegistryKey<>();
	public static final RegistryKey<Gui> GUI = new RegistryKey<>();
	public static final RegistryKey<Recipe> RECIPE = new RegistryKey<>();
}
