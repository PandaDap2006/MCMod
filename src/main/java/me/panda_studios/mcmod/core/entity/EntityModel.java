package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.core.entity.model.ModelPart;
import org.bukkit.entity.Mob;

import java.util.Map;

public abstract class EntityModel<T extends IEntity> {
	public abstract String modelLocation();
	public abstract String textureLocation();
	public void setupAnim(Mob baseEntity, WorldEntity<T> entity, EntityState state, Map<String, ModelPart> bones, int tick) {}
}
