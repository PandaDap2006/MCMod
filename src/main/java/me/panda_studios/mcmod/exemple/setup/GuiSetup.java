package me.panda_studios.mcmod.exemple.setup;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.gui.Gui;
import me.panda_studios.mcmod.core.register.Registries;
import me.panda_studios.mcmod.core.register.Registry;

public class GuiSetup {
	public static final Registry<Gui> GUIS = Registry.create(Mcmod.MODID, Registries.GUI);
}
