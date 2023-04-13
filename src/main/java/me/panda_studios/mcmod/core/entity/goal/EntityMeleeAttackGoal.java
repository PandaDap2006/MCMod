package me.panda_studios.mcmod.core.entity.goal;

import me.panda_studios.mcmod.core.entity.EntityGoal;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

public class EntityMeleeAttackGoal extends EntityGoal {
	final Mob entity;

	public EntityMeleeAttackGoal(Mob entity) {
		this.entity = entity;
	}

	@Override
	public boolean shouldActivate() {
		return true;
	}

	@Override
	public void tick() {
		if (entity.getTarget() != null) {
			entity.getPathfinder().moveTo(entity.getTarget());
			entity.lookAt(entity.getTarget());
			if (entity.getWorld().getNearbyEntitiesByType(LivingEntity.class, entity.getLocation(), 0.5f).contains(entity.getTarget())) {
				entity.attack(entity.getTarget());
			}
		}
	}
}
