package me.panda_studios.mcmod.core.item;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.utils.Cooldown;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import net.minecraft.network.protocol.game.PacketPlayOutBlockBreakAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.profiling.jfr.event.PacketEvent;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import net.minecraft.util.profiling.jfr.event.PacketSentEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.properties.BlockProperties;
import net.minecraft.world.level.block.state.properties.IBlockState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.codehaus.plexus.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemListener implements Listener {
	Cooldown cooldown = new Cooldown();

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem() == null)
			 return;
		IItem iItem = IItem.getItemFromItemStack(event.getItem());
		if (iItem == null)
			return;
		Player player = event.getPlayer();
		Boolean itemInOffhand = !player.getEquipment().getItemInOffHand().getType().isAir();
		Boolean hand = itemInOffhand ? event.getHand() == EquipmentSlot.OFF_HAND : event.getHand() == EquipmentSlot.HAND;
		ItemStack itemStack = event.getItem();
		if (!cooldown.isOnCooldown()) {
			cooldown.start(2);
			if (event.getAction() == Action.RIGHT_CLICK_AIR) {
				Registries.ITEM.entries.get(iItem.name).use(player, itemStack);
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Registries.ITEM.entries.get(iItem.name).useOn(player, itemStack, event.getClickedBlock(), event.getBlockFace());
			}
		}
	}
}
