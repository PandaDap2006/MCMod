package me.panda_studios.mcmod.core.features;

import me.panda_studios.mcmod.core.item.utils.ItemTiers;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum ToolTypes implements VanillaToolList {
	AXE(new VanillaTool(ItemTiers.WOOD, Material.WOODEN_AXE),
			new VanillaTool(ItemTiers.STONE, Material.STONE_AXE),
			new VanillaTool(ItemTiers.IRON, Material.IRON_AXE),
			new VanillaTool(ItemTiers.GOLD, Material.GOLDEN_AXE),
			new VanillaTool(ItemTiers.DIAMOND, Material.DIAMOND_AXE),
			new VanillaTool(ItemTiers.NETHERITE, Material.NETHERITE_AXE))
	;

	private final Map<Material, VanillaTool> itemTool;

	ToolTypes(VanillaTool... vanillaTools) {
		this.itemTool = new HashMap<>();
		for (VanillaTool tool: vanillaTools) {
			this.itemTool.put(tool.material, tool);
		}
	}

	@Override
	public Map<Material, VanillaTool> vanillaTools() {
		return itemTool;
	}
}
