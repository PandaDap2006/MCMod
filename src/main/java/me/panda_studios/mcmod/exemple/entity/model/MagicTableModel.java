package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.animation.Model;
import me.panda_studios.mcmod.core.block.IBlock;

public class MagicTableModel implements Model<IBlock> {

	@Override
	public String modelLocation() {
		return "entity/reaper/reaper.geo";
	}

	@Override
	public String textureLocation() {
		return "entity/reaper";
	}
}
