package me.panda_studios.mcmod.core.block;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BlockListener implements Listener {
	@EventHandler
	public void StopBlockBreak(BlockBreakEvent event) {
		for (WorldBlock worldBlock: WorldRegistry.WorldBlocks.values()) {
			if (worldBlock.CollisionBlocks.containsValue(event.getBlock())) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void onStart(PluginEnableEvent event) {
		Mcmod.protocolManager.addPacketListener(new PacketAdapter(Mcmod.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				EnumWrappers.PlayerDigType type = event.getPacket().getPlayerDigTypes().read(0);
				Block block = event.getPacket().getBlockPositionModifier().read(0).toLocation(event.getPlayer().getWorld()).getBlock();
				for (WorldBlock worldBlock: WorldRegistry.WorldBlocks.values()) {
					if (worldBlock.CollisionBlocks.containsValue(block)) {
						switch (type.toString()) {
							case "START_DESTROY_BLOCK" -> worldBlock.entityMining = event.getPlayer();
							case "ABORT_DESTROY_BLOCK", "STOP_DESTROY_BLOCK" -> {
								worldBlock.entityMining = null;
								worldBlock.iBlock.stopMining(event.getPlayer(), worldBlock);
							}
						}
						break;
					}
				}
			}
		});
	}

	@EventHandler
	public void blockInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.OFF_HAND) {
			for (WorldBlock worldBlock: WorldRegistry.WorldBlocks.values()) {
				if (worldBlock.CollisionBlocks.containsValue(event.getClickedBlock())) {
					worldBlock.iBlock.use(event.getPlayer(), worldBlock);
					break;
				}
			}
		}
	}
}
