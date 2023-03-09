package me.panda_studios.mcmod.core.item.utils;

import me.panda_studios.mcmod.core.item.Tier;

public enum ItemTiers implements Tier {
	WOOD(60, 4, 2, 1),
	STONE(132, 5, 2, 2),
	IRON(251, 6, 2, 3),
	GOLD(33, 4, 2, 3),
	DIAMOND(1562, 7, 2, 4),
	NETHERITE(1562, 8, 2, 5);

	private final int durability;
	private final int damage;
	private final float miningSpeed;
	private final int tier;

	ItemTiers(int durability, int damage, float miningSpeed, int tier) {
		this.durability = durability;
		this.damage = damage;
		this.miningSpeed = miningSpeed;
		this.tier = tier;
	}

	@Override
	public int durability() {
		return durability;
	}

	@Override
	public int damage() {
		return damage;
	}

	@Override
	public float miningSpeed() {
		return miningSpeed;
	}

	@Override
	public int tier() {
		return tier;
	}
}
