package me.panda_studios.mcmod.core.block;

import me.panda_studios.mcmod.core.item.itemtypes.TierItem;
import me.panda_studios.mcmod.exemple.loottables.UncraftingTableLoot;
import me.panda_studios.mcmod.core.features.Loottable;
import me.panda_studios.mcmod.core.features.ToolTypes;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.item.Tier;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public abstract class IBlock extends BlockBehavior implements Cloneable {
	public IBlock(Properties properties) {
		super(properties);
		blockHP = properties.getBlockHP();
	}

	public Vector collision() {
		return new Vector(1, 1, 1);
	}

	public void destroy(Entity entity, WorldBlock block) {
		for (Block collision: block.CollisionBlocks.values()) {
			collision.setType(Material.AIR);
		}
		Bukkit.getEntity(block.entityUUID).remove();
		block.exist = false;
		WorldRegistry.WorldBlocks.remove(block.entityUUID);
	}

	float blockHP;
	public void miningTick(Entity entity, WorldBlock block) {
		if (entity instanceof Player) {
			if (((Player) entity).getGameMode().equals(GameMode.CREATIVE)) {
				blockBreak(entity, block, false);
			} else {
				if (blockHP <= 0) {
					blockBreak(entity, block, true);
				}

				ItemStack itemStack = ((Player) entity).getInventory().getItemInMainHand();
				IItem iItem = IItem.getItemFromItemStack(itemStack);
				if (iItem instanceof TierItem) {
					if (((TierItem) iItem).tier.tier() >= properties.BlockTier) {
						blockHP -= ((TierItem) iItem).tier.miningSpeed();
					} else {
						blockHP -= ((TierItem) iItem).tier.miningSpeed() * 0.25f;
					}
				} else if (iItem == null && (ToolTypes.AXE.vanillaTools().containsKey(itemStack.getType()))) {
					Tier tier = ToolTypes.AXE.vanillaTools().get(itemStack.getType()).tier;
					if (tier.tier() >= properties.BlockTier) {
						blockHP -= tier.miningSpeed();
					} else {
						blockHP -= tier.miningSpeed() * 0.25f;
					}
				} else if (properties.BlockTier > 0) {
					blockHP -= 0.25f;
				} else {
					blockHP -= 1f;
				}
			}
		}
	}

	public void stopMining(Entity entity, WorldBlock block) {
		blockHP = properties.getBlockHP();
	}

	public void blockBreak(Entity entity, WorldBlock block, Boolean drop) {
		IItem iItem = IItem.getItemFromItemStack(((Player) entity).getInventory().getItemInMainHand());
		if (drop && this.loottable() != null)
			for (ItemStack itemStack: new UncraftingTableLoot().GenerateLoot()) {
				entity.getWorld().dropItem(block.blockLocation, itemStack);
			}
		destroy(entity, block);
	}

	public void tick(WorldBlock block) {

	}

	public void use(Entity entity, WorldBlock block) {

	}

	public Loottable loottable() {
		return null;
	}

	@Override
	public IBlock clone() {
		try {
			IBlock clone = (IBlock) super.clone();
			// TODO: copy mutable state here, so the clone can't change the internals of the original
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
