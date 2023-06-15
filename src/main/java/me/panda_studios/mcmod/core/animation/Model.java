package me.panda_studios.mcmod.core.animation;

import me.panda_studios.mcmod.core.entity.IEntity;

import java.util.Map;

public interface Model<T> {
	String modelLocation();
	String textureLocation();
	default void setupAnim(T base, Armature<T> armature, Map<String, Joint> joints, int tick) {}
}
