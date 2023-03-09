package me.panda_studios.mcmod.core.register;

import me.panda_studios.mcmod.core.utils.Behavior;

import java.util.HashMap;
import java.util.Map;

public class Registry<T extends Behavior> {
	final Map<String, Behavior> registerList = new HashMap<>();

	final String id;
	final RegistryKey<T> registryKey;

	public Registry(String id, RegistryKey<T> registryKey) {
		this.id = id;
		this.registryKey = registryKey;
	}

	public static <T extends Behavior> Registry<T> create(String id, RegistryKey<T> registryKey) {
		return new Registry<>(id, registryKey);
	}

	public void register() {
		registryKey.entries.putAll((Map<String, T>) registerList);
	}

	public T register(String name, T object) {
		String id = this.id + ":" + name;
		object.name = id;
		this.registerList.put(id, object);
		object.onRegister();
		return object;
	}
}
