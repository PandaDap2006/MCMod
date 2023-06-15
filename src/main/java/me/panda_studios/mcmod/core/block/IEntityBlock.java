package me.panda_studios.mcmod.core.block;

import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.register.WorldRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public abstract class IEntityBlock extends IBlock {
	public IEntityBlock(Properties properties) {
		super(properties);
	}

	protected abstract IEntity TileEntity();

	@Override
	public void blockPlace(WorldBlock block) {
		new WorldEntity<>(TileEntity().clone(), Bukkit.getEntity(block.entityUUID));
	}

	@Override
	public void destroy(Entity entity, WorldBlock block) {
		super.destroy(entity, block);
		this.getTileEntity(block).remove();
	}

	public WorldEntity<?, ?> getTileEntity(WorldBlock block) {
		return WorldRegistry.Entities.get(block.entityUUID);
	}
}
