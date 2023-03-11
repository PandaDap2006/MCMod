package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {
	@EventHandler
	public void onMenuOpen(final InventoryOpenEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getPlayer().getUniqueId())) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getPlayer().getUniqueId());
			worldGui.gui.onGuiOpen(event, worldGui);
		}
	}

	@EventHandler
	public void onMenuClose(final InventoryCloseEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getPlayer().getUniqueId())) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getPlayer().getUniqueId());
			worldGui.gui.onGuiClose(event, worldGui);
			WorldRegistry.GUIS.remove(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onSlotClick(final InventoryClickEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getWhoClicked().getUniqueId()) && event.getClickedInventory() == event.getView().getTopInventory()) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getWhoClicked().getUniqueId());
			worldGui.gui.onSlotClick(event, worldGui);
			boolean hasSlotType = worldGui.gui.slotTypes.containsKey(event.getSlot());
			switch (event.getAction()) {
				case PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD -> event.setCancelled(
						!(hasSlotType && worldGui.gui.slotTypes.get(event.getSlot()) == Gui.SlotType.INPUT)
				);
				case PICKUP_ALL, PICKUP_ONE, PICKUP_SOME, PICKUP_HALF, MOVE_TO_OTHER_INVENTORY -> event.setCancelled(
						!(hasSlotType && (worldGui.gui.slotTypes.get(event.getSlot()) == Gui.SlotType.INPUT ||
						worldGui.gui.slotTypes.get(event.getSlot()) == Gui.SlotType.OUTPUT))
				);
				default -> event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onItemDrag(final InventoryDragEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getWhoClicked().getUniqueId()) && event.getView().getTopInventory() == event.getInventory()) {
			event.setCancelled(true);
		}
	}
}
