package me.panda_studios.mcmod.core.entity;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
	private final Map<String, Number> attributesMap = new HashMap<>();

	public Attributes add(EntityAttribute attribute) {
		this.attributesMap.put(attribute.name, attribute.defaultValue);
		return this;
	}

	public Attributes add(EntityAttribute attribute, Number value) {
		this.attributesMap.put(attribute.name, value);
		return this;
	}

	public boolean has(EntityAttribute attribute) {
		return this.attributesMap.containsKey(attribute.name);
	}

	public Number get(EntityAttribute attribute) {
		return this.attributesMap.get(attribute.name);
	}
	public double getAsDouble(EntityAttribute attribute) {
		return (Double) this.attributesMap.get(attribute.name);
	}
	public int getAsInt(EntityAttribute attribute) {
		return (Integer) this.attributesMap.get(attribute.name);
	}
}
