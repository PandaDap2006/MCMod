package me.panda_studios.mcmod.core.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import me.panda_studios.mcmod.core.utils.JsonUtils;
import me.panda_studios.mcmod.core.utils.Mth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WorldEntity {
	private final Map<String, ModelPart> modelParts = new HashMap<>();

	private final UUID entityBase;
	private final UUID hitbox;
	public final IEntity iEntity;
	public Vector3d velocity = new Vector3d();
	private boolean jump = false;

	private double health;

	public WorldEntity(IEntity iEntity, Location location) {
		this.iEntity = iEntity.clone();
		this.iEntity.worldEntity = this;
		LivingEntity base = location.getWorld().spawn(location, Silverfish.class, entity -> {
			entity.addScoreboardTag("mcmod:entity");
			entity.addScoreboardTag("mcmod:entity_base");
			entity.addScoreboardTag("mcmod:entity_id:" + entity.getUniqueId());
			entity.setSilent(true);
			entity.setInvulnerable(true);
			entity.setRemoveWhenFarAway(false);
			entity.setPersistent(true);
			entity.setInvisible(true);
			entity.setAware(false);
			entity.setGravity(false);
		});
		Interaction hitbox = location.getWorld().spawn(location, Interaction.class, entity -> {
			entity.addScoreboardTag("mcmod:entity");
			entity.addScoreboardTag("mcmod:entity_hitbox");
			entity.addScoreboardTag("mcmod:entity_id:" + base.getUniqueId());
		});
		base.addPassenger(hitbox);

		this.entityBase = base.getUniqueId();
		this.hitbox = hitbox.getUniqueId();

		this.health = iEntity.attribute().getAsDouble(Attribute.maxHealth);

		this.saveData();
		WorldRegistry.RegisterWorldEntity(this.getBaseEntity());
	}

	public WorldEntity(JsonObject jsonObject) {
		this.entityBase = UUID.fromString(jsonObject.get("uuid").getAsString());
		this.hitbox = UUID.fromString(jsonObject.get("hitbox_uuid").getAsString());
		this.iEntity = Registries.ENTITY.entries.get(jsonObject.get("entity_id").getAsString()).clone();
		this.iEntity.worldEntity = this;

		for (JsonElement bones : jsonObject.getAsJsonArray("bones")) {
			JsonObject bone = (JsonObject) bones;
			Bukkit.getEntity(UUID.fromString(bone.get("uuid").getAsString())).remove();
		}

		this.health = iEntity.attribute().getAsDouble(Attribute.maxHealth);

		this.loadModel();
		this.saveData();

		this.ticks();
	}

	private void reloadModel() {
		this.unloadModel();
		this.loadModel();
	}

	private void loadModel() {
		JsonObject jsonRoot = ResourceManager.getJsonFile(iEntity.plugin, "models/" + iEntity.model.modelLocation(iEntity) + ".json");
		if (jsonRoot.get("format_version").getAsString().equals("1.12.0")) {
			JsonObject geometry = jsonRoot.getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject();
			for (JsonElement bones : geometry.getAsJsonArray("bones")) {
				JsonObject bone = bones.getAsJsonObject();

				Vector3d location = new Vector3d();
				Vector3d rotation = new Vector3d();
				Vector3d scale = new Vector3d(1, 1, 1);

				if (bone.has("pivot"))
					location = new Vector3d(
							bone.getAsJsonArray("pivot").get(0).getAsDouble(),
							bone.getAsJsonArray("pivot").get(1).getAsDouble(),
							bone.getAsJsonArray("pivot").get(2).getAsDouble()
					);

				if (bone.has("rotation"))
					rotation = new Vector3d(
							bone.getAsJsonArray("rotation").get(0).getAsDouble(),
							bone.getAsJsonArray("rotation").get(1).getAsDouble(),
							bone.getAsJsonArray("rotation").get(2).getAsDouble()
					);

				if (bone.has("scale"))
					scale = new Vector3d(
							bone.getAsJsonArray("scale").get(0).getAsDouble(),
							bone.getAsJsonArray("scale").get(1).getAsDouble(),
							bone.getAsJsonArray("scale").get(2).getAsDouble()
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
				LivingEntity entity = (LivingEntity) Bukkit.getEntity(WorldEntity.this.entityBase);
				if (entity != null && entity.getLocation().getChunk().isLoaded()) {
					movementUpdate();
					iEntity.tick();
					WorldEntity.this.modelParts.forEach((k, v) -> v.tick());
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
			if (this.iEntity.death()) {
				WorldRegistry.Entities.remove(this.entityBase);
				Bukkit.getEntity(this.entityBase).remove();
				Bukkit.getEntity(this.hitbox).remove();
				this.unloadModel();
			}
		}
	}

	private double stepToNumber(double current, double target, double step) {
		return current > target ? Math.max(current - step, target) : Math.min(current + step, target);
	}

	public LivingEntity getBaseEntity() {
		return (LivingEntity) Bukkit.getEntity(this.entityBase);
	}

	public void saveData() {
		JsonObject json = new JsonObject();
		json.addProperty("version", "1.0");
		json.addProperty("uuid", this.entityBase.toString());
		json.addProperty("entity_id", this.iEntity.name);
		json.addProperty("hitbox_uuid", this.hitbox.toString());
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
}
