package me.panda_studios.mcmod.exemple.entity.model;

import me.panda_studios.mcmod.core.entity.model.EntityModel;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;
import org.bukkit.entity.LivingEntity;

public class MagicTableModel implements EntityModel<ReaperEntity, LivingEntity> {

	@Override
	public String modelLocation() {
		return "entity/reaper/reaper.geo";
	}

	@Override
	public String textureLocation() {
		return "entity/reaper";
	}
}
