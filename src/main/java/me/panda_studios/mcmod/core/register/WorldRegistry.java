package me.panda_studios.mcmod.core.register;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.block.WorldBlock;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.gui.WorldGui;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

import java.util.*;

public class WorldRegistry implements Listener {
	public static Map<UUID, WorldBlock> Blocks = new HashMap<>();
	public static Map<UUID, WorldEntity> Entities = new HashMap<>();
	public static Map<UUID, WorldGui> GUIS = new HashMap<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChunkLoad(EntitiesLoadEvent event) {
		List<Entity> entities = event.getEntities();
		for (Entity entity: entities) {
			if (entity instanceof ItemDisplay && entity.getScoreboardTags().contains("mcmod:block")) {
				RegisterWorldBlock((ItemDisplay) entity);
			}
			if (entity instanceof LivingEntity && entity.getScoreboardTags().contains("mcmod:entity_base")) {
				RegisterWorldEntity((LivingEntity) entity);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onStartEvent(PluginEnableEvent event) {
		List<Entity> entities = new ArrayList<>();
		for (World world: Bukkit.getWorlds()) {
			entities.addAll(world.getEntities());
		}
		for (Entity entity: entities) {
			if (entity instanceof ItemDisplay && entity.getScoreboardTags().contains("mcmod:block")) {
				RegisterWorldBlock((ItemDisplay) entity);
			}
			if (entity instanceof LivingEntity && entity.getScoreboardTags().contains("mcmod:entity_base")) {
				RegisterWorldEntity((LivingEntity) entity);
			}
		}
	}

	public static void RegisterWorldBlock(ItemDisplay entity) {
		Gson gson = new Gson();
		String json = null;
		for (String tag: entity.getScoreboardTags()) {
			String[] tags = tag.split(":");
			if (tags[0].equals("mcmod") && tags[1].equals("block_data")) {
				json = tag.replace("mcmod:block_data:", "");
			}
		}
		if (json != null && !WorldRegistry.Blocks.containsKey(entity.getUniqueId())) {
			WorldRegistry.Blocks.put(entity.getUniqueId(), new WorldBlock(gson.fromJson(json, JsonObject.class)));
			Bukkit.getLogger().info("Loaded and Registered Block");
		}
	}

	public static void RegisterWorldEntity(LivingEntity entity) {
		Gson gson = new Gson();
		String json = null;
		for (String tag: entity.getScoreboardTags()) {
			String[] tags = tag.split(":");
			if (tags[0].equals("mcmod") && tags[1].equals("entity_data")) {
				json = tag.replace("mcmod:entity_data:", "");
			}
		}
		if (json != null && !WorldRegistry.Entities.containsKey(entity.getUniqueId())) {
			WorldRegistry.Entities.put(entity.getUniqueId(), new WorldEntity(gson.fromJson(json, JsonObject.class)));
			Bukkit.getLogger().info("Loaded and Registered Entity");
		}
	}
}
