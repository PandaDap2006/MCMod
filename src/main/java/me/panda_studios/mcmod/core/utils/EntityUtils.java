package me.panda_studios.mcmod.core.utils;

import me.panda_studios.mcmod.core.entity.IEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

public class EntityUtils {
	public static boolean isIEntity(Mob mob) {
		return mob.getPersistentDataContainer().has(IEntity.DataNamespace);
	}

	public static boolean isInRadiusOf(LivingEntity entity, LivingEntity target, float radius) {
		return entity.getWorld().getNearbyEntitiesByType(target.getClass(), entity.getLocation(), radius).contains(target);
	}
}
