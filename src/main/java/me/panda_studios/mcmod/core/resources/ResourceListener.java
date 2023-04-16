package me.panda_studios.mcmod.core.resources;

import me.panda_studios.mcmod.Mcmod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ResourceListener implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().setResourcePack(Mcmod.resourcesURL);
	}

	@EventHandler
	public void onServerLoad(ServerLoadEvent event) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.setResourcePack(Mcmod.resourcesURL);
		}
	}
}
