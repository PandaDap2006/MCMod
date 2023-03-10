package me.panda_studios.mcmod.exemple.block;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.block.WorldBlock;
import me.panda_studios.mcmod.exemple.loottables.UncraftingTableLoot;
import me.panda_studios.mcmod.core.features.Loottable;
import me.panda_studios.mcmod.exemple.setup.GuiSetup;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class UncraftingTableBlock extends IBlock {
	public UncraftingTableBlock() {
		super(new Properties(10001).requireTool().setTier(2));
	}

	@Override
	public Loottable loottable() {
		return new UncraftingTableLoot();
	}

	@Override
	public void use(Entity entity, WorldBlock block) {
		if (entity instanceof Player) {
			GuiSetup.UNCRAFTING_TABLE_GUI.OpenMenu((Player) entity);
		}
	}
}
