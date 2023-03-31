package me.panda_studios.mcmod.core.entity;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
	private final Map<String, Comparable> attributesMap = new HashMap<>();

	public Attributes add(Attribute<?> attribute) {
		this.attributesMap.put(attribute.name, attribute.defaultValue);
		return this;
	}

	public Attributes add(Attribute<Double> attribute, double value) {
		this.attributesMap.put(attribute.name, value);
		return this;
	}
	public Attributes add(Attribute<Integer> attribute, int value) {
		this.attributesMap.put(attribute.name, value);
		return this;
	}

	public boolean has(Attribute<?> attribute) {
		return this.attributesMap.containsKey(attribute.name);
	}

	public double getAsDouble(Attribute<Double> attribute) {
		return (Double) this.attributesMap.get(attribute.name);
	}
	public int getAsInt(Attribute<Integer> attribute) {
		return (Integer) this.attributesMap.get(attribute.name);
	}
}
