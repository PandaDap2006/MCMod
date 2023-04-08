package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.core.gui.guitypes.PlayerWorldGui;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.utils.Behavior;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Gui extends Behavior implements Cloneable {
	Map<Integer, GuiSlot<?>> slots = new HashMap<>();
	public WorldGui worldGui;

	public final String title;
	protected final int pageSize;
	protected final int maxPage;
	protected int page = 0;

	public Gui(String title, int pageSize) {
		this(title, pageSize, 1);
	}

	public Gui(String title, int pageSize, int maxPage) {
		this.title = title;
		this.pageSize = 9*pageSize;
		this.maxPage = maxPage;
	}

	public void registerButtons() {

	}

	public void OpenNewMenu(Player player) {
		this.OpenNewMenu(player, 0);
	}

	public void OpenNewMenu(Player player, int page) {
		player.closeInventory();
		if (WorldRegistry.GUIS.containsKey(player.getUniqueId())) {
			WorldRegistry.GUIS.get(player.getUniqueId()).gui.onGuiClose(player, WorldRegistry.GUIS.get(player.getUniqueId()));
		}

		WorldRegistry.GUIS.put(player.getUniqueId(), new PlayerWorldGui(this.clone(), player));
		WorldGui worldGui = WorldRegistry.GUIS.get(player.getUniqueId());
		worldGui.gui.page = page;
		worldGui.reloadSlots();
		worldGui.gui.onGuiOpen(player, worldGui);

		player.openInventory(worldGui.inventory.get(page));
	}

	public void onGuiOpen(Player player, WorldGui worldGui) {}
	public void onGuiClose(Player player, WorldGui worldGui) {}
	public void onTick(WorldGui worldGui) {}
	public Character customGuiUnicode() {return null;}

	@Override
	public Gui clone() {
		try {
			Gui clone = (Gui) super.clone();
			// TODO: copy mutable state here, so the clone can't change the internals of the original
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
