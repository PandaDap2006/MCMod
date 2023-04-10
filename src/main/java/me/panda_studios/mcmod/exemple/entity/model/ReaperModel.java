package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.entity.EntityModel;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.model.ModelPart;

import java.util.Map;

public class ReaperModel extends EntityModel {

	@Override
	public String modelLocation(IEntity entity) {
		return "entity/reaper/reaper.geo";
	}

	@Override
	public String textureLocation(IEntity entity) {
		return "entity/reaper";
	}

	@Override
	public void setupAnim(Map<String, ModelPart> bones, int tick) {
		ModelPart root = bones.get("root");
		ModelPart head = bones.get("head");
		ModelPart right_arm = bones.get("right_arm");
		ModelPart left_arm = bones.get("left_arm");

		root.location.y = Math.sin((double) tick / 10) * 1;
		root.rotation.x = -Math.cos((double) tick / 10) * 1 + 1;
		head.rotation.x = Math.cos((double) tick / 10) * 1 + 1;

		right_arm.rotation.z = -Math.sin((double) tick / 10) * 5 + 80;
		left_arm.rotation.z = Math.sin((double) tick / 10) * 5 + -80;
	}
}
