package me.panda_studios.mcmod.core.commands;

import me.panda_studios.mcmod.core.register.Registries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveCommand {
    public static class Command implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            if (args.length != 2) {
                return false;
            }
            if (Registries.ITEM.entries.get(args[1]) != null) {
                Bukkit.getPlayer(args[0]).getInventory().addItem(Registries.ITEM.entries.get(args[1]).getItem());
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
                case 2 -> list.addAll(Registries.ITEM.entries.keySet());
            }
            Collections.sort(list);
            return list.stream().filter(str -> str.contains(args[args.length-1])).toList();
        }
    }
}
