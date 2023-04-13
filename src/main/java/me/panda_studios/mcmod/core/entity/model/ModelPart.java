package me.panda_studios.mcmod.core.entity.model;

import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.joml.*;

import javax.annotation.Nullable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class ModelPart {
	public ItemDisplay displayEntity;
	private final WorldEntity entity;
	public final String name;

	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private Vector3f scale = new Vector3f(1);
	private final Vector3f defaultPosition;
	private final Vector3f defaultRotation;
	private final Vector3f defaultScale;
	private final ModelPart parent;
	private List<ModelPart> children = new ArrayList<>();

	public ModelPart(WorldEntity entity, String name, Vector3f location, Vector3f rotation, Vector3f scale, @Nullable ModelPart parent) {
		this.entity = entity;
		this.name = name;
		this.parent = parent;

		this.displayEntity = entity.getBaseEntity().getWorld().spawn(entity.getBaseEntity().getLocation(), ItemDisplay.class, e -> {
			String modelName = entity.iEntity.name + "_" + name;

			if (ResourceManager.modelBaseItem.modelIDs.containsKey(modelName)) {
				ItemStack itemStack = new ItemStack(Material.PAPER);
				ItemMeta meta = itemStack.getItemMeta();
				meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(modelName));
				itemStack.setItemMeta(meta);
				e.setItemStack(itemStack);
			}

			e.addScoreboardTag("mcmod:entity");
			e.addScoreboardTag("mcmod:entity_modelpart");
			e.addScoreboardTag("mcmod:entity_id:" + ModelPart.this.entity.getBaseEntity().getUniqueId());

			e.setInterpolationDuration(1);
		});
		entity.getBaseEntity().addPassenger(displayEntity);

		this.defaultPosition = location;
		this.defaultRotation = rotation;
		this.defaultScale = scale;
		if (this.parent != null)
			this.parent.children.add(this);
		updateTransform();
	}

	public Vector3f[] getTransform() {
		return new Vector3f[] {
				new Vector3f(position),
				new Vector3f(rotation),
				new Vector3f(scale)
		};
	}

	public void setPosition(Vector3f position) {
		this.position = position;
		updateTransform();
	}
	public Vector3f getPosition() {
		return new Vector3f(this.position);
	}
	public void setX(float amount) {
		this.setPosition(new Vector3f(amount, getPosition().y, getPosition().z));
	}
	public void setY(float amount) {
		this.setPosition(new Vector3f(getPosition().x, amount, getPosition().z));
	}
	public void setZ(float amount) {
		this.setPosition(new Vector3f(getPosition().x, getPosition().y, amount));
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
		updateTransform();
	}
	public Vector3f getRotation() {
		return new Vector3f(this.rotation);
	}
	public void setRotX(float amount) {
		this.setRotation(new Vector3f(amount, getRotation().y, getRotation().z));
	}
	public void setRotY(float amount) {
		this.setRotation(new Vector3f(getRotation().x, amount, getRotation().z));
	}
	public void setRotZ(float amount) {
		this.setRotation(new Vector3f(getRotation().x, getRotation().y, amount));
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
		updateTransform();
	}
	public Vector3f getScale() {
		return new Vector3f(this.scale);
	}
	public void setScaleX(float amount) {
		this.setScale(new Vector3f(amount, getScale().y, getScale().z));
	}
	public void setScaleY(float amount) {
		this.setScale(new Vector3f(getScale().x, amount, getScale().z));
	}
	public void setScaleZ(float amount) {
		this.setScale(new Vector3f(getScale().x, getScale().y, amount));
	}

	private void updateTransform() {
		this.children.forEach(ModelPart::updateTransform);
		this.displayEntity.setInterpolationDelay(0);

		Vector3f[] transform = getTransform();

		Vector3f translation = new Vector3f(defaultPosition);
		Quaternionf rotation;
		Vector3f scale = new Vector3f(defaultScale);

		Quaternionf totalRotation = new Quaternionf().identity();
		if (parent != null) {
			totalRotation.mul(parent.displayEntity.getTransformation().getLeftRotation());
		}
//		totalRotation.mul(new Quaternionf().identity().rotationXYZ(
//				(float) -Math.toRadians(defaultRotation.x),
//				(float) Math.toRadians(defaultRotation.y),
//				(float) Math.toRadians(defaultRotation.z)
//		));
		totalRotation.mul(new Quaternionf().identity()
				.rotateLocalX((float) -Math.toRadians(defaultRotation.x))
				.rotateLocalY((float) Math.toRadians(defaultRotation.y))
				.rotateLocalZ((float) Math.toRadians(defaultRotation.z))
		);
		rotation = totalRotation;

		if (parent != null) {
			translation.rotate(parent.displayEntity.getTransformation().getLeftRotation());
			translation.add(parent.displayEntity.getTransformation().getTranslation());
			scale.mul(parent.displayEntity.getTransformation().getScale().div(10));
		} else {
			translation.y -= (float) entity.getBaseEntity().getHeight() - 0.5;
		}

		translation.add(new Vector3f(transform[0]).div(16));
//		rotation.mul(new Quaternionf().identity().rotationXYZ(
//				(float) -Math.toRadians(transform[1].x),
//				(float) Math.toRadians(transform[1].y),
//				(float) Math.toRadians(transform[1].z)
//		));
		rotation.mul(new Quaternionf().identity()
				.rotateLocalX((float) -Math.toRadians(transform[1].x))
				.rotateLocalY((float) Math.toRadians(transform[1].y))
				.rotateLocalZ((float) Math.toRadians(transform[1].z))
		);
		scale.mul(transform[2]);

		displayEntity.setTransformation(new Transformation(
				translation,
				rotation,
				scale.mul(10),
				new Quaternionf().identity()
		));
	}
}
