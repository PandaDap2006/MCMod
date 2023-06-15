package me.panda_studios.mcmod.core.animation;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.animation.model.GeoModel;
import me.panda_studios.mcmod.core.entity.IEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Armature<T> {
	protected final String name;
	private final Model<T> model;
	private final T base;
	public final GeoModel geoModel;
	public final Map<String, Joint> joints = new HashMap<>();

	private final UUID baseEntityUUID;

	public Armature(String name, UUID baseEntityUUID, Model model, T base) {
		this.baseEntityUUID = baseEntityUUID;
		this.model = model;
		this.name = name;
		this.base = base;
		this.geoModel = GeoModel.models.get(this.model.modelLocation());

		if (this.geoModel.version().equals("1.12.0")) {
			this.geoModel.bones().forEach((s, bone) -> this.joints.put(s, Joint.of(this, bone)));
			this.joints.forEach((s, joint) -> {
				if (joint.getParent() == null)
					joint.updateTransform();
			});
		}

		new BukkitRunnable() {
			int tick = 0;
			@Override
			public void run() {
				try {
					Entity entity = Armature.this.getBaseEntity();
					if (entity != null && entity.getLocation().getChunk().isLoaded()) {
						Armature.this.model.setupAnim(Armature.this.base, Armature.this, joints, tick++);
//						Armature.this.joints.forEach((s, joint) -> {
//							if (joint.positionChanged() || joint.rotationChanged() || joint.scaleChanged())
//								joint.updateTransform();
//						});
					} else if (entity == null) {
						cancel();
					}
				} catch (Exception e) {
					throw e;
				}
			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}
	public Entity getBaseEntity() {
		return Bukkit.getEntity(baseEntityUUID);
	}

	public void remove() {
		this.joints.forEach((key, value) -> {
			value.displayEntity.remove();
		});
		this.joints.clear();
	}
}
