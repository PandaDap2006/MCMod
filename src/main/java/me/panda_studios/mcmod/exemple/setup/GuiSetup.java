package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;
import me.panda_studios.mcmod.exemple.gui.RecipesGui;
import me.panda_studios.mcmod.exemple.gui.UncraftingTableGui;

public class GuiSetup {
	public static final Registry<Gui> GUIS = Registry.create(Mcmod.MODID, Registries.GUI);

	public static final Gui RECIPES = GUIS.register("recipes", new RecipesGui());
	public static final Gui UNCRAFTING_TABLE_GUI = GUIS.register("uncrafting_table", new UncraftingTableGui());
}
