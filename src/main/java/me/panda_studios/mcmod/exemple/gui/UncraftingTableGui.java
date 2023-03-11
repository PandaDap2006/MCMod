package me.panda_studios.mcmod.exemple.gui;

import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.gui.WorldGui;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class UncraftingTableGui extends Gui {
	public UncraftingTableGui() {
		super("§fऐက§rऑUncrafting Table");
		setSlotType(SlotType.INPUT, 11);
		setSlotType(SlotType.OUTPUT, 6, 7, 8, 15, 16, 17, 24, 25, 26);
	}

	@Override
	public void onGuiClose(InventoryCloseEvent event, WorldGui worldGui) {
		if (event.getInventory().getContents()[10] != null) {
			Item entity = event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation().add(0, 1, 0), event.getInventory().getContents()[10]);
			entity.setVelocity(event.getPlayer().getLocation().getDirection().multiply(0.25));
		}
	}
}
