package me.panda_studios.mcmod.core.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMenuClose(final InventoryCloseEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getPlayer().getUniqueId())) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getPlayer().getUniqueId());
			worldGui.gui.onGuiClose((Player) event.getPlayer(), worldGui);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSlotClick(final InventoryClickEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getWhoClicked().getUniqueId()) && event.getClickedInventory() == event.getView().getTopInventory()) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getWhoClicked().getUniqueId());
			int slotID = event.getSlot() + (worldGui.gui.pageSize*(worldGui.gui.page));
			boolean hasSlot = worldGui.gui.slots.containsKey(slotID);
			if (hasSlot) {
				GuiSlot<?> guiSlot = worldGui.gui.slots.get(slotID);
				if (guiSlot.condition.isActive(guiSlot)) {
					if (guiSlot instanceof Slot slot) {
						switch (event.getAction()) {
							case PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD ->
									event.setCancelled(
											!(slot.slotType == SlotType.INPUT)
									);
							case PICKUP_ALL, PICKUP_ONE, PICKUP_SOME, PICKUP_HALF, MOVE_TO_OTHER_INVENTORY -> event.setCancelled(
									!(slot.slotType == SlotType.INPUT || slot.slotType == SlotType.OUTPUT)
							);
							default -> event.setCancelled(true);
						}
					} else {
						event.setCancelled(true);
					}
					guiSlot.clickAction.activation(guiSlot, (Player) event.getWhoClicked());
				} else {
					event.setCancelled(true);
				}
			} else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				event.setCancelled(true);
			} else {
				event.setCancelled(true);
			}
		} else {
			event.setCancelled(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onItemDrag(final InventoryDragEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getWhoClicked().getUniqueId()) && event.getView().getTopInventory() == event.getInventory()) {
			event.setCancelled(true);
		}
	}
}
