package me.panda_studios.mcmod;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.panda_studios.mcmod.core.block.BlockListener;
import me.panda_studios.mcmod.core.commands.GiveCommand;
import me.panda_studios.mcmod.core.commands.MenuCommand;
import me.panda_studios.mcmod.core.commands.SetBlockCommand;
import me.panda_studios.mcmod.core.commands.SummonCommand;
import me.panda_studios.mcmod.core.entity.EntityListener;
import me.panda_studios.mcmod.core.gui.GuiListener;
import me.panda_studios.mcmod.core.item.ItemListener;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import me.panda_studios.mcmod.core.resources.ResourceLocation;
import me.panda_studios.mcmod.core.resources.ResourceManager;
import me.panda_studios.mcmod.exemple.setup.*;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.*;
import java.util.*;

public final class Mcmod extends JavaPlugin {
	public static final String MODID = "mcmod_example";
	public static Plugin plugin;
	public static ProtocolManager protocolManager;

	HttpServer server;

	@Override
	public void onEnable() {
		Mcmod.plugin = this;
		Mcmod.protocolManager = ProtocolLibrary.getProtocolManager();

		this.getCommand("msummon").setExecutor(new SummonCommand.Command());
		this.getCommand("msummon").setTabCompleter(new SummonCommand.TabComplete());

		this.getCommand("msetblock").setExecutor(new SetBlockCommand.Command());
		this.getCommand("msetblock").setTabCompleter(new SetBlockCommand.TabComplete());

		this.getCommand("mgive").setExecutor(new GiveCommand.Command());
		this.getCommand("mgive").setTabCompleter(new GiveCommand.TabComplete());

		this.getCommand("menu").setExecutor(new MenuCommand.Command());
		this.getCommand("menu").setTabCompleter(new MenuCommand.TabComplete());

		this.getServer().getPluginManager().registerEvents(new WorldRegistry(), this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
		this.getServer().getPluginManager().registerEvents(new EntityListener(), this);
		this.getServer().getPluginManager().registerEvents(new GuiListener(), this);

		BlockSetup.BLOCKS.register();
		ItemSetup.ITEMS.register();
		EntitySetup.ENTITIES.register();
		TagSetup.ITEM_TAGS.register();
		GuiSetup.GUIS.register();

		Bukkit.removeRecipe(new NamespacedKey(NamespacedKey.MINECRAFT, "book"));

		/*InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/model/entity/reaper.json");
		if (inputStream != null) {
			Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
			String json = scanner.hasNext() ? scanner.next() : "";
			Gson gson = new Gson();
			JsonObject root = gson.fromJson(json, JsonObject.class);
			System.out.println(json);
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}*/

		ResourceManager.createModelBaseItem();

		try {
			int port = 8080;
			InetSocketAddress address = new InetSocketAddress("0.0.0.0", port);
			server = HttpServer.create(address, 0);

			// Add handlers for requests
			server.createContext("/", exchange -> {
				String response = "hello";
				exchange.sendResponseHeaders(200, response.length());
				OutputStream os = exchange.getResponseBody();
				os.write(response.getBytes());
				os.close();
			});
			server.setExecutor(null);

			server.start();
			System.out.println("Server is listening on port " + port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onDisable() {
		server.stop(0);
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
}
