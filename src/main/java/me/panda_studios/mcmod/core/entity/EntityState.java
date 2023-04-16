package me.panda_studios.mcmod.core.entity;

import org.joml.Vector2d;

public class EntityState<T extends IEntity> {
	final WorldEntity<T> entity;
	protected boolean isDead = false;

	public EntityState(WorldEntity<T> entity) {
		this.entity = entity;
	}

	public boolean isWalking() {
		Vector2d movement = new Vector2d(entity.getBaseEntity().getVelocity().getX(), entity.getBaseEntity().getVelocity().getZ());
		return movement.length() > 0.001 && entity.getBaseEntity().isOnGround();
	}

	public boolean isDead() {
		return isDead;
	}
}
