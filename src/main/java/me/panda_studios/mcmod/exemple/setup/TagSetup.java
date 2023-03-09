package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.features.ItemTag;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TagSetup {
    public static final Registry<ItemTag> ITEM_TAGS = Registry.create(Mcmod.MODID, Registries.ITEM_TAG);

    public static final ItemTag WOOD_PLANKS = ITEM_TAGS.register("plank", new ItemTag(
            new ItemStack(Material.OAK_PLANKS),
            new ItemStack(Material.SPRUCE_PLANKS),
            new ItemStack(Material.BIRCH_PLANKS),
            new ItemStack(Material.JUNGLE_PLANKS),
            new ItemStack(Material.ACACIA_PLANKS),
            new ItemStack(Material.DARK_OAK_PLANKS),
            new ItemStack(Material.CRIMSON_PLANKS),
            new ItemStack(Material.WARPED_PLANKS)
    ));

    public static final ItemTag COBBLESTONE = ITEM_TAGS.register("cobblestone", new ItemTag(
            new ItemStack(Material.COBBLESTONE)
    ));
}
