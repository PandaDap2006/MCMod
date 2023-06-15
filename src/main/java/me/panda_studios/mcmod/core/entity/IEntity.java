package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.animation.Model;
import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;

public class IEntity extends Behavior implements Cloneable {
	public static final NamespacedKey DataNamespace = new NamespacedKey(Mcmod.plugin, "entity.data");

	public final Model<?> model;
	public WorldEntity worldEntity;
	protected Goals goals = new Goals();

	public IEntity(Model<?> model) {
		this.model = model;
	}

	public Attributes attribute() {
		return new Attributes().add(EntityAttribute.maxSpeed).add(EntityAttribute.maxHealth).add(EntityAttribute.attackDamage);
	}
	public void registerGoals(Mob entity) {
	}
	public double damageOther(Entity damaged, double damage) {
		return damage;
	}
	public double takeDamage(Entity damager, double damage) {
		return damage;
	}
	public void	death() {
		worldEntity.remove();
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
