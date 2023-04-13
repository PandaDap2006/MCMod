package me.panda_studios.mcmod.core.entity;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.GameEvent;
import org.bukkit.entity.*;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class IEntity extends Behavior implements Cloneable {
	public final EntityModel model;
	public WorldEntity worldEntity;

	protected Goals goals = new Goals();

	public IEntity(EntityModel entityModel) {
		this.model = entityModel;
	}

	public Attributes attribute() {
		return new Attributes().add(Attribute.gravity).add(Attribute.maxSpeed).add(Attribute.maxHealth);
	}
	public void registerGoals(Mob entity) {
	}
	public double damage(WorldEntity worldEntity, double damage, Entity damager) {
		Entity entity = worldEntity.getBaseEntity();

		Vector3d position = new Vector3d(entity.getLocation().getX(), 0, entity.getLocation().getZ());
		Vector3d target = new Vector3d(damager.getLocation().getX(), 0, damager.getLocation().getZ());
		target.sub(position);

		if (target.length() > 0) {
			target.normalize();
		}

//		worldEntity.velocity.x = -target.x*.2;
//		worldEntity.velocity.z = -target.z*.2;
//		if (worldEntity.getBaseEntity().isOnGround())
//			worldEntity.velocity.y = .3;

		return damage;
	}
	public boolean death() {
		return true;
	}
	public void tick() {
	}
	public Class<?> baseEntity() {
		return Zombie.class;
	}

	@Override
	public IEntity clone() {
		try {
			IEntity clone = (IEntity) super.clone();
			// TODO: copy mutable state here, so the clone can't change the internals of the original
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
