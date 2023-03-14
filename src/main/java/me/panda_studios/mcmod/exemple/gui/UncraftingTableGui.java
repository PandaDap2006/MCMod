package me.panda_studios.mcmod.exemple.gui;

import me.panda_studios.mcmod.core.gui.Button;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.gui.WorldGui;
import me.panda_studios.mcmod.exemple.setup.GuiSetup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.concurrent.atomic.AtomicInteger;

public class UncraftingTableGui extends Gui {
	public UncraftingTableGui() {
		super("§fऐက§rऑUncrafting Table", 3);
		setSlotType(SlotType.INPUT, 11);
		setSlotType(SlotType.OUTPUT, 6, 7, 8, 15, 16, 17, 24, 25, 26);

		setButton("recipe_menu", 13, "Recipes");
	}

	@Override
	public void onButtonClick(InventoryClickEvent event, Button button, WorldGui worldGui) {
		switch (button.name) {
			case "recipe_menu" -> GuiSetup.RECIPES.OpenMenu((Player) event.getWhoClicked());
			case "uncraft" -> {
				ItemStack itemStack = this.getContent(SlotType.INPUT, event.getClickedInventory())[0];
				if (itemStack != null) {
					Recipe recipe = Bukkit.getRecipesFor(itemStack).get(0);
					if (recipe instanceof ShapedRecipe shapedRecipe) {
						AtomicInteger i = new AtomicInteger();
						shapedRecipe.getIngredientMap().forEach((k, v) ->
								this.setContent(SlotType.OUTPUT, event.getClickedInventory(), i.getAndIncrement(), v.clone()));
						itemStack.setAmount(itemStack.getAmount()-1);
						setContent(SlotType.INPUT, event.getClickedInventory(), 0, itemStack);
					}
				}
			}
		}
	}

	@Override
	public void onGuiClose(Player player, WorldGui worldGui) {
		ItemStack[] itemStacks = this.getContent(null, player.getOpenInventory().getTopInventory());
		for (ItemStack itemStack : itemStacks) {
			if (itemStack != null) {
				Item entity = player.getWorld().dropItem(player.getLocation().add(0, 1, 0),
						itemStack);
				entity.setVelocity(player.getLocation().getDirection().multiply(0.25));
			}
		}
	}
}
