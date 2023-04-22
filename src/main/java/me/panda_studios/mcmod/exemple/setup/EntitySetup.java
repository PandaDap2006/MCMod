package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;
import me.panda_studios.mcmod.exemple.entity.tile.MagicTableEntity;

public class EntitySetup {
	public static final Registry<IEntity> ENTITIES = Registry.create(Mcmod.plugin, Registries.ENTITY);

	public static final IEntity REAPER = ENTITIES.register("reaper", new ReaperEntity());

	public static final IEntity MAGIC_TABLE = ENTITIES.register("magic_table", new MagicTableEntity());
}
