package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.utils.Mth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Vector2d;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldEntity {
	public final Map<String, ModelPart> modelparts = new HashMap<>();

	private final UUID entityBase;
	private final UUID hitbox;
	private final IEntity iEntity;
	private Vector2d rotation = new Vector2d();
	public Vector3d velocity = new Vector3d();
	private boolean jump = false;

	private double health;

	public WorldEntity(IEntity iEntity, Location location) {
		this.iEntity = iEntity;
		LivingEntity base = (LivingEntity) location.getWorld().spawn(location, iEntity.BaseEntity().getEntityClass(), e -> {
			LivingEntity entity = (LivingEntity) e;
			entity.addScoreboardTag("mcmod:entity");
			entity.addScoreboardTag("mcmod:entity_base");
			entity.setSilent(true);
			entity.setInvulnerable(true);
			entity.setRemoveWhenFarAway(false);
			entity.setPersistent(true);
			entity.setInvisible(true);
			if (entity instanceof Mob mob) {
				mob.setAware(false);
			}
		});
		Interaction hitbox = location.getWorld().spawn(location, Interaction.class, e -> {
			e.addScoreboardTag("mcmod:entity");
			e.addScoreboardTag("mcmod:entity_hitbox");
			e.addScoreboardTag("mcmod:entity_id:" + base.getUniqueId());
		});
		base.addPassenger(hitbox);

		this.reloadModel();

		this.entityBase = base.getUniqueId();
		this.hitbox = hitbox.getUniqueId();

		this.health = iEntity.attribute().getAsDouble(Attribute.maxHealth);

		this.ticks();
	}

	private void reloadModel() {
		modelparts.clear();
		this.loadModel();
	}

	private void loadModel() {

	}

	private void ticks() {
		new BukkitRunnable() {
			@Override
			public void run() {
				LivingEntity entity = (LivingEntity) Bukkit.getEntity(WorldEntity.this.entityBase);
				if (entity != null && entity.getLocation().getChunk().isLoaded()) {
					movementUpdate();
					iEntity.tick(WorldEntity.this);
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	private void movementUpdate() {
		double gravity = this.iEntity.attribute().getAsDouble(Attribute.gravity);
		LivingEntity entity = (LivingEntity) Bukkit.getEntity(this.entityBase);
		Interaction hitbox = (Interaction) Bukkit.getEntity(this.hitbox);

		entity.setRotation(0, 0);

		entity.setVelocity(new Vector(this.velocity.x, this.velocity.y, this.velocity.z));
		hitbox.setInteractionHeight((float) this.iEntity.hitbox.y);
		hitbox.setInteractionWidth((float) this.iEntity.hitbox.x);

		if (entity.isInWater()) {
			this.velocity.y = stepToNumber(this.velocity.y, -gravity/3, gravity);
		} else {
			if (entity.isOnGround()) {
				this.velocity.y = stepToNumber(this.velocity.y, -gravity, gravity);
			} else {
				this.velocity.y -= gravity;
			}
		}

		if (jump) {
			this.velocity.y = 1;
			jump = false;
		}

		if (entity.isOnGround()) {
			this.velocity.x = stepToNumber(this.velocity.x, 0, gravity/3);
			this.velocity.z = stepToNumber(this.velocity.z, 0, gravity/3);
		}
	}

	public void jump() {
		this.jump = true;
	}

	public void damage(double damage, Entity damager) {
		this.health -= this.iEntity.damage(this, damage, damager);
		this.health = Mth.clamp(this.health, 0, iEntity.attribute().getAsDouble(Attribute.maxHealth));
		checkIfDead();
	}

	private void checkIfDead() {
		if (this.health <= 0) {
			if (this.iEntity.death(this)) {
				WorldRegistry.Entities.remove(this.entityBase);
				Bukkit.getEntity(this.entityBase).remove();
				Bukkit.getEntity(this.hitbox).remove();
			}
		}
	}

	private double stepToNumber(double current, double target, double step) {
		return current > target ? Math.max(current - step, target) : Math.min(current + step, target);
	}

	public LivingEntity getBaseEntity() {
		return (LivingEntity) Bukkit.getEntity(this.entityBase);
	}
}
