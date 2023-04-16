package me.panda_studios.mcmod.core.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.datarecords.ItemData;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import me.panda_studios.mcmod.core.utils.Behavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

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
		String[] namespace = this.name.split(":");
		TranslatableComponent translatable = Component.translatable(namespace[0] + ".item." + namespace[1],
				Style.style()
						.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
						.build()
		);
		meta.displayName(translatable);
		meta.setCustomModelData(ResourceManager.modelBaseItem.modelIDs.get(name));

		meta.getPersistentDataContainer()
				.set(new NamespacedKey(Mcmod.plugin, "item.data"), DataTypes.Item, new ItemData(
						ItemData.CurrentVersion,
						this.name
				));

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static class Properties {
		private final Material material;

		public int StackSize = 64;

		public Properties() {
			this.material = Material.PAPER;
		}

		public Properties setStackSize(int stackSize) {
			this.StackSize = stackSize;
			return this;
		}
	}
}
