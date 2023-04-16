package me.panda_studios.mcmod.exemple.entity.goals;

import me.panda_studios.mcmod.core.entity.EntityGoal;
import me.panda_studios.mcmod.core.utils.EntityUtils;
import org.bukkit.Particle;
import org.bukkit.entity.Mob;
import org.joml.Math;

public class ReaperMagic extends EntityGoal {
	final Mob entity;
	int castCooldown = 0;
	public int castingTime = 0;

	public ReaperMagic(Mob entity) {
		this.entity = entity;
	}

	@Override
	public boolean shouldActivate() {
		return entity.getTarget() != null;
	}

	@Override
	public void tick() {
		castCooldown++;
		entity.lookAt(entity.getTarget());
		if (EntityUtils.isInRadiusOf(entity, entity.getTarget(), 5)) {
			entity.getPathfinder().stopPathfinding();
			if (castCooldown > 60) {
				if (castingTime++ < 60) {
					entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation().add(0, 2.5, 0), 2);
				} else {
					castCooldown = 0;
					float distence = (float) entity.getLocation().distance(entity.getTarget().getLocation());
					entity.getTarget().damage(10/ Math.min(distence, 1), entity);
				}
			} else {
				castingTime = 0;
			}
		} else {
			entity.getPathfinder().moveTo(entity.getTarget());
			castingTime = 0;
		}
	}

	@Override
	public void stop() {
		this.castingTime = 0;
		this.castCooldown = 0;
	}
}
