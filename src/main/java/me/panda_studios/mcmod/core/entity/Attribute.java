package me.panda_studios.mcmod.core.entity;

public class Attribute<T extends Comparable<?>> {
	public static final Attribute<Double> maxSpeed = new Attribute<>("max_speed", .1d);
	public static final Attribute<Double> gravity = new Attribute<>("gravity", .078d);
	public static final Attribute<Double> maxHealth = new Attribute<>("max_health", .20d);


	public final String name;
	public final T defaultValue;

	public Attribute(String name, T defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

}
