package me.panda_studios.mcmod.core.entity.model;

import me.panda_studios.mcmod.core.entity.EntityState;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

import java.util.Map;
import java.util.function.Consumer;

public interface EntityModel<T extends IEntity> {
	String modelLocation();
	String textureLocation();
	default void setupAnim(Mob baseEntity, WorldEntity<T> entity, EntityState<T> state, Map<String, ModelPart> bones, int tick) {}
}
