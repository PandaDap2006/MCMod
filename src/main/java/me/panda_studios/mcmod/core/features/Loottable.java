package me.panda_studios.mcmod.core.features;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Loottable {
	public List<ItemPool> itemPools = new ArrayList<>();

	public Loottable() {

	}

	public List<ItemStack> GenerateLoot() {
		Random random = new Random();
		List<ItemStack> itemStacks = new ArrayList<>();
		for (ItemPool pool: this.itemPools) {
			for (int i = 0; i < (random.nextInt(pool.maxRolls - pool.minRolls) + pool.minRolls); i++) {
				int maxWeight = 0;
				for (ItemEntry entry: pool.entries) {
					maxWeight += entry.weight;
				}
				int weight = random.nextInt(maxWeight);
				for (ItemEntry entry: pool.entries) {
					if (weight > entry.weight) {
						weight -= entry.weight;
					} else {
						itemStacks.add(entry.itemStack);
					}
				}
			}
		}
		return itemStacks;
	}

	public class ItemPool {
		final List<ItemEntry> entries;
		final int minRolls;
		final int maxRolls;

		public ItemPool(int rolls, ItemEntry... entries) {
			this(rolls, rolls, entries);
		}

		public ItemPool(int minRolls, int maxRolls, ItemEntry... entries) {
			this.entries = new ArrayList<>(List.of(entries));
			this.minRolls = minRolls;
			this.maxRolls = maxRolls;
		}
	}

	public static class ItemEntry {
		final ItemStack itemStack;
		final int weight;

		public ItemEntry(ItemStack itemStack, int weight) {
			this.itemStack = itemStack;
			this.weight = weight;
		}
	}
}
