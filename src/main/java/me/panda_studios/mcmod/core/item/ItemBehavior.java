package me.panda_studios.mcmod.core.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.datarecords.ItemData;
import me.panda_studios.mcmod.core.item.itemtypes.*;
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

import java.util.HashMap;

public class ItemBehavior extends Behavior {
	public final Properties properties;

	public ItemBehavior(Properties properties) {
		this.properties = properties;
	}

	public ItemStack getItem() {
		return getItem(1);
	}

	public ItemStack getItem(int amount) {
		Material material;
		int modelData;

		if (this instanceof SwordItem) {
			material = Material.WOODEN_SWORD;
			modelData = ResourceManager.swordBaseItem.modelIDs.get(name);
		} else if (this instanceof AxeItem) {
			material = Material.WOODEN_AXE;
			modelData = ResourceManager.axeBaseItem.modelIDs.get(name);
		} else if (this instanceof PickaxeItem) {
			material = Material.WOODEN_PICKAXE;
			modelData = ResourceManager.pickaxeBaseItem.modelIDs.get(name);
		} else if (this instanceof ShovelItem) {
			material = Material.WOODEN_SHOVEL;
			modelData = ResourceManager.shovelBaseItem.modelIDs.get(name);
		} else if (this instanceof HoeItem) {
			material = Material.WOODEN_HOE;
			modelData = ResourceManager.hoeBaseItem.modelIDs.get(name);
		} else {
			material = Material.PAPER;
			modelData = ResourceManager.modelBaseItem.modelIDs.get(name);
		}

		ItemStack itemStack = new ItemStack(material, amount);
		ItemMeta meta = itemStack.getItemMeta();
		if (this instanceof IItem iItem)
			meta = iItem.itemMeta(meta.clone());

		String[] namespace = this.name.split(":");
		TranslatableComponent translatable = Component.translatable(namespace[0] + ".item." + namespace[1],
				Style.style()
						.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
						.build()
		);
		meta.displayName(translatable);
		meta.setCustomModelData(modelData);

		itemStack.setItemMeta(meta);
		IItem.saveData(itemStack, new ItemData(
				ItemData.CurrentVersion,
				this.name
		));
		return itemStack;
	}

	public static class Properties {
		public int StackSize = 64;

		public Properties() {
		}

		public Properties setStackSize(int stackSize) {
			this.StackSize = stackSize;
			return this;
		}
	}
}
