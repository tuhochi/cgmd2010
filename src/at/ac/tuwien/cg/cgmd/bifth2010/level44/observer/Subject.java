package at.ac.tuwien.cg.cgmd.bifth2010.level44.observer;

import java.util.Vector;

public class Subject {
	private Vector<Observer> observer = new Vector<Observer>();
	
	public void addObserver(Observer o) {
		observer.add(o);
	}
	
	public void removeObserver(Observer o) {
		observer.remove(o);
	}
	
	public void notifyAll(Event event) {
		for (Observer o : observer) {
			o.notify(event);
		}
	}
}
