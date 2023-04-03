package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.joml.Vector2d;
import org.joml.Vector3d;

public abstract class IEntity extends Behavior {
	protected Vector2d hitbox;

	public IEntity(Vector2d hitbox) {
		this.hitbox = hitbox;
	}

	public EntityType BaseEntity() {
		return EntityType.SILVERFISH;
	};
	public abstract String ModelLocation();

	public Attributes attribute() {
		return new Attributes().add(Attribute.gravity).add(Attribute.maxSpeed).add(Attribute.maxHealth);
	}

	public double damage(WorldEntity worldEntity, double damage, Entity damager) {
		Entity entity = worldEntity.getBaseEntity();

		Vector3d position = new Vector3d(entity.getLocation().getX(), 0, entity.getLocation().getZ());
		Vector3d target = new Vector3d(damager.getLocation().getX(), 0, damager.getLocation().getZ());
		target.sub(position);

		if (target.length() > 0) {
			target.normalize();
		}

		worldEntity.velocity.x = -target.x*.2;
		worldEntity.velocity.z = -target.z*.2;
		if (worldEntity.getBaseEntity().isOnGround())
			worldEntity.velocity.y = .3;

		return damage;
	}

	public boolean death(WorldEntity worldEntity) {
		return true;
	}

	public void tick(WorldEntity worldEntity) {
	}
}
