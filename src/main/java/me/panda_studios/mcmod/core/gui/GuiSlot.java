package me.panda_studios.mcmod.core.gui;

import me.panda_studios.mcmod.core.utils.LocalType;
import org.bukkit.entity.Player;

public abstract class GuiSlot<T extends GuiSlot<?>> implements Cloneable {
	protected final int slot;
	protected int page;
	protected LocalType localType = LocalType.GLOBAL;
	protected ActiveCondition condition = guiSlot -> true;
	protected ClickAction clickAction = (guiSlot, player) -> {};
	protected Gui parentGui;

	public GuiSlot(int slot) {
		this.slot = slot;
	}

	public T setCondition(ActiveCondition condition) {
		this.condition = condition;
		return (T) this;
	}
	public T setClickAction(ClickAction action) {
		this.clickAction = action;
		return (T) this;
	}

	public T setPage(int page) {
		this.page = page;
		this.localType = LocalType.LOCAL;
		return (T) this;
	}

	public void build(Gui gui) {
		switch (this.localType) {
			case GLOBAL -> {
				for (int i = 0; i < gui.maxPage; i++) {
					gui.slots.put(this.slot+(gui.pageSize*i), this);
				}
			}
			case LOCAL -> {
				gui.slots.put((this.slot)+(gui.pageSize*this.page), this);
			}
		}

		this.parentGui = gui;
	}

	@Override
	public T clone() {
		try {
			T clone = (T) super.clone();
			// TODO: copy mutable state here, so the clone can't change the internals of the original
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public interface ActiveCondition {
		boolean isActive(GuiSlot<?> guiSlot);
	}
	public interface ClickAction {
		void activation(GuiSlot<?> guiSlot, Player player);
	}
}
