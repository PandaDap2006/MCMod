package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.item.ItemBlock;
import me.panda_studios.mcmod.core.item.Tier;
import me.panda_studios.mcmod.core.item.itemtypes.*;
import me.panda_studios.mcmod.core.item.utils.ItemTiers;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;

import static me.panda_studios.mcmod.core.item.ItemBehavior.Properties;

public class ItemSetup {
	public static final Registry<IItem> ITEMS = Registry.create(Mcmod.plugin, Registries.ITEM);

	public static final IItem ENDERITE = ITEMS.register("enderite_ingot", new IItem(new Properties()));

	public static final IItem UNCRAFTING_TABLE = ITEMS.register("uncrafting_table", new ItemBlock(BlockSetup.UNCRAFTING_TABLE, new Properties()));

	public static final IItem MAGIC_TABLE = ITEMS.register("magic_table", new ItemBlock(BlockSetup.MAGIC_TABLE, new Properties()));

	public static final IItem ENDERITE_SWORD = ITEMS.register("enderite_sword", new SwordItem(ItemTiers.NETHERITE, new Properties()));
	public static final IItem ENDERITE_AXE = ITEMS.register("enderite_axe", new AxeItem(ItemTiers.NETHERITE, new Properties()));
	public static final IItem ENDERITE_PICKAXE = ITEMS.register("enderite_pickaxe", new PickaxeItem(ItemTiers.NETHERITE, new Properties()));
	public static final IItem ENDERITE_SHOVEL = ITEMS.register("enderite_shovel", new ShovelItem(ItemTiers.NETHERITE, new Properties()));
	public static final IItem ENDERITE_HOE = ITEMS.register("enderite_hoe", new HoeItem(ItemTiers.NETHERITE, new Properties()));
}
