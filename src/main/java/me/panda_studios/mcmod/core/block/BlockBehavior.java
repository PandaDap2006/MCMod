package me.panda_studios.mcmod.core.block;

import me.panda_studios.mcmod.core.utils.Behavior;

public class BlockBehavior extends Behavior {
	public final Properties properties;

	public BlockBehavior(Properties properties) {
		this.properties = properties;
	}

	public static class Properties {
		public WorldBlock worldBlock;

		float Strength = 1;
		boolean requireTool = false;
		int BlockTier = 0;

		public Properties setStrength(float strength) {
			this.Strength = strength;
			return this;
		}
		public Properties setTier(int blockTier) {
			this.BlockTier = blockTier;
			return this;
		}
		public Properties requireTool() {
			this.requireTool = true;
			return this;
		}

		public float getBlockHP() {
			float hp = 20 * Strength;
			hp = hp <= 0 ? 1 : hp;
			return hp;
		}
	}
}
