package me.panda_studios.mcmod.core.entity;

public class EntityAttribute {
	public static final EntityAttribute maxSpeed = new EntityAttribute("max_speed", .1d);
	public static final EntityAttribute maxHealth = new EntityAttribute("max_health", 20d);
	public static final EntityAttribute attackDamage = new EntityAttribute("attack_damage", 4d);

	public final String name;
	public final Number defaultValue;

	public EntityAttribute(String name, Number defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

}
