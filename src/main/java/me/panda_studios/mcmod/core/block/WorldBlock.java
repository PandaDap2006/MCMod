package me.panda_studios.mcmod.core.block;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.datarecords.BlockData;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.features.ToolTypes;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.item.Tier;
import me.panda_studios.mcmod.core.item.itemtypes.TierItem;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class WorldBlock {
	public final UUID entityUUID;
	public final Map<Location, Block> CollisionBlocks = new HashMap<>();
	public final Location blockLocation;
	public final IBlock iBlock;
	private BlockData data;

	public boolean exist = true;
	public Entity entityMining;
	public MiningTicker miningTicker = null;

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

	public WorldBlock(BlockData data) {
		this.entityUUID = data.uuid();
		this.iBlock = Registries.BLOCK.entries.get(data.name()).clone();
		this.iBlock.properties.worldBlock = this;
		this.blockLocation = new Location(
				Bukkit.getWorld(data.location().worldName()),
				data.location().position[0], data.location().position[1], data.location().position[2]
		);
		data.collisions().forEach(collision -> {
			Location location = new Location(
					Bukkit.getWorld(collision.worldName()),
					collision.position[0], collision.position[1], collision.position[2]
			);
			this.CollisionBlocks.put(location, location.getBlock());
		});
		tick();
	}
	protected WorldBlock(IBlock iBlock, Location location, List<Location> collistion) {
		this.iBlock = iBlock.clone();
		this.iBlock.properties.worldBlock = this;
		this.blockLocation = location;

		for (Location blockLoc: collistion) {
			blockLoc.getBlock().setType(Material.BARRIER);
			this.CollisionBlocks.put(blockLoc.getBlock().getLocation(), blockLoc.getBlock());
		}

		ItemDisplay blockEntity = blockLocation.getWorld().spawn(blockLocation.add(0.5, 0, 0.5), ItemDisplay.class, entity -> {
			ItemStack model = new ItemStack(Material.PAPER, 1);
			ItemMeta meta = model.getItemMeta();
			meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(iBlock.name));
			model.setItemMeta(meta);
			entity.setItemStack(model);
			entity.setTransformation(new Transformation(new Vector3f(0, 0.5f, 0), new Quaternionf(), new Vector3f(1, 1, 1), new Quaternionf()));

			entity.addScoreboardTag("mcmod:block");
		});
		this.entityUUID = blockEntity.getUniqueId();

		saveData();
		WorldBlock worldBlock = WorldRegistry.RegisterWorldBlock(blockEntity);
		worldBlock.iBlock.blockPlace(worldBlock);
	}

	public void tick() {
		WorldBlock worldBlock = this;
		new BukkitRunnable() {
			@Override
			public void run() {
				worldBlock.iBlock.tick(worldBlock);
				if (!exist) {
					cancel();
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	public void saveData() {
		List<BlockLocation> collisions = new ArrayList<>();
		this.CollisionBlocks.forEach((location, block) -> collisions.add(new BlockLocation(
				new int[] {block.getX(), block.getY(), block.getZ()},
				block.getWorld().getName()
		)));

		Location loc = this.blockLocation;
		this.data = new BlockData(
				BlockData.CurrentVersion,
				this.entityUUID,
				this.iBlock.name,
				new BlockLocation(
						new int[] {loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()},
						loc.getWorld().getName()
				),
				collisions
		);

		Entity entity = Bukkit.getEntity(entityUUID);
		entity.getPersistentDataContainer().set(IBlock.DataNamespace, DataTypes.Block, this.data);
	}

	public record BlockLocation(
			int[] position,
			String worldName
	) {}

	static class MiningTicker extends BukkitRunnable {
		private final WorldBlock worldBlock;
		private final CraftPlayer player;
		private final Location location;
		private final float maxBlockDamage;

		public MiningTicker(WorldBlock worldBlock, Player player, Location location) {
			this.maxBlockDamage = worldBlock.iBlock.properties.getBlockHP();
			this.worldBlock = worldBlock;
			this.player = (CraftPlayer) player;
			this.location = location;

			this.runTaskTimer(Mcmod.plugin, 0, 0);
		}

		float blockDamage = 0;
		@Override
		public void run() {
			if (blockDamage >= maxBlockDamage) {
				worldBlock.iBlock.blockBreak(player, worldBlock, true);
				cancel();
			}

			ItemStack itemStack = player.getInventory().getItemInMainHand();
			IItem iItem = IItem.getItemFromItemStack(itemStack);
			if (iItem instanceof TierItem) {
				Tier tier = ((TierItem) iItem).tier;
				if (tier.tier() >= worldBlock.iBlock.properties.BlockTier) {
					blockDamage += tier.miningSpeed();
				} else {
					blockDamage += tier.miningSpeed() * 0.25f;
				}
			} else if (iItem == null && (ToolTypes.AXE.vanillaTools().containsKey(itemStack.getType()))) {
				Tier tier = ToolTypes.AXE.vanillaTools().get(itemStack.getType()).tier;
				if (tier.tier() >= worldBlock.iBlock.properties.BlockTier) {
					blockDamage += tier.miningSpeed();
				} else {
					blockDamage += tier.miningSpeed() * 0.25f;
				}
			} else if (worldBlock.iBlock.properties.BlockTier > 0) {
				blockDamage += 0.25f;
			} else {
				blockDamage += 1f;
			}
		}

		@Override
		public synchronized void cancel() throws IllegalStateException {
			super.cancel();
			blockDamage = 0;
		}
	}
}
