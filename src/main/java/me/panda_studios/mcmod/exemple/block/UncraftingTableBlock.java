package me.panda_studios.mcmod.exemple.block;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.block.WorldBlock;
import org.bukkit.entity.Entity;

public class UncraftingTableBlock extends IBlock {
	public UncraftingTableBlock() {
		super(new Properties().requireTool().setTier(2));
	}

	@Override
	public void use(Entity entity, WorldBlock block) {
	}
}
