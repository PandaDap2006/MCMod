package me.panda_studios.mcmod.core.utils;

import org.bukkit.entity.LivingEntity;

public class EntityUtils {
	public static boolean isInRadiusOf(LivingEntity entity, LivingEntity target, float radius) {
		return entity.getWorld().getNearbyEntitiesByType(target.getClass(), entity.getLocation(), radius).contains(target);
	}
}
