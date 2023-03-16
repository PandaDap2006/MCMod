package me.panda_studios.mcmod.core.gui;

import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.utils.Behavior;
import me.panda_studios.mcmod.core.utils.LocalType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Gui extends Behavior implements Cloneable {
	Map<Integer, SlotType> slotTypes = new HashMap<>();
	Map<Integer, Button> buttons = new HashMap<>();

	private final String title;
	protected final int pageSize;
	protected final int maxPage;
	protected int page = 0;

	public Gui(String title, int pageSize) {
		this(title, pageSize, 1);
	}

	public Gui(String title, int pageSize, int maxPage) {
		this.title = title;
		this.pageSize = 9*pageSize;
		this.maxPage = maxPage;
	}

	public void OpenMenu(Player player) {
		this.OpenMenu(player, 0);
	}

	public void OpenMenu(Player player, int page) {
		player.closeInventory();
		if (WorldRegistry.GUIS.containsKey(player.getUniqueId())) {
			WorldRegistry.GUIS.get(player.getUniqueId()).gui.onGuiClose(player, WorldRegistry.GUIS.get(player.getUniqueId()));
		}

		List<Inventory> inventory = new ArrayList<>();
		for (int i = 0; i < this.maxPage; i++) {
			String title = this.title;
			if (this.maxPage > 1)
				title += " - Page " + (i+1) + " - " + maxPage;
			inventory.add(Bukkit.createInventory(player, pageSize, title));
		}
		WorldRegistry.GUIS.put(player.getUniqueId(), new WorldGui(this.clone(), inventory, player));
		WorldGui worldGui = WorldRegistry.GUIS.get(player.getUniqueId());
		worldGui.gui.page = page;
		worldGui.updateMenu();
		worldGui.gui.onGuiOpen(player, worldGui);

		player.openInventory(inventory.get(page));
	}

	public void onButtonClick(InventoryClickEvent event, Button button, WorldGui worldGui) {}
	public void onSlotClick(InventoryClickEvent event, WorldGui worldGui) {}
	public void onGuiOpen(Player player, WorldGui worldGui) {}
	public void onGuiClose(Player player, WorldGui worldGui) {}

	protected void setSlotType(SlotType slotType, int... slot) {
		Arrays.stream(slot).forEach(i -> this.slotTypes.put(i-1, slotType));
	}

	protected ItemStack[] getContent(SlotType slotType, Inventory inventory) {
		List<ItemStack> list = new ArrayList<>();
		slotTypes.forEach((i, st) -> {
			if (slotType == null || st == slotType) {
				list.add(inventory.getItem(i));
			}
		});
		return list.toArray(new ItemStack[0]);
	}

	protected void setContent(SlotType slotType, Inventory inventory, int slot, ItemStack itemStack) {
		List<Integer> keys = new ArrayList<>();
		slotTypes.forEach((i, st) -> {
			if (slotType == null || st == slotType) {
				keys.add(i);
			}
		});
		inventory.setItem(keys.get(slot), itemStack);
	}

	@Override
	public Gui clone() {
		try {
			Gui clone = (Gui) super.clone();
			// TODO: copy mutable state here, so the clone can't change the internals of the original
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	protected enum SlotType {
		INPUT,
		OUTPUT
	}
}
