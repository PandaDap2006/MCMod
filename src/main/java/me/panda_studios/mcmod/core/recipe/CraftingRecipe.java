package me.panda_studios.mcmod.core.recipe;

import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class CraftingRecipe extends Recipe {
	final ItemStack[] items;
	final int height;
	final int width;

	public CraftingRecipe(int height, int width, ItemStack... itemStacks) {
		this.height = height;
		this.width = width;
		this.items = itemStacks;
	}

	protected boolean matches(ItemStack[] object) {
		int height = 0;
		int width = 0;
		int offsetX = 0;
		int offsetY = 0;
		int size = (int) Math.sqrt(object.length);
		boolean canOffset = true;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (canOffset) {
					offsetY = i;
					offsetX = j;
				}
				if (object[(i*size)+j] != null) {
					if (j+1 > width)
						width = j+1;
					if (i+1 > height)
						height = i+1;
					canOffset = false;
					if (j < offsetX) {
						offsetX = j;
					}
					if (i < offsetY) {
						offsetY = i;
					}
				}
			}
		}
		int newHeight = height-offsetY;
		int newWidth = width-offsetX;
		ItemStack[] match = new ItemStack[newHeight*newWidth];
		for (int i = 0; i < newHeight; i++) {
			for (int j = 0; j < newWidth; j++) {
				ItemStack item = object[((i+offsetY)*size)+(j+offsetX)];
				if (item != null) {
					item = item.clone();
					if (this.items.length > (i*newWidth)+j)
						item.setAmount(this.items[(i*newWidth)+j].getAmount());
				} else {
					item = null;
				}
				match[(i*newWidth)+j] = item;
			}
		}
		return Arrays.equals(this.items, match);
	}
}
