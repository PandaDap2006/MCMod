package me.panda_studios.mcmod.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Goals {
	List<PreGoal> goals = new ArrayList<>();

	public void add(int priority, EntityGoal goal) {
		this.goals.add(new PreGoal(priority, goal));
	}

	static class PreGoal {
		public final int priority;
		public final EntityGoal goal;

		public PreGoal(int priority, EntityGoal goal) {
			this.priority = priority;
			this.goal = goal;
		}
	}
}
