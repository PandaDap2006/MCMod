package me.panda_studios.mcmod.core.block;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BlockListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void StopBlockBreak(BlockBreakEvent event) {
		for (WorldBlock worldBlock: WorldRegistry.Blocks.values()) {
			if (worldBlock.CollisionBlocks.containsValue(event.getBlock())) {
				worldBlock.iBlock.destroy(event.getPlayer(), worldBlock);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void BlockBreakStart(BlockDamageEvent event) {
		event.setCancelled(true);
		for (WorldBlock worldBlock: WorldRegistry.Blocks.values()) {
			if (worldBlock.CollisionBlocks.containsValue(event.getBlock())) {
				worldBlock.miningTicker = new WorldBlock.MiningTicker(worldBlock, event.getPlayer(), event.getBlock().getLocation());
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void BlockBreakStop(BlockDamageAbortEvent event) {
		for (WorldBlock worldBlock: WorldRegistry.Blocks.values()) {
			if (worldBlock.CollisionBlocks.containsValue(event.getBlock())) {
				if (worldBlock.miningTicker != null) {
					worldBlock.miningTicker.cancel();
					worldBlock.miningTicker = null;
				}
				break;
			}
		}
	}

//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onStart(PluginEnableEvent event) {
//		Mcmod.protocolManager.addPacketListener(new PacketAdapter(Mcmod.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
//			@Override
//			public void onPacketReceiving(PacketEvent event) {
//				EnumWrappers.PlayerDigType type = event.getPacket().getPlayerDigTypes().read(0);
//				Block block = event.getPacket().getBlockPositionModifier().read(0).toLocation(event.getPlayer().getWorld()).getBlock();
//				for (WorldBlock worldBlock: WorldRegistry.Blocks.values()) {
//					if (worldBlock.CollisionBlocks.containsValue(block)) {
//						switch (type.toString()) {
//							case "START_DESTROY_BLOCK" -> worldBlock.entityMining = event.getPlayer();
//							case "ABORT_DESTROY_BLOCK", "STOP_DESTROY_BLOCK" -> {
//								worldBlock.entityMining = null;
//								worldBlock.iBlock.stopMining(event.getPlayer(), worldBlock);
//							}
//						}
//						break;
//					}
//				}
//			}
//		});
//	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void blockInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.OFF_HAND) {
			for (WorldBlock worldBlock: WorldRegistry.Blocks.values()) {
				if (worldBlock.CollisionBlocks.containsValue(event.getClickedBlock())) {
					worldBlock.iBlock.use(event.getPlayer(), worldBlock);
					break;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void tryBlockPlace(BlockCanBuildEvent event) {
		for (WorldBlock worldBlock: WorldRegistry.Blocks.values()) {
			Block target = event.getPlayer().getTargetBlockExact(6);
			if ((target != null && worldBlock.CollisionBlocks.containsValue(target)) && !event.getPlayer().isSneaking()) {
				event.setBuildable(false);
				worldBlock.iBlock.use(event.getPlayer(), worldBlock);
				break;
			}
		}
	}
}
