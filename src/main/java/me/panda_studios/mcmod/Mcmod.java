package me.panda_studios.mcmod;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.panda_studios.mcmod.core.block.BlockListener;
import me.panda_studios.mcmod.core.commands.GiveCommand;
import me.panda_studios.mcmod.core.commands.MenuCommand;
import me.panda_studios.mcmod.core.commands.SetBlockCommand;
import me.panda_studios.mcmod.core.gui.GuiListener;
import me.panda_studios.mcmod.core.recipe.RecipeListener;
import me.panda_studios.mcmod.exemple.setup.BlockSetup;
import me.panda_studios.mcmod.exemple.setup.ItemSetup;
import me.panda_studios.mcmod.exemple.setup.RecipeSetup;
import me.panda_studios.mcmod.exemple.setup.TagSetup;
import me.panda_studios.mcmod.core.item.ItemListener;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public final class Mcmod extends JavaPlugin {
	public static final String MODID = "mcmod_example";
	public static Plugin plugin;
	public static ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		Mcmod.plugin = this;
		Mcmod.protocolManager = ProtocolLibrary.getProtocolManager();

		this.getCommand("msetblock").setExecutor(new SetBlockCommand.Command());
		this.getCommand("msetblock").setTabCompleter(new SetBlockCommand.TabComplete());

		this.getCommand("mgive").setExecutor(new GiveCommand.Command());
		this.getCommand("mgive").setTabCompleter(new GiveCommand.TabComplete());

		this.getCommand("menu").setExecutor(new MenuCommand.Command());
		this.getCommand("menu").setTabCompleter(new MenuCommand.TabComplete());

		this.getServer().getPluginManager().registerEvents(new WorldRegistry(), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
		this.getServer().getPluginManager().registerEvents(new GuiListener(), this);
		this.getServer().getPluginManager().registerEvents(new RecipeListener(), this);

		BlockSetup.BLOCKS.register();
		ItemSetup.ITEMS.register();
		TagSetup.ITEM_TAGS.register();
		RecipeSetup.RECIPES.register();

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			RecipeListener.getDataPath(plugin, "");
		}
	}

	@Override
	public void onDisable() {

	}
}
