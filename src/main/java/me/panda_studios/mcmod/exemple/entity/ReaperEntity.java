package me.panda_studios.mcmod.exemple.entity;

import me.panda_studios.mcmod.core.entity.IEntity;
import org.joml.Vector2d;

public class ReaperEntity extends IEntity {
	public ReaperEntity() {
		super(new Vector2d(.8, 1.8));
	}

	@Override
	public String ModelLocation() {
		return "entity/reaper.json";
	}
}
