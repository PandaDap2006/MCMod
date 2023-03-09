package me.panda_studios.mcmod.core.commands;

import me.panda_studios.mcmod.core.block.WorldBlock;
import me.panda_studios.mcmod.core.register.Registries;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetBlockCommand {
    public static class Command implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Player player = (Player) sender;

            if (args.length != 4) {
                return false;
            }

            WorldBlock.PlaceBlock(Registries.BLOCK.entries.get(args[0]),
                    new Location(player.getWorld(),
                            args[1].equals("~") ? player.getLocation().getBlockX() : Integer.parseInt(args[1]),
                            args[2].equals("~") ? player.getLocation().getBlockY() : Integer.parseInt(args[2]),
                            args[3].equals("~") ? player.getLocation().getBlockZ() : Integer.parseInt(args[3])),
                    true);
            return true;
        }
    }

    public static class TabComplete implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Player player = (Player) sender;
            List<String> list = new ArrayList<>();
            switch (args.length) {
                case 1 -> list.addAll(Registries.BLOCK.entries.keySet());
                case 2, 3, 4 -> list.add("~");
            }
            Collections.sort(list);
            return list.stream().filter(str -> str.contains(args[args.length-1])).toList();
        }
    }
}
