package me.panda_studios.mcmod.core.block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.utils.JsonUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WorldBlock {
	public final UUID entityUUID;
	public final Map<Location, Block> CollisionBlocks = new HashMap<>();
	public final Location blockLocation;
	public final IBlock iBlock;

	public boolean exist = true;
	public Entity entityMining;

	/**
	 * Places Block
	 * @param iBlock
	 * @param location
	 * @param force
	 * @return
	 */
	public static WorldBlock PlaceBlock(IBlock iBlock, Location location, boolean force) {
		List<Location> collistion = new ArrayList<>();
		for (int x = 0; x < iBlock.collision().getBlockX(); x++) {
			for (int y = 0; y < iBlock.collision().getBlockY(); y++) {
				for (int z = 0; z < iBlock.collision().getBlockZ(); z++) {
					Location blockLoc = new Location(location.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
					if (!force) {
						if (blockLoc.getBlock().getType() != Material.AIR ||
								!blockLoc.getWorld().getNearbyEntities(blockLoc.add(0.5, 0.5, 0.5), 0.5, 0.5, 0.5).isEmpty()) {
							return null;
						}
					}
					collistion.add(blockLoc);
				}
			}
		}

		return new WorldBlock(iBlock, location, collistion);
	}

	/**
	 * Loads Block from JSON
	 * @param blockData
	 */
	public WorldBlock(JsonObject blockData) {
		this.entityUUID = UUID.fromString(blockData.get("uuid").getAsString());
		this.iBlock = Registries.BLOCK.entries.get(blockData.get("block_id").getAsString()).clone();
		this.iBlock.properties.worldBlock = this;
		this.blockLocation = JsonUtils.getLocation(blockData.get("location").getAsString());
		for (JsonElement jsonBoxs: blockData.getAsJsonArray("collisionBoxs")) {
			String stringLoc = jsonBoxs.getAsString();
			Location location = JsonUtils.getLocation(stringLoc);
			this.CollisionBlocks.put(location, location.getBlock());
		}
		tick();
	}

	/**
	 * Places Block
	 * @param iBlock
	 * @param location
	 * @param collistion
	 */
	private WorldBlock(IBlock iBlock, Location location, List<Location> collistion) {
		this.iBlock = iBlock.clone();
		this.iBlock.properties.worldBlock = this;
		this.blockLocation = location;

		for (Location blockLoc: collistion) {
			blockLoc.getBlock().setType(Material.BARRIER);
			this.CollisionBlocks.put(blockLoc.getBlock().getLocation(), blockLoc.getBlock());
		}

		ArmorStand blockEntity = blockLocation.getWorld().spawn(blockLocation.add(0.5, 0, 0.5), ArmorStand.class, entity -> {
			entity.setSmall(true);
			entity.setGravity(false);
			entity.setMarker(true);
			entity.setInvulnerable(true);
			entity.setInvisible(true);
			entity.hasEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
			entity.hasEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
			entity.hasEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
			entity.hasEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);

			ItemStack model = new ItemStack(Material.PAPER, 1);
			ItemMeta meta = model.getItemMeta();
			meta.setCustomModelData(iBlock.properties.modelID);
			model.setItemMeta(meta);
			entity.getEquipment().setHelmet(model);

			entity.addScoreboardTag("mcmod:block");
			entity.addScoreboardTag("custom_block");
		});
		this.entityUUID = blockEntity.getUniqueId();

		saveData();
		WorldRegistry.RegisterWorldBlock(blockEntity);
	}

	/**
	 * Ticks
	 */
	public void tick() {
		WorldBlock worldBlock = this;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (entityMining != null) {
					worldBlock.iBlock.miningTick(entityMining, worldBlock);
				}
				worldBlock.iBlock.tick(worldBlock);
				if (!exist) {
					cancel();
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	/**
	 * Saves Data
	 */
	public void saveData() {
		JsonObject json = new JsonObject();
		json.addProperty("uuid", this.entityUUID.toString());
		json.addProperty("block_id", this.iBlock.name);
		json.addProperty("location", JsonUtils.makeLocation(blockLocation));
		JsonArray collisionArray = new JsonArray();
		for (Block block: this.CollisionBlocks.values()) {
			collisionArray.add(JsonUtils.makeLocation(block.getLocation()));
		}
		json.add("collisionBoxs", collisionArray);

		Entity entity = Bukkit.getEntity(entityUUID);
		for (String tag: entity.getScoreboardTags()) {
			if (tag.contains("mcmod:block_data:")) {
				entity.removeScoreboardTag(tag);
			}
		}
		entity.addScoreboardTag("mcmod:block_data:" + json);
	}
}
