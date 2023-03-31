package me.panda_studios.mcmod.core.entity;

import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityListener implements Listener {
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Interaction entity && entity.getScoreboardTags().contains("mcmod:entity")) {
//			entity.remove();
		}
	}
}
