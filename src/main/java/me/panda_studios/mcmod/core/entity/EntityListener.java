package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class EntityListener implements Listener {
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof LivingEntity entity) {
			if (event.getEntity().getScoreboardTags().contains("mcmod:entity")) {
				UUID uuid = null;
				if (event.getEntity().getScoreboardTags().contains("mcmod:entity_base")) {
					uuid = event.getEntity().getUniqueId();
				}
				if (event.getEntity().getScoreboardTags().contains("mcmod:entity_hitbox")) {
					for (String tag : event.getEntity().getScoreboardTags()) {
						if (tag.contains("mcmod:entity_id:")) {
							uuid = UUID.fromString(tag.replace("mcmod:entity_id:", ""));
						}
					}
				}
				WorldEntity worldEntity = WorldRegistry.Entities.get(uuid);
				worldEntity.damage(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue(), event.getDamager());
				event.setCancelled(true);
			}
		}
	}
}
