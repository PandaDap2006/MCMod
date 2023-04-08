package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;
import me.panda_studios.mcmod.exemple.entity.ReaperEntity;

public class EntitySetup {
	public static final Registry<IEntity> ENTITIES = Registry.create(Mcmod.plugin, Registries.ENTITY);

	public static final IEntity REAPER = ENTITIES.register("reaper", new ReaperEntity());
}
