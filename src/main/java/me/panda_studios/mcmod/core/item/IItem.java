package me.panda_studios.mcmod.core.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.datarecords.DataTypes;
import me.panda_studios.mcmod.core.datarecords.ItemData;
import me.panda_studios.mcmod.core.register.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class IItem extends ItemBehavior {
	public static NamespacedKey DataNamespace = new NamespacedKey(Mcmod.plugin, "item.data");

	public IItem(Properties properties) {
		super(properties);
	}

	public void use(Player player, ItemStack itemStack) {
	}

	public void useOn(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
	}
	public ItemMeta itemMeta(ItemMeta meta) {
		return meta;
	}

	public static IItem getItemFromItemStack(ItemStack itemStack) {
		if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(DataNamespace)) {
			return Registries.ITEM.entries.get(itemStack.getItemMeta().getPersistentDataContainer().get(DataNamespace, DataTypes.Item).name());
		}
		return null;
	}

	public static void saveData(ItemStack itemStack) {
		ItemData oldData = IItem.loadData(itemStack);
		if (oldData != null) {
			ItemData data = new ItemData(
					oldData.version(),
					oldData.name()
			);

			saveData(itemStack, data);
		}
	}

	public static void saveData(ItemStack itemStack, ItemData data) {
		itemStack.editMeta(meta -> meta.getPersistentDataContainer()
					.set(new NamespacedKey(Mcmod.plugin, "item.data"), DataTypes.Item, data));
	}

	public static ItemData loadData(ItemStack itemStack) {
		return itemStack.getItemMeta().getPersistentDataContainer().has(DataNamespace) ?
				itemStack.getItemMeta().getPersistentDataContainer().get(DataNamespace, DataTypes.Item) :
				null;
	}
}
