package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.exemple.block.UncraftingTableBlock;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;

public class BlockSetup {
	public static final Registry<IBlock> BLOCKS = Registry.create(Mcmod.MODID, Registries.BLOCK);

	public static final IBlock UNCRAFTING_TABLE = BLOCKS.register("uncrafting_table", new UncraftingTableBlock());
}
