package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.entity.EntityModel;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;

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
	public String animationLocation(IEntity entity) {
		return null;
	}
}
