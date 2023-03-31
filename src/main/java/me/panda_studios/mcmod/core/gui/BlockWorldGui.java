package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.core.block.IBlock;

public class BlockWorldGui extends WorldGui {
	public final IBlock owner;

	public BlockWorldGui(Gui gui, IBlock owner) {
		super(gui);
		this.owner = owner;
	}
}
