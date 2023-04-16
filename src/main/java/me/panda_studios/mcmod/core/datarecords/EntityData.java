package me.panda_studios.mcmod.core.datarecords;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public record EntityData(
		String version,
		UUID uuid,
		String name,
		Collection<UUID> modelParts
) {
	public static final String CurrentVersion = "1.0";
}
