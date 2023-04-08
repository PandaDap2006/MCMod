package me.panda_studios.mcmod.core.register;

import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Registry<T extends Behavior> {
	final Map<String, Behavior> registerList = new HashMap<>();

	final String id;
	public final Plugin plugin;
	final RegistryKey<T> registryKey;

	public Registry(Plugin plugin, RegistryKey<T> registryKey) {
		this.id = plugin.getName().toLowerCase().replace(" ", "_");
		this.plugin = plugin;
		this.registryKey = registryKey;
	}

	public static <T extends Behavior> Registry<T> create(Plugin plugin, RegistryKey<T> registryKey) {
		return new Registry<>(plugin, registryKey);
	}

	public void register() {
		registryKey.entries.putAll((Map<String, T>) registerList);
	}

	public T register(String name, T object) {
		String id = this.id + ":" + name;
		object.name = id;
		object.plugin = plugin;
		this.registerList.put(id, object);
		object.onRegister();
		return object;
	}
}
