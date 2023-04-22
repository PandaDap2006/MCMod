package me.panda_studios.mcmod.core.block;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.item.itemtypes.TierItem;
import me.panda_studios.mcmod.core.features.ToolTypes;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.item.Tier;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class IBlock extends BlockBehavior implements Cloneable {
	public static final NamespacedKey DataNamespace = new NamespacedKey(Mcmod.plugin, "block.data");

	public IBlock(Properties properties) {
		super(properties);
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
		WorldRegistry.Blocks.remove(block.entityUUID);
	}
	public void blockPlace(WorldBlock block) {}
	public void blockBreak(Entity entity, WorldBlock block, Boolean drop) {
		destroy(entity, block);
	}
	public void tick(WorldBlock block) {}
	public void use(Entity entity, WorldBlock block) {}

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
