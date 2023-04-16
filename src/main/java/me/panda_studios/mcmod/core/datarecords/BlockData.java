package me.panda_studios.mcmod.core.datarecords;

import me.panda_studios.mcmod.core.block.WorldBlock;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.UUID;

public record BlockData(
		String version,
		UUID uuid,
		String name,
		WorldBlock.BlockLocation location,
		Collection<WorldBlock.BlockLocation> collisions
) {
	public static final String CurrentVersion = "1.0";
}
