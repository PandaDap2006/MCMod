package me.panda_studios.mcmod.core.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.register.Registries;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IItem extends ItemBehavior {
	public IItem(Properties properties) {
		super(properties);
	}

	public void use(Player player, ItemStack itemStack) {
	}

	public void useOn(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
	}

	public static IItem getItemFromItemStack(ItemStack itemStack) {
		Gson gson = new Gson();
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLocalizedName()) {
			JsonObject jsonObject = gson.fromJson(itemStack.getItemMeta().getLocalizedName(), JsonObject.class);
			return Registries.ITEM.entries.get(jsonObject.get("name").getAsString());
		}
		return null;
	}
}
