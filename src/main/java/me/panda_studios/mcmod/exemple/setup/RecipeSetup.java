package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.recipe.CraftingRecipe;
import me.panda_studios.mcmod.core.recipe.Recipe;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;
import me.panda_studios.mcmod.exemple.recipe.UncraftingTableCraftingRecipe;

public class RecipeSetup {
	public static final Registry<Recipe> RECIPES = Registry.create(Mcmod.MODID, Registries.RECIPE);

	public static final Recipe UNCRAFTING_TABLE = RECIPES.register("uncrafting_table", new UncraftingTableCraftingRecipe());
}
