package me.panda_studios.mcmod.core.gui;

import org.bukkit.inventory.Inventory;

public class WorldGui {
	public final Gui gui;
	public final Inventory inventory;

	public WorldGui(Gui gui, Inventory inventory) {
		this.gui = gui;
		this.inventory = inventory;
	}
}
