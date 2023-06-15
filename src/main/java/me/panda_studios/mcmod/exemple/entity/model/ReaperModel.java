package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.animation.Armature;
import me.panda_studios.mcmod.core.animation.Joint;
import me.panda_studios.mcmod.core.animation.Model;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;
import org.bukkit.entity.LivingEntity;
import org.joml.Math;

import java.util.Map;

public class ReaperModel implements Model<ReaperEntity> {

	@Override
	public String modelLocation() {
		return "entity/reaper/reaper.geo";
	}

	@Override
	public String textureLocation() {
		return "entity/reaper";
	}

	@Override
	public void setupAnim(ReaperEntity base, Armature<ReaperEntity> armature, Map<String, Joint> joints, int tick) {
		Joint root = joints.get("root");
		Joint body = joints.get("body");
		Joint head = joints.get("head");
		Joint right_arm = joints.get("right_arm");
		Joint left_arm = joints.get("left_arm");

		root.setRotY(-((LivingEntity)armature.getBaseEntity()).getBodyYaw() + 180);

		root.setY((float) Math.sin((double) tick / 10) * 1);
		root.setRotX((float) -Math.cos((double) tick / 10) * 1 + 1);

		head.setRotX(armature.getBaseEntity().getLocation().getPitch() + (float) Math.cos((double) tick / 10) * 1 + 1);
		head.setRotY(-armature.getBaseEntity().getLocation().getYaw() + ((LivingEntity)armature.getBaseEntity()).getBodyYaw());

		if (base.reaperMagic.castingTime > 0) {
			right_arm.setRotZ(-75 + Math.cos(((float) tick)/5) * 10);
			right_arm.setRotY(0);
			left_arm.setRotZ(75 + -Math.cos(((float) tick)/5) * 10);
			left_arm.setRotY(0);
		} else {
			if (base.worldEntity.state.isWalking()) {
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
