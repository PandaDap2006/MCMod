package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.utils.EntityUtils;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Mob && EntityUtils.isIEntity((Mob) event.getEntity())) {
			WorldEntity worldEntity = WorldRegistry.Entities.get(event.getEntity().getUniqueId());
			if (!worldEntity.state.isDead())
				event.setDamage(worldEntity.iEntity.takeDamage(event.getDamager(), event.getDamage()));
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Mob && EntityUtils.isIEntity((Mob) event.getEntity())) {
			event.setCancelled(true);
			WorldEntity worldEntity = WorldRegistry.Entities.get(event.getEntity().getUniqueId());
			worldEntity.kill();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Mob && EntityUtils.isIEntity((Mob) event.getDamager())) {
			WorldEntity worldEntity = WorldRegistry.Entities.get(event.getDamager().getUniqueId());
			event.setDamage(worldEntity.iEntity.damageOther(event.getEntity(), event.getDamage()));
		}
	}
}
