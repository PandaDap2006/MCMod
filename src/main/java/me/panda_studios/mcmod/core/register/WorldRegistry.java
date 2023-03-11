package me.panda_studios.mcmod.core.register;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.block.WorldBlock;
import me.panda_studios.mcmod.core.gui.WorldGui;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class WorldRegistry implements Listener {
	public static Map<UUID, WorldBlock> WorldBlocks = new HashMap<>();
	public static Map<UUID, WorldGui> GUIS = new HashMap<>();

	@EventHandler
	public void onChunkLoad(EntitiesLoadEvent event) {
		List<Entity> entities = event.getEntities();
		for (Entity entity: entities) {
			if (entity instanceof ArmorStand && entity.getScoreboardTags().contains("mcmod:block")) {
				RegisterWorldBlock((ArmorStand) entity);
			}
		}
	}

	@EventHandler
	public void onStartEvent(PluginEnableEvent event) {
		List<Entity> entities = new ArrayList<>();
		for (World world: Bukkit.getWorlds()) {
			entities.addAll(world.getEntities());
		}
		for (Entity entity: entities) {
			if (entity instanceof ArmorStand && entity.getScoreboardTags().contains("mcmod:block")) {
				RegisterWorldBlock((ArmorStand) entity);
			}
		}
	}

	public static void RegisterWorldBlock(ArmorStand entity) {
		Gson gson = new Gson();
		String json = null;
		for (String tag: entity.getScoreboardTags()) {
			String[] tags = tag.split(":");
			if (tags[0].equals("mcmod") && tags[1].equals("block_data")) {
				json = tag.replace("mcmod:block_data:", "");
			}
		}
		if (json != null && !WorldRegistry.WorldBlocks.containsKey(entity.getUniqueId())) {
			WorldRegistry.WorldBlocks.put(entity.getUniqueId(), new WorldBlock(gson.fromJson(json, JsonObject.class)));
			Bukkit.getLogger().info("Loaded and Registered Block");
		}
	}
}
