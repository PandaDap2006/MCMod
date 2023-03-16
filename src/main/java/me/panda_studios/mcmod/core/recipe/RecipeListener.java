package me.panda_studios.mcmod.core.recipe;

import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeListener implements Listener {
	@EventHandler
	public void onCraftingGridUpdate(final PrepareItemCraftEvent event) {
		Optional<Recipe> output = Registries.RECIPE.entries.values().stream().filter((recipe) ->
				recipe instanceof CraftingRecipe && ((CraftingRecipe) recipe).matches(event.getInventory().getMatrix())).findFirst();
		output.ifPresentOrElse(recipe -> event.getInventory().setResult(recipe.output()), () -> {
			org.bukkit.inventory.Recipe recipe = Bukkit.getCraftingRecipe(event.getInventory().getMatrix(), event.getViewers().get(0).getWorld());
			if (recipe != null) {
				event.getInventory().setResult(recipe.getResult());
			} else {
				event.getInventory().setResult(null);
			}
		});
	}

	@EventHandler
	public void onCraftingCreate(final InventoryClickEvent event) {
		if (event.getClickedInventory() != null && (event.getClickedInventory().getType() == InventoryType.WORKBENCH ||
				event.getClickedInventory().getType() == InventoryType.CRAFTING) && event.getSlot() == 0) {
			CraftingInventory craftingInventory = (CraftingInventory) event.getClickedInventory();

			if (craftingInventory.getResult() != null) {
				event.setCancelled(true);
				switch (event.getAction()) {
					case PICKUP_ALL, PICKUP_HALF, PICKUP_SOME, PICKUP_ONE -> {
						if (event.getCursor().isSimilar(craftingInventory.getResult())) {
							event.getCursor().setAmount(event.getCursor().getAmount() + craftingInventory.getResult().getAmount());
						} else {
							event.setCursor(craftingInventory.getResult().clone());
						}
						for (ItemStack itemstack : craftingInventory.getMatrix()) {
							if (itemstack != null)
								itemstack.setAmount(itemstack.getAmount()-1);
						}
					}
					case MOVE_TO_OTHER_INVENTORY -> {
						ItemStack itemStack = craftingInventory.getResult().clone();
						while (itemStack == craftingInventory.getResult() || craftingInventory.getResult() != null ||
								InventoryUtils.hasSpace(event.getView().getBottomInventory().getContents(), craftingInventory.getResult())) {
							event.getView().getBottomInventory().addItem(itemStack);
							for (ItemStack itemstack : craftingInventory.getMatrix()) {
								if (itemstack != null)
									itemstack.setAmount(itemstack.getAmount()-1);
							}
							Bukkit.getPluginManager().callEvent(new PrepareItemCraftEvent(craftingInventory, event.getView(), false));
						}
					}
				}
				Bukkit.getPluginManager().callEvent(new PrepareItemCraftEvent(craftingInventory, event.getView(), false));
			}
		}
	}
}
