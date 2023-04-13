package me.panda_studios.mcmod.core.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.*;

public class WorldEntity<T extends IEntity> {
	private final Map<String, ModelPart> modelParts = new HashMap<>();
	private final List<EntityGoal> goals = new ArrayList<>();

	private final UUID entityBase;
	public final T iEntity;


	public WorldEntity(IEntity iEntity, Location location) {
		this.iEntity = (T) iEntity.clone();
		this.iEntity.worldEntity = this;
		Mob base = location.getWorld().spawn(location, this.iEntity.baseEntity().asSubclass(Mob.class), entity -> {
			entity.addScoreboardTag("mcmod:entity");
			entity.addScoreboardTag("mcmod:entity_base");
			entity.addScoreboardTag("mcmod:entity_id:" + entity.getUniqueId());

			entity.setSilent(true);
			entity.setInvisible(true);
			entity.setInvulnerable(false);

			if (entity instanceof Zombie zombie) {
				zombie.setShouldBurnInDay(false);
				zombie.setAdult();
			}

			try {
				registerGoal(entity);
			} catch (Exception e) {
				throw e;
			}
		});

		this.entityBase = base.getUniqueId();

		this.saveData();
		WorldRegistry.RegisterWorldEntity(this.getBaseEntity());
	}
	public WorldEntity(JsonObject jsonObject) {
		this.entityBase = UUID.fromString(jsonObject.get("uuid").getAsString());
		this.iEntity = (T) Registries.ENTITY.entries.get(jsonObject.get("entity_id").getAsString()).clone();
		this.iEntity.worldEntity = this;

		for (JsonElement bones : jsonObject.getAsJsonArray("bones")) {
			JsonObject bone = (JsonObject) bones;
			Bukkit.getEntity(UUID.fromString(bone.get("uuid").getAsString())).remove();
		}

		registerGoal(getBaseEntity());

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

	private void ticks() {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					LivingEntity entity = (LivingEntity) Bukkit.getEntity(WorldEntity.this.entityBase);
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
				LivingEntity entity = (LivingEntity) Bukkit.getEntity(WorldEntity.this.entityBase);
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
					LivingEntity entity = (LivingEntity) Bukkit.getEntity(WorldEntity.this.entityBase);
					if (entity != null && entity.getLocation().getChunk().isLoaded()) {
						animationUpdate();
					} else if (entity == null) {
						cancel();
					}
				} catch (Exception e) {
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
	public Mob getBaseEntity() {
		return (Mob) Bukkit.getEntity(this.entityBase);
	}

	public void saveData() {
		JsonObject json = new JsonObject();
		json.addProperty("version", "1.0");
		json.addProperty("uuid", this.entityBase.toString());
		json.addProperty("entity_id", this.iEntity.name);
		JsonArray bonesArray = new JsonArray();
		for (ModelPart part: this.modelParts.values()) {
			JsonObject bone = new JsonObject();
			bone.addProperty("uuid", part.displayEntity.getUniqueId().toString());
			bone.addProperty("name", part.name);
			bonesArray.add(bone);
		}
		json.add("bones", bonesArray);

		Entity entity = Bukkit.getEntity(this.entityBase);
		String deleteTag = null;
		for (String tag: entity.getScoreboardTags()) {
			if (tag.contains("mcmod:entity_data:")) {
				deleteTag = tag;
			}
		}
		if (deleteTag != null)
			entity.removeScoreboardTag(deleteTag);
		entity.addScoreboardTag("mcmod:entity_data:" + json);
	}

	int tick = 0;
	private void animationUpdate() {
		Vector velocity = getBaseEntity().getVelocity();
		Vector3f movement = new Vector3f((float) velocity.getX(), 0, (float) velocity.getZ());

		this.iEntity.model.setupAnim(this.getBaseEntity(), this, new EntityState(movement.length() > 0.001), modelParts, tick++);
	}
	private void registerGoal(Mob entity) {
		Bukkit.getMobGoals().removeAllGoals(entity);
		this.iEntity.goals.goals.clear();
		this.iEntity.registerGoals(entity);
		List<Goals.PreGoal> preGoals = this.iEntity.goals.goals;
		preGoals.sort(Comparator.comparingInt(o -> o.priority));
		for (Goals.PreGoal preGoal : preGoals) {
			this.goals.add(preGoal.goal);
		}
	}
}
