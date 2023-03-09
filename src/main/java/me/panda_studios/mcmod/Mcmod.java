package me.panda_studios.mcmod;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.panda_studios.mcmod.core.block.BlockListener;
import me.panda_studios.mcmod.core.commands.GiveCommand;
import me.panda_studios.mcmod.core.commands.SetBlockCommand;
import me.panda_studios.mcmod.exemple.setup.BlockSetup;
import me.panda_studios.mcmod.exemple.setup.ItemSetup;
import me.panda_studios.mcmod.exemple.setup.TagSetup;
import me.panda_studios.mcmod.core.item.ItemListener;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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

		this.getServer().getPluginManager().registerEvents(new WorldRegistry(), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);

		BlockSetup.BLOCKS.register();
		ItemSetup.ITEMS.register();
		TagSetup.ITEM_TAGS.register();
	}

	@Override
	public void onDisable() {

	}
}
