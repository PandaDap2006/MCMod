package me.panda_studios.mcmod.core.features;

import me.panda_studios.mcmod.core.item.Tier;
import org.bukkit.Material;

public class VanillaTool {
	public final Tier tier;
	public final Material material;

	public VanillaTool(Tier tier, Material material) {
		this.tier = tier;
		this.material = material;
	}
}
