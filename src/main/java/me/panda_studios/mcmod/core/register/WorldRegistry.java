package me.panda_studios.mcmod.core.register;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.block.WorldBlock;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.datarecords.EntityData;
import me.panda_studios.mcmod.core.entity.IEntity;
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
			if (entity instanceof ItemDisplay && entity.getPersistentDataContainer().has(IBlock.DataNamespace)) {
				RegisterWorldBlock((ItemDisplay) entity);
			}
			if (entity instanceof LivingEntity && entity.getPersistentDataContainer().has(IEntity.DataNamespace)) {
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
			if (entity instanceof ItemDisplay && entity.getPersistentDataContainer().has(IBlock.DataNamespace)) {
				RegisterWorldBlock((ItemDisplay) entity);
			}
			if (entity instanceof LivingEntity && entity.getPersistentDataContainer().has(IEntity.DataNamespace)) {
				RegisterWorldEntity((LivingEntity) entity);
			}
		}
	}

	public static WorldBlock RegisterWorldBlock(ItemDisplay entity) {
		if (!WorldRegistry.Blocks.containsKey(entity.getUniqueId())) {
			Bukkit.getLogger().info("Loaded and Registered Block");

			WorldRegistry.Blocks.put(entity.getUniqueId(), new WorldBlock(
					entity.getPersistentDataContainer().get(IBlock.DataNamespace, DataTypes.Block)));
		}
		return WorldRegistry.Blocks.get(entity.getUniqueId());
	}

	public static void RegisterWorldEntity(Entity entity) {
		if (!WorldRegistry.Entities.containsKey(entity.getUniqueId())) {
			Bukkit.getLogger().info("Loaded and Registered Entity");

			EntityData data = entity.getPersistentDataContainer().get(IEntity.DataNamespace, DataTypes.Entity);
			WorldRegistry.Entities.put(entity.getUniqueId(), new WorldEntity<>(data));
		}
	}
}
