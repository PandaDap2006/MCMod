package me.panda_studios.mcmod.core.utils;

import com.google.gson.JsonObject;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.utils.JsonUtils;
import org.bukkit.NamespacedKey;
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
