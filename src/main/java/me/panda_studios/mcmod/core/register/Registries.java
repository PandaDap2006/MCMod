package me.panda_studios.mcmod.core.register;

import me.panda_studios.mcmod.core.block.IBlock;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.item.IItem;

public class Registries {
	public static final RegistryKey<IItem> ITEM = new RegistryKey<>();
	public static final RegistryKey<IBlock> BLOCK = new RegistryKey<>();
	public static final RegistryKey<IEntity> ENTITY = new RegistryKey<>();
	public static final RegistryKey<Gui> GUI = new RegistryKey<>();
}
