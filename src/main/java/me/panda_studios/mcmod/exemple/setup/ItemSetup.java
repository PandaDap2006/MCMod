package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.item.itemtypes.RawItem;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.item.ItemBlock;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;

import static me.panda_studios.mcmod.core.item.ItemBehavior.Properties;

public class ItemSetup {
	public static final Registry<IItem> ITEMS = Registry.create(Mcmod.MODID, Registries.ITEM);

	public static final IItem ENDERITE = ITEMS.register("enderite_ingot", new RawItem(new Properties(20001, "Enderite Ingot")));
	public static final IItem UNCRAFTING_TABLE = ITEMS.register("uncrafting_table", new ItemBlock(BlockSetup.UNCRAFTING_TABLE,
			new Properties(10001, "Uncrafting Table")));
}
