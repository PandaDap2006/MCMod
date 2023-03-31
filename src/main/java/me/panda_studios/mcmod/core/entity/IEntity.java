package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public abstract class IEntity extends Behavior {
	public IEntity() {

	}

	public EntityType BaseEntity() {
		return EntityType.SILVERFISH;
	};
	public abstract String ModelLocation();

	public Attributes attribute() {
		return new Attributes().add(Attribute.gravity).add(Attribute.maxSpeed).add(Attribute.maxHealth);
	}
}
