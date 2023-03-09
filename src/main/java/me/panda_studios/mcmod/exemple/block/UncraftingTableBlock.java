package me.panda_studios.mcmod.exemple.block;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.exemple.loottables.UncraftingTableLoot;
import me.panda_studios.mcmod.core.features.Loottable;

public class UncraftingTableBlock extends IBlock {
	public UncraftingTableBlock() {
		super(new Properties(10001).requireTool().setTier(2));
	}

	@Override
	public Loottable loottable() {
		return new UncraftingTableLoot();
	}
}
