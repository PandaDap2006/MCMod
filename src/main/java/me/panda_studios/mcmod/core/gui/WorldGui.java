package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WorldGui extends Behavior {
	public final Gui gui;
	public final List<Inventory> inventory;

	public WorldGui(Gui gui) {
		this.gui = gui.clone();
		this.gui.worldGui = this;
		List<Inventory> inventories = new ArrayList<>();
		for (int i = 0; i < gui.maxPage; i++) {
			String title = gui.title;
			if (gui.maxPage > 1)
				title += " - Page " + (i+1) + " - " + gui.maxPage;
			inventories.add(Bukkit.createInventory(null, gui.pageSize,
					(gui.customGuiUnicode() != null ? "§fऐ" + gui.customGuiUnicode() + "§rऑ" : "") + title));
		}
		this.inventory = inventories;
		this.reloadSlots();
		this.gui.registerButtons();
		new BukkitRunnable() {
			@Override
			public void run() {
				gui.onTick(WorldGui.this);
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	public void open(Player player) {
		this.open(player, 0);
	}

	public void open(Player player, int page) {
		player.closeInventory();
		if (WorldRegistry.GUIS.containsKey(player.getUniqueId())) {
			WorldRegistry.GUIS.get(player.getUniqueId()).gui.onGuiClose(player, WorldRegistry.GUIS.get(player.getUniqueId()));
		}

		WorldRegistry.GUIS.put(player.getUniqueId(), this);
		this.gui.page = page;
		this.reloadSlots();
		this.gui.onGuiOpen(player, this);

		player.openInventory(this.inventory.get(page));
	}

	public void updateButton() {
		for (GuiSlot<?> slot : this.gui.slots.values()) {
			if (slot instanceof Button button) {
				switch (slot.localType) {
					case GLOBAL -> inventory.forEach((inv -> inv.setItem(button.slot, button.getItem())));
					case LOCAL -> inventory.get(button.page).setItem(button.slot, button.getItem());
				}
			}
		}
	}

	public void reloadSlots() {
		for (GuiSlot<?> slot : this.gui.slots.values()) {
			if (slot instanceof Button button) {
				switch (slot.localType) {
					case GLOBAL -> inventory.forEach((inv -> inv.setItem(button.slot, null)));
					case LOCAL -> inventory.get(button.page).setItem(button.slot, null);
				}
			}
		}

		this.gui.slots.clear();
		this.gui.registerButtons();

		updateButton();
	}

	public void switchPage(int page, Player player) {
		if (hasPage(page)) {
			this.gui.page = page;
			player.openInventory(this.inventory.get(page));
		}
	}

	public boolean hasPage(int page) {
		return page >= 0 && page < this.gui.maxPage;
	}
}
