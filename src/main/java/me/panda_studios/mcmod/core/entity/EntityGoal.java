package me.panda_studios.mcmod.core.entity;

public abstract class EntityGoal implements Cloneable {
	protected boolean isActive = false;

	public abstract boolean shouldActivate();
	public abstract void tick();
	public void start() {}
	public void stop() {}

	@Override
	public EntityGoal clone() {
		try {
			return (EntityGoal) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
