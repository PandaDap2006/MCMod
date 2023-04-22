package me.panda_studios.mcmod.core.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.datarecords.EntityData;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3f;

import java.util.*;

public class WorldEntity<T extends IEntity, M extends Entity> {
	private final Map<String, ModelPart> modelParts = new HashMap<>();
	private EntityData data;

	private final UUID entityBase;
	public final T iEntity;
	public EntityState<T> state = new EntityState<>(this);

	private List<EntityGoal> goals = new ArrayList<>();

	public static WorldEntity Spawn(IEntity iEntity, Location location) {
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
		JsonObject jsonRoot = ResourceManager.getJsonFile(iEntity.plugin, "models/" + iEntity.model.modelLocation() + ".json");
		if (jsonRoot.get("format_version").getAsString().equals("1.12.0")) {
			JsonObject geometry = jsonRoot.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject();
			for (JsonElement bones : geometry.getAsJsonArray("bones")) {
				JsonObject bone = bones.getAsJsonObject();

				Vector3f location = new Vector3f();
				Vector3f rotation = new Vector3f();
				Vector3f scale = new Vector3f(1, 1, 1);

				if (bone.has("pivot"))
					location = new Vector3f(
							bone.getAsJsonArray("pivot").get(0).getAsFloat(),
							bone.getAsJsonArray("pivot").get(1).getAsFloat(),
							bone.getAsJsonArray("pivot").get(2).getAsFloat()
					);

				if (bone.has("rotation"))
					rotation = new Vector3f(
							bone.getAsJsonArray("rotation").get(0).getAsFloat(),
							bone.getAsJsonArray("rotation").get(1).getAsFloat(),
							bone.getAsJsonArray("rotation").get(2).getAsFloat()
					);

				if (bone.has("scale"))
					scale = new Vector3f(
							bone.getAsJsonArray("scale").get(0).getAsFloat(),
							bone.getAsJsonArray("scale").get(1).getAsFloat(),
							bone.getAsJsonArray("scale").get(2).getAsFloat()
					);

				ModelPart parent = null;
				if (bone.has("parent"))
					parent = this.modelParts.get(bone.get("parent").getAsString());

				this.modelParts.put(bone.get("name").getAsString(),
						new ModelPart(this, bone.get("name").getAsString(), location.div(16), rotation, scale, parent));
			}
		}
	}
	private void unloadModel() {
		this.modelParts.forEach((key, value) -> {
			value.displayEntity.remove();
		});
		this.modelParts.clear();
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

		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Entity entity = Bukkit.getEntity(WorldEntity.this.entityBase);
					if (entity != null && entity.getLocation().getChunk().isLoaded()) {
						animationUpdate();
					} else if (entity == null) {
						cancel();
					}
				} catch (Exception e) {
					WorldEntity.this.remove();
					throw e;
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
		this.modelParts.values().forEach(m -> modelparts.add(m.displayEntity.getUniqueId()));
		this.data = new EntityData(
				EntityData.CurrentVersion,
				this.entityBase,
				this.iEntity.name,
				modelparts
		);
		Entity entity = Bukkit.getEntity(this.entityBase);
		entity.getPersistentDataContainer().set(IEntity.DataNamespace, DataTypes.Entity, this.data);
	}

	int tick = 0;
	private void animationUpdate() {
		this.iEntity.model.setupAnim(this.getBaseEntity(), this, this.state, modelParts, tick++);
	}
}
