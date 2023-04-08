package me.panda_studios.mcmod.core.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBehavior extends Behavior {
	public final Properties properties;

	public ItemBehavior(Properties properties) {
		this.properties = properties;
	}

	public boolean is(ItemStack itemStack) {
		if (itemStack == null) {
			return false;
		}
		ItemMeta meta = itemStack.getItemMeta();
		return meta.hasCustomModelData() && meta.getCustomModelData() == ResourceManager.modelBaseItem.modelIDs.get(name);
	}

	public ItemStack getItem() {
		return getItem(1);
	}

	public ItemStack getItem(int amount) {
		ItemStack itemStack = new ItemStack(properties.material, amount);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + properties.name);
		meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(name));

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("mcmod.item.version", "1.0");
		jsonObject.addProperty("name", name);

		meta.setLocalizedName(new Gson().toJson(jsonObject));
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static class Properties {
		private final Material material;
		public final String name;

		public int StackSize = 64;

		public Properties(String name) {
			this.material = Material.PAPER;
			this.name = name;
		}

		public Properties(Material material, String name) {
			this.material = material;
			this.name = name;
		}

		public Properties setStackSize(int stackSize) {
			this.StackSize = stackSize;
			return this;
		}
	}
}
