package at.ac.tuwien.cg.cgmd.bifth2010.level44.observer;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.TimeManager;

public class TimeEvent implements Event {
	public String toString() {
		return TimeManager.getInstance().toString();
	}
}
