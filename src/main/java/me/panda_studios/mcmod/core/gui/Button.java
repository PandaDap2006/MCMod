package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.core.utils.LocalType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Button {
	public final int slot;
	public final String name;
	private final String label;
	public LocalType localType = LocalType.GLOBAL;
	public int page;
	private final int[] modelData = new int[] {30001, 30001};

	public ButtonState buttonState = ButtonState.ENABLED;

	public Button(String name, String label, int slot) {
		this.slot = slot;
		this.name = name;
		this.label = label;
	}

	public Button setDesign(ButtonState buttonState, int modelData) {
		switch (buttonState) {
			case ENABLED -> this.modelData[0] = modelData;
			case DISABLED -> this.modelData[1] = modelData;
		}
		return this;
	}

	public Button local() {
		this.localType = LocalType.LOCAL;
		return this;
	}

	public Button setPage(int page) {
		this.page = page;
		return this;
	}

	public void build(Gui gui) {
		switch (this.localType) {
			case GLOBAL -> {
				for (int i = 0; i < gui.maxPage; i++) {
					gui.buttons.put(this.slot+(gui.pageSize*i), this);
				}
			}
			case LOCAL -> gui.buttons.put(this.slot+(gui.pageSize*this.page), this);
		}
	}

	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(Material.PAPER);
		ItemMeta meta = itemStack.getItemMeta();

		meta.setDisplayName((this.buttonState == ButtonState.ENABLED ? "§f" : "§7") + label);
		meta.setCustomModelData(buttonState == ButtonState.ENABLED ? modelData[0] : modelData[1]);

		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public enum ButtonState {
		ENABLED,
		DISABLED
	}
}
