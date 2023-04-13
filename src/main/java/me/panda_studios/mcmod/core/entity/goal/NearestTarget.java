package me.panda_studios.mcmod.core.entity.goal;

import me.panda_studios.mcmod.core.entity.EntityGoal;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.List;

public class NearestTarget<T extends LivingEntity> extends EntityGoal {
	final Mob entity;
	final Class<T> targetClass;
	final double range;

	public NearestTarget(Mob entity, Class<T> targetClass, double range) {
		this.entity = entity;
		this.targetClass = targetClass;
		this.range = range;
	}

	@Override
	public boolean shouldActivate() {
		return true;
	}

	@Override
	public void tick() {
		List<T> entities = this.entity.getWorld().getNearbyEntitiesByType(this.targetClass, this.entity.getLocation(), this.range).stream().toList();
		if (entities.size() > 0 && this.entity.getTarget() == null) {
			for (Entity target : entities) {
				if (target instanceof Player player) {
					if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
						return;
				}
				this.entity.setTarget((LivingEntity) target);
				break;
			}
		} else if (this.entity.getTarget() == null || (this.entity.getTarget() instanceof Player player &&
				(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR))) {
			this.entity.setTarget(null);
		}
	}
}
