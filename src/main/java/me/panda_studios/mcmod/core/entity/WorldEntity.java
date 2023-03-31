package me.panda_studios.mcmod.core.entity;

import me.panda_studios.mcmod.Mcmod;
import me.panda_studios.mcmod.core.entity.model.ModelPart;
import me.panda_studios.mcmod.core.utils.Mth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.joml.Vector3d;
import org.joml.Vector3i;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public class WorldEntity {
	public final Map<String, ModelPart> modelparts = new HashMap<>();

	private final UUID entityBase;
	private final IEntity iEntity;
	private Vector3d position;
	private Vector3d velocity = new Vector3d();
	private boolean grounded = false;

	public WorldEntity(IEntity iEntity, Location location) {
		this.iEntity = iEntity;
		this.velocity.y = 0.57;
		position = new Vector3d(location.getX(), location.getY()-.1, location.getZ());
		Interaction entity = location.getWorld().spawn(location, Interaction.class, e -> {
			e.addScoreboardTag("mcmod:entity");
		});
		this.entityBase = entity.getUniqueId();

		this.ticks();
	}

	private void ticks() {
		new BukkitRunnable() {
			@Override
			public void run() {
				Entity entity = Bukkit.getEntity(WorldEntity.this.entityBase);
				if (entity != null && entity.getLocation().getChunk().isLoaded()) {
					movementUpdate();
				}

			}
		}.runTaskTimer(Mcmod.plugin, 0, 0);
	}

	private void movementUpdate() {
		Entity entity = Bukkit.getEntity(this.entityBase);

		Player player = Bukkit.getOnlinePlayers().stream().toList().get(0);
		Location location = player.getLocation();

		Vector3d target = new Vector3d(location.getX()+2, 0, location.getZ());
		Vector3d vec = new Vector3d(this.position);
		vec.y = 0;

		vec = target.sub(vec);
		vec.mul(0.1);
		this.velocity.x = Mth.clamp(vec.x, -1, 1);
		this.velocity.z = Mth.clamp(vec.z, -1, 1);

		if (this.iEntity.attribute().has(Attribute.gravity)) {
			this.velocity.y -= this.iEntity.attribute().getAsDouble(Attribute.gravity);
			this.velocity.y = Math.max(this.velocity.y, -.99);
		}

		Block block = entity.getLocation().getBlock();
		if (entity.getBoundingBox().overlaps(block.getBoundingBox()) && block.getType().isSolid()) {
			this.velocity.y = 0;
			this.position.y = block.getBoundingBox().getMaxY()-.01;
		}

		this.position.add(this.velocity);
		entity.teleport(new Location(entity.getWorld(), this.position.x, this.position.y, this.position.z));
	}
}
