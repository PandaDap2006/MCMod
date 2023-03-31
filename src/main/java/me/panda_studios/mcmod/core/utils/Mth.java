package me.panda_studios.mcmod.core.utils;

public class Mth {
	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}
}
