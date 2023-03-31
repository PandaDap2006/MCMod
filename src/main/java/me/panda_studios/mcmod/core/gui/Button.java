package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.utils.LocalType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Button extends GuiSlot<Button> {
	private final String label;
	private final List<String> lore = new ArrayList<>();
	private final ItemStack[] models = new ItemStack[] {null, null};

	public Button(String label, int slot) {
		super(slot);
		this.label = label;
		this.setDesign(true, 30001);
		this.setDesign(false, 30001);
	}

	public Button setDesign(boolean enabled, int modelData) {
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setCustomModelData(modelData);
		itemStack.setItemMeta(meta);
		this.setDesign(enabled, itemStack);
		return this;
	}

	public Button setDesign(boolean enabled, ItemStack itemStack) {
		if (enabled) {
			this.models[0] = itemStack;
		} else {
			this.models[1] = itemStack;
		}
		return this;
	}

	public Button setLore(String... text) {
		for (String s : text) {
			this.lore.add(ChatColor.GRAY + s);
		}
		return this;
	}

	public ItemStack getItem() {
		Player player = this.parentGui.worldGui instanceof PlayerWorldGui playerWorldGui ? playerWorldGui.owner : null;
		ItemStack itemStack = this.models[this.condition.isActive(this) ? 0 : 1];
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName((this.condition.isActive(this) ? "§f" : "§7") + Mcmod.makePlaceholder(player, label));
		meta.setLore(Mcmod.makePlaceholder(player, this.lore.toArray(new String[0])));
		itemStack.setItemMeta(meta);
		return itemStack;
	}
}
