package me.panda_studios.mcmod.core.item;

import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.utils.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {
	Cooldown cooldown = new Cooldown();

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent event) {
		for (IItem iItem : Registries.ITEM.entries.values()) {
			Player player = event.getPlayer();
			Boolean itemInOffhand = !player.getEquipment().getItemInOffHand().getType().isAir();
			Boolean hand = itemInOffhand ? event.getHand() == EquipmentSlot.OFF_HAND : event.getHand() == EquipmentSlot.HAND;
			ItemStack itemStack = event.getItem();
			if (hand && iItem.is(itemStack) && !cooldown.isOnCooldown()) {
				cooldown.start(2);
				if (event.getAction() == Action.RIGHT_CLICK_AIR) {
					Registries.ITEM.entries.get(iItem.name).use(player, itemStack);
				} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Registries.ITEM.entries.get(iItem.name).useOn(player, itemStack, event.getClickedBlock(), event.getBlockFace());
				}
			}
		}
	}
}
