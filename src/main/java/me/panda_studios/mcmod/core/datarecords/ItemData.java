package me.panda_studios.mcmod.core.datarecords;

import me.panda_studios.mcmod.core.block.WorldBlock;
import org.bukkit.enchantments.Enchantment;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public record ItemData(
		String version,
		String name
) {
	public static final String CurrentVersion = "1.0";
}
