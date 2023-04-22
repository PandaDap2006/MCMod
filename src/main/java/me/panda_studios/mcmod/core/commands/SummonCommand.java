package me.panda_studios.mcmod.core.commands;

import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.register.Registries;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummonCommand {
	public static class Command implements CommandExecutor {
		@Override
		public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
			if (args.length != 2) {
				return false;
			}
			if (Registries.ENTITY.entries.containsKey(args[1])) {
				WorldEntity.Spawn(Registries.ENTITY.entries.get(args[1]), ((Player) sender).getLocation());
			}
			return true;
		}
	}

	public static class TabComplete implements TabCompleter {
		@Override
		public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
			List<String> list = new ArrayList<>();
			switch (args.length) {
				case 1 -> Bukkit.getOnlinePlayers().forEach(o -> list.add(o.getName()));
				case 2 -> list.addAll(Registries.ENTITY.entries.keySet());
			}
			Collections.sort(list);
			return list.stream().filter(str -> str.contains(args[args.length-1])).toList();
		}
	}
}
