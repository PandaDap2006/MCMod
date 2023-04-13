package me.panda_studios.mcmod.exemple.entity;

import me.panda_studios.mcmod.core.entity.IEntity;
import me.panda_studios.mcmod.core.entity.WorldEntity;
import me.panda_studios.mcmod.core.entity.goal.EntityMeleeAttackGoal;
import me.panda_studios.mcmod.core.entity.goal.NearestTarget;
import me.panda_studios.mcmod.exemple.entity.goals.ReaperMagic;
import me.panda_studios.mcmod.exemple.entity.model.ReaperModel;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import javax.swing.text.html.parser.Entity;

public class ReaperEntity extends IEntity {
	public ReaperMagic reaperMagic;

	public ReaperEntity() {
		super(new ReaperModel());
	}

	@Override
	public void registerGoals(Mob entity) {
		this.goals.add(0, new NearestTarget<>(entity, Player.class, 20));
		this.goals.add(1, reaperMagic = new ReaperMagic(entity));
	}
}
