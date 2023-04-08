package me.panda_studios.mcmod.exemple.entity;

import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.exemple.entity.model.ReaperModel;
import org.joml.Vector2d;

public class ReaperEntity extends IEntity {
	public ReaperEntity() {
		super(new ReaperModel(), new Vector2d(.8, 1.8));
	}
}
