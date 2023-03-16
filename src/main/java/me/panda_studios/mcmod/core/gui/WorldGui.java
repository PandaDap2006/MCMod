package me.panda_studios.mcmod.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class WorldGui {
	public final Gui gui;
	public final List<Inventory> inventory;
	private final Player owner;


	public WorldGui(Gui gui, List<Inventory> inventory, Player owner) {
		this.gui = gui;
		this.inventory = inventory;
		this.owner = owner;
	}

	public void updateMenu() {
		for (Button button : this.gui.buttons.values()) {
			switch (button.localType) {
				case GLOBAL -> inventory.forEach((inv -> inv.setItem(button.slot, button.getItem())));
				case LOCAL -> inventory.get(button.page).setItem(button.slot, button.getItem());
			}
		}
	}

	public void switchPage(int page) {
		if (hasPage(page)) {
			this.gui.page = page;
			this.owner.openInventory(this.inventory.get(page));
		}
	}

	public boolean hasPage(int page) {
		return page >= 0 && page < this.gui.maxPage;
	}
}
