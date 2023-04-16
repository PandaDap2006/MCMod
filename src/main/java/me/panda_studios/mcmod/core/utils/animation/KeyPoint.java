package me.panda_studios.mcmod.core.utils.animation;

import org.joml.Vector2f;

public class KeyPoint {
	public InterpolationType type;
	public Vector2f position;

	public KeyPoint(InterpolationType type, Vector2f position) {
		this.type = type;
		this.position = position;
	}
}
