package me.panda_studios.mcmod.core.gui;

public class Slot extends GuiSlot<Slot> {
	public final SlotType slotType;

	public Slot(int slot, SlotType slotType) {
		super(slot);
		this.slotType = slotType;
	}
}
