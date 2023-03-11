package me.panda_studios.mcmod.core.commands;

import me.panda_studios.mcmod.core.register.Registries;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuCommand {
    public static class Command implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (args.length != 2) {
                return false;
            }
            if (Registries.GUI.entries.get(args[1]) != null) {
                Registries.GUI.entries.get(args[1]).OpenMenu((Player) sender);
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
                case 2 -> list.addAll(Registries.GUI.entries.keySet());
            }
            Collections.sort(list);
            return list.stream().filter(str -> str.contains(args[args.length-1])).toList();
        }
    }
}
