package me.panda_studios.mcmod.core.entity.model;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Transformation;
import org.joml.*;

import javax.annotation.Nullable;
import java.lang.Math;

public class ModelPart {
	public ItemDisplay displayEntity;
	private final WorldEntity entity;
	private final ModelPart parent;
	public final String name;

	public Vector3d location = new Vector3d();
	public Vector3d rotation = new Vector3d();
	public Vector3d scale = new Vector3d(1, 1, 1);

	final Vector3d defaultLocation;
	final Vector3d defaultRotation;
	final Vector3d defaultScale;

	public ModelPart(WorldEntity entity, String name, Vector3d location, Vector3d rotation, Vector3d scale, @Nullable ModelPart parent) {
		this.entity = entity;
		this.name = name;
		this.defaultLocation = location;
		this.defaultRotation = rotation;
		this.defaultScale = scale;
		this.parent = parent;

		this.displayEntity = entity.getBaseEntity().getWorld().spawn(entity.getBaseEntity().getLocation(), ItemDisplay.class, e -> {
			String modelName = entity.iEntity.name + "_" + name;

			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta meta = itemStack.getItemMeta();
			meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(modelName));
			itemStack.setItemMeta(meta);
			e.setItemStack(itemStack);

			e.addScoreboardTag("mcmod:entity");
			e.addScoreboardTag("mcmod:entity_modelpart");
			e.addScoreboardTag("mcmod:entity_id:" + ModelPart.this.entity.getBaseEntity().getUniqueId());

			e.setInterpolationDuration(1);
		});
		entity.getBaseEntity().addPassenger(displayEntity);

		new BukkitRunnable() {
			@Override
			public void run() {
				LivingEntity entity = ModelPart.this.entity.getBaseEntity();
				if (entity != null && entity.getLocation().getChunk().isLoaded()) {
					tick();
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	public void tick() {
		if (displayEntity != null) {
			displayEntity.setInterpolationDelay(0);

			Transformation transform = displayEntity.getTransformation();

			Vector3d location = new Vector3d(this.defaultLocation).add(new Vector3d(this.location).div(16));
			Vector3d rotation = new Vector3d(this.defaultRotation).add(this.rotation);
			Vector3d scale = new Vector3d(this.defaultScale).add(this.scale).sub(1, 1, 1);

			Quaternionf rotationQuat = new Quaternionf().identity().rotateXYZ(
					(float) -Math.toRadians(rotation.x),
					(float) Math.toRadians(rotation.y),
					(float) Math.toRadians(rotation.z)
			);

			if (parent != null) {
				Transformation parentTransform = parent.displayEntity.getTransformation();

				rotationQuat.mul(parentTransform.getLeftRotation());

				location.rotate(new Quaterniond(parentTransform.getLeftRotation()));
				location.add(parentTransform.getTranslation());
			} else {
				location.y -= this.entity.getBaseEntity().getHeight() - ((double) 8 / 16);
			}

			scale.mul(10);

			displayEntity.setTransformation(new Transformation(
					new Vector3f((float) location.x, (float) location.y, (float) location.z),
					rotationQuat,
					new Vector3f((float) scale.x, (float) scale.y, (float) scale.z),
					transform.getRightRotation()
			));
		}
	}
}
