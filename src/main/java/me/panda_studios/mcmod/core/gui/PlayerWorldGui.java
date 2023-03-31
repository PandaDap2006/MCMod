package me.panda_studios.mcmod.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class PlayerWorldGui extends WorldGui {
	public final Player owner;

	public PlayerWorldGui(Gui gui, Player owner) {
		super(gui);
		this.owner = owner;
	}
}
