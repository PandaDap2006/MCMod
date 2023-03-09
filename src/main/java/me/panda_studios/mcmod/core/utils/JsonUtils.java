package me.panda_studios.mcmod.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class JsonUtils {
    public static String makeLocation(Location location) {
        return "level:" + location.getWorld().getName() +
                ":X:" + location.getBlockX() + ":Y:" + location.getBlockY() + ":Z:" + location.getBlockZ();
    }

    public static Location getLocation(String location) {
        String[] string = location.split(":");
        return new Location(Bukkit.getWorld(string[1]), Integer.parseInt(string[3]), Integer.parseInt(string[5]), Integer.parseInt(string[7]));
    }
}
