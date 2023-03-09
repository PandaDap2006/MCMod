package me.panda_studios.mcmod.core.item.itemtypes;

import me.panda_studios.mcmod.core.item.IItem;
import me.panda_studios.mcmod.core.item.Tier;

public class TierItem extends IItem {
	public final Tier tier;

	public TierItem(Tier tier, Properties properties) {
		super(properties);
		this.tier = tier;
	}
}
