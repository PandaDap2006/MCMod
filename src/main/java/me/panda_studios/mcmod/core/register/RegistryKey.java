package me.panda_studios.mcmod.core.register;

import me.panda_studios.mcmod.core.utils.Behavior;

import java.util.HashMap;
import java.util.Map;

public class RegistryKey<T extends Behavior> {
	public Map<String, T> entries = new HashMap<>();
}
