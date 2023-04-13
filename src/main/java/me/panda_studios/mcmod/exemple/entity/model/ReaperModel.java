package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.entity.EntityModel;
import me.panda_studios.mcmod.core.entity.EntityState;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;
import org.bukkit.entity.Mob;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.Map;

public class ReaperModel extends EntityModel<ReaperEntity> {

	@Override
	public String modelLocation() {
		return "entity/reaper/reaper.geo";
	}

	@Override
	public String textureLocation() {
		return "entity/reaper";
	}

	public void setupAnim(Mob baseEntity, WorldEntity<ReaperEntity> entity, EntityState state, Map<String, ModelPart> bones, int tick) {
		ModelPart root = bones.get("root");
		ModelPart body = bones.get("body");
		ModelPart head = bones.get("head");
		ModelPart right_arm = bones.get("right_arm");
		ModelPart left_arm = bones.get("left_arm");

		root.setRotY(-baseEntity.getBodyYaw() + 180);

		root.setY((float) Math.sin((double) tick / 10) * 1);
		root.setRotX((float) -Math.cos((double) tick / 10) * 1 + 1);

		head.setRotX(baseEntity.getLocation().getPitch() + (float) Math.cos((double) tick / 10) * 1 + 1);
		head.setRotY(-baseEntity.getLocation().getYaw() + baseEntity.getBodyYaw());

		if (entity.iEntity.reaperMagic.castingTime > 0) {
			right_arm.setRotZ(-75 + Math.cos(((float) tick)/5) * 10);
			right_arm.setRotY(0);
			left_arm.setRotZ(75 + -Math.cos(((float) tick)/5) * 10);
			left_arm.setRotY(0);
		} else {
			if (state.isMoving()) {
				right_arm.setRotY(-90);
				left_arm.setRotY(90);
			} else {
				right_arm.setRotY(0);
				left_arm.setRotY(0);
			}
			right_arm.setRotZ((float) -Math.sin((double) tick / 10) * 5 + 80);
			left_arm.setRotZ((float) Math.sin((double) tick / 10) * 5 + -80);
		}
	}
}
