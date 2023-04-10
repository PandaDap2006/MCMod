package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.core.entity.model.ModelPart;

import java.util.Map;

public abstract class EntityModel {
	public abstract String modelLocation(IEntity entity);
	public abstract String textureLocation(IEntity entity);
	public void setupAnim(Map<String, ModelPart> bones, int tick) {}
}
