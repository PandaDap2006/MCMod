package me.panda_studios.mcmod;

import me.clip.placeholderapi.PlaceholderAPI;
import me.panda_studios.mcmod.core.block.BlockListener;
import me.panda_studios.mcmod.core.commands.GiveCommand;
import me.panda_studios.mcmod.core.commands.MenuCommand;
import me.panda_studios.mcmod.core.commands.SetBlockCommand;
import me.panda_studios.mcmod.core.commands.SummonCommand;
import me.panda_studios.mcmod.core.entity.EntityListener;
import me.panda_studios.mcmod.core.gui.GuiListener;
import me.panda_studios.mcmod.core.item.ItemListener;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.resources.ResourceListener;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import me.panda_studios.mcmod.exemple.setup.BlockSetup;
import me.panda_studios.mcmod.exemple.setup.EntitySetup;
import me.panda_studios.mcmod.exemple.setup.GuiSetup;
import me.panda_studios.mcmod.exemple.setup.ItemSetup;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Mcmod extends JavaPlugin {
	public static Plugin plugin;

	public static String resourcesURL;

	@Override
	public void onEnable() {
		Mcmod.plugin = this;
		Mcmod.resourcesURL = ResourceManager.startResourcesDownload();

		this.getCommand("msummon").setExecutor(new SummonCommand.Command());
		this.getCommand("msummon").setTabCompleter(new SummonCommand.TabComplete());

		this.getCommand("msetblock").setExecutor(new SetBlockCommand.Command());
		this.getCommand("msetblock").setTabCompleter(new SetBlockCommand.TabComplete());

		this.getCommand("mgive").setExecutor(new GiveCommand.Command());
		this.getCommand("mgive").setTabCompleter(new GiveCommand.TabComplete());

		this.getCommand("menu").setExecutor(new MenuCommand.Command());
		this.getCommand("menu").setTabCompleter(new MenuCommand.TabComplete());

		this.getServer().getPluginManager().registerEvents(new ResourceListener(), this);
		this.getServer().getPluginManager().registerEvents(new WorldRegistry(), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
		this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
		this.getServer().getPluginManager().registerEvents(new GuiListener(), this);

		BlockSetup.BLOCKS.register();
		ItemSetup.ITEMS.register();
		EntitySetup.ENTITIES.register();
		GuiSetup.GUIS.register();

		Bukkit.removeRecipe(new NamespacedKey(NamespacedKey.MINECRAFT, "book"));

		ResourceManager.StartResourceCreation();
	}

	@Override
	public void onDisable() {
		ResourceManager.stopResourcesDownload();
	}

	public static List<String> makePlaceholder(Player player, String... text) {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			return PlaceholderAPI.setPlaceholders(player, Arrays.asList(text));
		} else {
			return List.of(text);
		}
	}

	public static String makePlaceholder(Player player, String text) {
		return Mcmod.makePlaceholder(player, new String[] {text}).get(0);
	}

	public static ItemStack getItemstack(String name, int amount) {
		return Registries.ITEM.entries.containsKey(name) ?
				Registries.ITEM.entries.get(name).getItem(amount) :
				new ItemStack(Objects.requireNonNull(Material.matchMaterial(name)), amount);
	}
}
