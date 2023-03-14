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
	protected int page = 1;

	public Gui(String title, int pageSize) {
		this(title, pageSize, 1);
	}

	public Gui(String title, int pageSize, int maxPage) {
		this.title = title;
		this.pageSize = 9*pageSize;
		this.maxPage = maxPage;
	}

	public void OpenMenu(Player player) {
		this.OpenMenu(player, 1);
	}

	public void OpenMenu(Player player, int page) {
		player.closeInventory();
		if (WorldRegistry.GUIS.containsKey(player.getUniqueId())) {
			WorldRegistry.GUIS.get(player.getUniqueId()).gui.onGuiClose(player, WorldRegistry.GUIS.get(player.getUniqueId()));
		}
		this.page = page;

		String title = this.title;
		if (this.maxPage > 1)
			title += " - Page " + page + " - " + maxPage;
		Inventory inventory = Bukkit.createInventory(player, pageSize, title);
		WorldRegistry.GUIS.put(player.getUniqueId(), new WorldGui(this.clone(), inventory));
		buttons.forEach((slot, btn) -> {
			int newSlot = slot - (this.pageSize*(page-1));
			if (newSlot >= 0 && newSlot < this.pageSize) {
				btn.buttonState = WorldRegistry.GUIS.get(player.getUniqueId()).gui.getButtonState(btn, WorldRegistry.GUIS.get(player.getUniqueId()));
				inventory.setItem(newSlot, btn.getItem());
			}
		});

		WorldRegistry.GUIS.get(player.getUniqueId()).gui.onGuiOpen(player, WorldRegistry.GUIS.get(player.getUniqueId()));
		player.openInventory(inventory);
	}

	public void onButtonClick(InventoryClickEvent event, Button button, WorldGui worldGui) {}
	public Button.ButtonState getButtonState(Button button, WorldGui worldGui) {
		return Button.ButtonState.ENABLED;
	}
	public void onSlotClick(InventoryClickEvent event, WorldGui worldGui) {}
	public void onGuiOpen(Player player, WorldGui worldGui) {}
	public void onGuiClose(Player player, WorldGui worldGui) {}

	protected void setSlotType(SlotType slotType, int... slot) {
		Arrays.stream(slot).forEach(i -> this.slotTypes.put(i-1, slotType));
	}

	protected void setButton(String name, int slot, String label, LocalType localType, int modelDataEnabled, int modelDataDisabled, int page) {
		Button button = new Button(slot, name, label, modelDataEnabled, modelDataDisabled);
		switch (localType) {
			case LOCAL -> {
				int newSlot = (slot-1) + (this.pageSize*(page-1));
				this.buttons.put(newSlot, button);
			}
			case GLOBAL -> {
				for (int i = 0; i < maxPage; i++) {
					int newSlot = (slot-1) + (this.pageSize*(i));
					this.buttons.put(newSlot, button);
				}
			}
		}
	}

	protected void setButton(String name, int slot, String label) {
		this.setButton(name, slot, label, LocalType.GLOBAL, 30001, 30001, 1);
	}

	protected void setButton(String name, int slot, String label, int page) {
		this.setButton(name, slot, label, LocalType.LOCAL, 30001, 30001, page);
	}

	protected boolean hasPage(int page) {
		return page > 0 && page <= this.maxPage;
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
