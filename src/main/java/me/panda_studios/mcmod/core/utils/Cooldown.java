package me.panda_studios.mcmod.core.utils;

import me.panda_studios.mcmod.Mcmod;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown {
    int cooldown = 0;

    public Cooldown() {

    }

    public void start(int ticks) {
        cooldown = ticks;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (cooldown-- < 1) {
                    cancel();
                }
            }
        }.runTaskTimer(Mcmod.plugin, 0, 1);
    }

    public boolean isOnCooldown() {
        return this.cooldown > 0;
    }
}
