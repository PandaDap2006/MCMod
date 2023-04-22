package me.panda_studios.mcmod.core.entity.model;

import me.panda_studios.mcmod.core.entity.EntityState;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import org.bukkit.entity.Entity;

import java.util.Map;

public interface EntityModel<T extends IEntity, M extends Entity> {
	String modelLocation();
	String textureLocation();
	default void setupAnim(M baseEntity, WorldEntity<T, M> entity, EntityState<T> state, Map<String, ModelPart> bones, int tick) {}
}
