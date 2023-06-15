package me.panda_studios.mcmod.core.animation;

import me.panda_studios.mcmod.core.animation.model.GeoModel;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Joint {
	public ItemDisplay displayEntity;
	public final Armature<?> armature;
	public final GeoModel.Bone bone;

	private Vector3f position = new Vector3f();
	private Vector3f rotation = new Vector3f();
	private Vector3f scale = new Vector3f(1);
	private Vector3f oldPosition = new Vector3f();
	private Quaternionf oldRotation = new Quaternionf().identity();
	private Vector3f oldScale = new Vector3f(1);

	private final List<GeoModel.Bone> children = new ArrayList<GeoModel.Bone>();

	public static Joint of(Armature<?> armature, GeoModel.Bone bone) {
		return new Joint(armature, bone);
	}

	public Joint(Armature<?> armature, GeoModel.Bone bone) {
		this.armature = armature;
		this.bone = bone;
		this.children.addAll(bone.children().stream().toList());

		this.displayEntity = armature.getBaseEntity().getWorld().spawn(armature.getBaseEntity().getLocation(), ItemDisplay.class, e -> {
			String modelName = armature.name + "_" + this.bone.name();

			if (ResourceManager.modelBaseItem.modelIDs.containsKey(modelName)) {
				ItemStack itemStack = new ItemStack(Material.PAPER);
				ItemMeta meta = itemStack.getItemMeta();
				meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(modelName));
				itemStack.setItemMeta(meta);
				e.setItemStack(itemStack);
			}

			e.addScoreboardTag("mcmod:entity");
			e.addScoreboardTag("mcmod:entity_modelpart");
			e.addScoreboardTag("mcmod:entity_id:" + Joint.this.armature.getBaseEntity().getUniqueId());

			e.setInterpolationDuration(1);
		});
		armature.getBaseEntity().addPassenger(displayEntity);
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
	public boolean positionChanged() {
		return this.displayEntity.getTransformation().getTranslation() != this.oldPosition;
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
	public boolean rotationChanged() {
		return this.oldRotation != new Quaternionf().identity()
				.rotateLocalX((float) -Math.toRadians(this.rotation.x))
				.rotateLocalY((float) Math.toRadians(this.rotation.y))
				.rotateLocalZ((float) Math.toRadians(this.rotation.z));
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
	public boolean scaleChanged() {
		return this.scale != this.oldScale;
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

	public void updateTransform() {
		this.displayEntity.setInterpolationDelay(0);

		Vector3f[] transform = getTransform();

		Vector3f translation = new Vector3f(this.bone.pivot()).div(16);
		Quaternionf rotation;
		Vector3f scale = new Vector3f(1);

		Quaternionf totalRotation = new Quaternionf().identity();
		if (this.getParent() != null) {
			totalRotation.mul(this.getParent().displayEntity.getTransformation().getLeftRotation());
		}
		totalRotation.mul(new Quaternionf().identity()
				.rotateLocalX((float) -Math.toRadians(this.bone.rotation().x))
				.rotateLocalY((float) Math.toRadians(this.bone.rotation().y))
				.rotateLocalZ((float) Math.toRadians(this.bone.rotation().z))
		);
		rotation = totalRotation;

		if (this.getParent() != null) {
			translation.rotate(this.getParent().displayEntity.getTransformation().getLeftRotation());
			translation.add(this.getParent().displayEntity.getTransformation().getTranslation());
			scale.mul(this.getParent().displayEntity.getTransformation().getScale().div(10));
		} else {
			translation.y -= (float) this.armature.getBaseEntity().getHeight() - 0.5;
		}

		translation.add(new Vector3f(transform[0]).div(16));
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
		this.bone.children().forEach(bone -> this.armature.joints.get(bone.name()).updateTransform());
	}

	public Joint getParent() {
		return this.armature.joints.get(this.bone.parent());
	}
}