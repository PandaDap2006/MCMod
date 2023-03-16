package me.panda_studios.mcmod.exemple.gui;

import me.panda_studios.mcmod.core.gui.Button;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.gui.WorldGui;
import me.panda_studios.mcmod.core.utils.LocalType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ShapedRecipe;

public class RecipesGui extends Gui {
	public RecipesGui() {
		super("Recipe", 6, 20);

		new Button("next_page", "Next Page", 9*6-1).build(this);
		new Button("previous_page", "Previous Page", 9*5).build(this);
	}

	@Override
	public void onButtonClick(InventoryClickEvent event, Button button, WorldGui worldGui) {
		switch (button.name) {
			case "next_page" -> worldGui.switchPage(this.page+1);
			case "previous_page" -> worldGui.switchPage(this.page-1);
		}
	}

	@Override
	public void onGuiOpen(Player player, WorldGui worldGui) {
		Bukkit.recipeIterator().forEachRemaining((recipe -> {
			if (recipe instanceof ShapedRecipe shapedRecipe) {

			}
		}));
	}
}
