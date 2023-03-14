package me.panda_studios.mcmod.exemple.gui;

import me.panda_studios.mcmod.core.gui.Button;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.gui.WorldGui;
import me.panda_studios.mcmod.core.utils.LocalType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RecipesGui extends Gui {
	public RecipesGui() {
		super("Recipe", 6, 2);

		setButton("next_page", 9*6, "Next Page");
		setButton("previous_page", (9*5)+1, "Previous Page");
	}

	@Override
	public void onButtonClick(InventoryClickEvent event, Button button, WorldGui worldGui) {
		switch (button.name) {
			case "next_page" -> this.OpenMenu((Player) event.getWhoClicked(), ++this.page);
			case "previous_page" -> this.OpenMenu((Player) event.getWhoClicked(), --this.page);
		}
	}

	@Override
	public Button.ButtonState getButtonState(Button button, WorldGui worldGui) {
		switch (button.name) {
			case "next_page" -> {
				return this.hasPage(this.page+1) ? Button.ButtonState.ENABLED : Button.ButtonState.DISABLED;
			}
			case "previous_page" -> {
				return this.hasPage(this.page-1) ? Button.ButtonState.ENABLED : Button.ButtonState.DISABLED;
			}
		}

		return super.getButtonState(button, worldGui);
	}
}
