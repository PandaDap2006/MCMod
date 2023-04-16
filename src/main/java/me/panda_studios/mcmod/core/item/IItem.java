package me.panda_studios.mcmod.core.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.register.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class IItem extends ItemBehavior {
	public static NamespacedKey DataNamespace = new NamespacedKey(Mcmod.plugin, "item.data");

	public IItem(Properties properties) {
		super(properties);
	}

	public void use(Player player, ItemStack itemStack) {
	}

	public void useOn(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
	}

	public static IItem getItemFromItemStack(ItemStack itemStack) {
		Gson gson = new Gson();
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(DataNamespace)) {
			JsonObject jsonObject = gson.fromJson(itemStack.getItemMeta()
					.getPersistentDataContainer().get(DataNamespace, PersistentDataType.STRING), JsonObject.class);
			return Registries.ITEM.entries.get(jsonObject.get("name").getAsString());
		}
		return null;
	}
}
