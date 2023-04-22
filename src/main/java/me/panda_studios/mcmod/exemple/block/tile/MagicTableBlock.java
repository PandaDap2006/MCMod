package me.panda_studios.mcmod.exemple.block.tile;

import me.panda_studios.mcmod.core.block.IEntityBlock;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.exemple.setup.EntitySetup;

public class MagicTableBlock extends IEntityBlock {
	public MagicTableBlock() {
		super(new Properties());
	}

	@Override
	public IEntity TileEntity() {
		return EntitySetup.MAGIC_TABLE;
	}
}
