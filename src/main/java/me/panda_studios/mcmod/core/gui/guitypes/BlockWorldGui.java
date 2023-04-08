package me.panda_studios.mcmod.core.gui.guitypes;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.gui.WorldGui;

public class BlockWorldGui extends WorldGui {
	public final IBlock owner;

	public BlockWorldGui(Gui gui, IBlock owner) {
		super(gui);
		this.owner = owner;
	}
}
