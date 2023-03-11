package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.utils.Behavior;
import me.panda_studios.mcmod.exemple.setup.ItemSetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Gui extends Behavior implements Cloneable {
	Map<Integer, SlotType> slotTypes = new HashMap<>();

	private final String title;

	public Gui(String title) {
		this.title = title;
	}

	public void OpenMenu(Player player) {
		Inventory inventory = Bukkit.createInventory(player, InventoryType.CHEST, this.title);

		WorldRegistry.GUIS.put(player.getUniqueId(), new WorldGui(this.clone(), inventory));
		player.openInventory(inventory);
	}

	public void onSlotClick(InventoryClickEvent event, WorldGui worldGui) {}
	public void onGuiOpen(InventoryOpenEvent event, WorldGui worldGui) {}
	public void onGuiClose(InventoryCloseEvent event, WorldGui worldGui) {}

	protected void setSlotType(SlotType slotType, int... slot) {
		Arrays.stream(slot).forEach(i -> this.slotTypes.put(i-1, slotType));
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
