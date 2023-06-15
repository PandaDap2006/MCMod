package me.panda_studios.mcmod.core.entity.goal;

import me.panda_studios.mcmod.core.entity.EntityGoal;
import org.bukkit.entity.Mob;

public class FollowTargetGoal extends EntityGoal {
	final Mob entity;

	public FollowTargetGoal(Mob entity) {
		this.entity = entity;
	}

	@Override
	public boolean shouldActivate() {
		return entity.getTarget() != null;
	}

	@Override
	public void tick() {
		entity.getPathfinder().moveTo(entity.getTarget());
	}
}
