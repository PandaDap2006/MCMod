package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.entity.EntityState;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.entity.model.EntityModel;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;
import org.bukkit.entity.LivingEntity;
import org.joml.Math;

import java.util.Map;

public class ReaperModel implements EntityModel<ReaperEntity, LivingEntity> {

	@Override
	public String modelLocation() {
		return "entity/reaper/reaper.geo";
	}

	@Override
	public String textureLocation() {
		return "entity/reaper";
	}

	@Override
	public void setupAnim(LivingEntity baseEntity, WorldEntity<ReaperEntity, LivingEntity> entity,
						  EntityState<ReaperEntity> state, Map<String, ModelPart> bones, int tick) {
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
			if (state.isWalking()) {
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
