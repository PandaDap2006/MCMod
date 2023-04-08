package me.panda_studios.mcmod.core.entity.model;

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
import org.bukkit.util.Transformation;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import javax.annotation.Nullable;

public class ModelPart {
	public ItemDisplay displayEntity;
	private final WorldEntity entity;
	public final ModelPart parent;
	public final String name;

	Vector3d location = new Vector3d();
	Vector3d rotation = new Vector3d();
	Vector3d scale = new Vector3d(1, 1, 1);

	Vector3d defaultLocation;
	Vector3d defaultRotation;
	Vector3d defaultScale;

	public ModelPart(WorldEntity entity, String name, Vector3d location, Vector3d rotation, Vector3d scale, @Nullable ModelPart parent) {
		this.entity = entity;
		this.name = name;
		this.defaultLocation = location;
		this.defaultRotation = rotation;
		this.defaultScale = scale;
		this.parent = parent;

		this.displayEntity = entity.getBaseEntity().getWorld().spawn(entity.getBaseEntity().getLocation(), ItemDisplay.class, e -> {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta meta = itemStack.getItemMeta();
			meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(entity.iEntity.name + "_" + name));
			itemStack.setItemMeta(meta);
			e.setItemStack(itemStack);

			e.addScoreboardTag("mcmod:entity");
			e.addScoreboardTag("mcmod:entity_modelpart");
			e.addScoreboardTag("mcmod:entity_id:" + ModelPart.this.entity.getBaseEntity().getUniqueId());
		});
		entity.getBaseEntity().addPassenger(displayEntity);
	}

	public void tick() {
		if (displayEntity != null) {
			Transformation transform = displayEntity.getTransformation();
			displayEntity.setTransformation(new Transformation(
					new Vector3f((float) defaultLocation.x, (float) defaultLocation.y, (float) defaultLocation.z),
					transform.getLeftRotation(),
					new Vector3f((float) defaultScale.x, (float) defaultScale.y, (float) defaultScale.z).mul(10),
					transform.getRightRotation()
			));
		}
	}
}
