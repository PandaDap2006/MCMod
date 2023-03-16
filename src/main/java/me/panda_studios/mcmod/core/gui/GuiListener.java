package me.panda_studios.mcmod.core.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

	@EventHandler
	public void onMenuClose(final InventoryCloseEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getPlayer().getUniqueId())) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getPlayer().getUniqueId());
			worldGui.gui.onGuiClose((Player) event.getPlayer(), worldGui);
		}
	}

	@EventHandler
	public void onSlotClick(final InventoryClickEvent event) {
		if (WorldRegistry.GUIS.containsKey(event.getWhoClicked().getUniqueId()) && event.getClickedInventory() == event.getView().getTopInventory()) {
			WorldGui worldGui = WorldRegistry.GUIS.get(event.getWhoClicked().getUniqueId());
			worldGui.gui.onSlotClick(event, worldGui);
			boolean hasSlotType = worldGui.gui.slotTypes.containsKey(event.getSlot());
			int slot = event.getSlot() + (worldGui.gui.pageSize*(worldGui.gui.page));

			if (worldGui.gui.buttons.containsKey(slot)) {
				Button button = worldGui.gui.buttons.get(slot);
				if (button.buttonState == Button.ButtonState.ENABLED)
					worldGui.gui.onButtonClick(event, button, worldGui);
				event.setCancelled(true);
				return;
			}
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
