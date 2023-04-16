package me.panda_studios.mcmod.core.datarecords;

import me.panda_studios.mcmod.core.block.WorldBlock;

import java.util.Collection;
import java.util.UUID;

public record ItemData(
		String version,
		String name
) {
	public static final String CurrentVersion = "1.0";
}
