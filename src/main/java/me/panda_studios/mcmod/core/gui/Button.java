package me.panda_studios.mcmod.core.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Button {
	public final int slot;
	public final String name;
	private final String label;
	private final int[] modelData;

	ButtonState buttonState;

	protected Button(int slot, String name, String label, int modelDataEnabled, int modelDataDisabled) {
		this.slot = slot;
		this.name = name;
		this.label = label;
		this.modelData = new int[] {modelDataEnabled, modelDataDisabled};
	}

	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta meta = itemStack.getItemMeta();

		meta.setDisplayName((this.buttonState == ButtonState.ENABLED ? "§f" : "§7") + label);
		meta.setCustomModelData(buttonState == ButtonState.ENABLED ? modelData[0] : modelData[1]);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public enum ButtonState {
		ENABLED,
		DISABLED
	}
}
