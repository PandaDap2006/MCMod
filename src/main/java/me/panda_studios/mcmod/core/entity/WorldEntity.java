package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.animation.Armature;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.datarecords.EntityData;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WorldEntity<T extends IEntity, M extends Entity> {
	private EntityData data;
	private Armature<T> armature;

	private final UUID entityBase;
	public final T iEntity;
	public EntityState<T> state = new EntityState<>(this);

	private List<EntityGoal> goals = new ArrayList<>();

	public static WorldEntity<?, ?> Spawn(IEntity iEntity, Location location) {
		Mob base = location.getWorld().spawn(location, iEntity.baseEntity().asSubclass(Mob.class), entity -> {
			entity.setSilent(true);
			entity.setGravity(true);
			entity.setInvulnerable(false);
			entity.setLootTable(null);
			entity.setInvisible(true);

			if (entity instanceof Zombie zombie) {
				zombie.setShouldBurnInDay(false);
				zombie.setAdult();
			}

			String[] name = iEntity.name.split(":");
			entity.customName(Component.translatable(name[0] + ".entity." + name[1]));
		});

		return new WorldEntity<>(iEntity, base);
	}

	public WorldEntity(T iEntity, M baseEntity) {
		this.iEntity = (T) iEntity.clone();
		this.iEntity.worldEntity = this;
		this.entityBase = baseEntity.getUniqueId();

		this.saveData();
		WorldRegistry.RegisterWorldEntity(this.getBaseEntity());
	}
	public WorldEntity(EntityData data) {
		this.iEntity = (T) Registries.ENTITY.entries.get(data.name()).clone();
		this.iEntity.worldEntity = this;
		this.entityBase = data.uuid();

		data.modelParts().forEach(uuid -> Bukkit.getEntity(uuid).remove());

		try {
			if (getBaseEntity() instanceof Mob mob)
				this.registerGoal(mob);
		} catch (Exception e) {
			throw e;
		}
		this.registerAttributes();

		this.loadModel();
		this.saveData();

		this.ticks();
	}

	private void reloadModel() {
		this.unloadModel();
		this.loadModel();
	}
	private void loadModel() {
		this.armature = new Armature<>(this.iEntity.name, this.entityBase, this.iEntity.model, this.iEntity);
	}
	private void unloadModel() {
		this.armature.remove();
	}

	void registerGoal(Mob entity) {
		Bukkit.getMobGoals().removeAllGoals(entity);
		this.iEntity.goals.goals.clear();
		this.iEntity.registerGoals(entity);
		List<Goals.PreGoal> preGoals = this.iEntity.goals.goals;
		preGoals.sort(Comparator.comparingInt(Goals.PreGoal::priority));
		this.goals = new ArrayList<>();
		for (Goals.PreGoal preGoal : preGoals) {
			this.goals.add(preGoal.goal());
		}
	}
	void registerAttributes() {
		if (this instanceof Mob mob) {
			Attributes attributes = iEntity.attribute();
			if (attributes.has(EntityAttribute.maxHealth))
				mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(attributes.getAsDouble(EntityAttribute.maxHealth));
			if (attributes.has(EntityAttribute.maxSpeed))
				mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(attributes.getAsDouble(EntityAttribute.maxSpeed));
		}
	}

	protected void ticks() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Entity entity = Bukkit.getEntity(WorldEntity.this.entityBase);
					if (entity != null && entity.getLocation().getChunk().isLoaded()) {
						iEntity.tick();
					} else if (entity == null) {
						cancel();
					}
				} catch (Exception e) {
					throw e;
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);

		new BukkitRunnable() {
			@Override
			public void run() {
				Entity entity = WorldEntity.this.getBaseEntity();
				if (entity != null && entity.getLocation().getChunk().isLoaded()) {
					try	{
						WorldEntity.this.goals.forEach(entityGoal -> {
							if (entityGoal.isActive) {
								if (!entityGoal.shouldActivate()) {
									entityGoal.stop();
									entityGoal.isActive = false;
									return;
								}

								entityGoal.tick();
							} else if (entityGoal.shouldActivate()) {
								entityGoal.start();
								entityGoal.isActive = true;
							}
						});
					} catch (Exception e) {
						throw e;
					}
				} else if (entity == null) {
					cancel();
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	public void remove() {
		WorldRegistry.Entities.remove(this.entityBase);
		Entity entity = Bukkit.getEntity(this.entityBase);
		if (entity != null)
			entity.remove();
		this.unloadModel();
	}
	public void kill() {
		this.state.isDead = true;
		iEntity.death();
	}
	public M getBaseEntity() {
		return (M) Bukkit.getEntity(this.entityBase);
	}

	public void saveData() {
		List<UUID> modelparts = new ArrayList<>();
		if (this.armature != null)
			this.armature.joints.values().forEach(m -> modelparts.add(m.displayEntity.getUniqueId()));
		this.data = new EntityData(
				EntityData.CurrentVersion,
				this.entityBase,
				this.iEntity.name,
				modelparts
		);
		Entity entity = Bukkit.getEntity(this.entityBase);
		entity.getPersistentDataContainer().set(IEntity.DataNamespace, DataTypes.Entity, this.data);
	}
}
