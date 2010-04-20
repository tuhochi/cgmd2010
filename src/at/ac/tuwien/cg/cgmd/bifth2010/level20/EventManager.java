/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Every component can use this class to dispatch events to 
 * the connected event listeners.
 * 
 * @see EventListener
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class EventManager {
	
	// TODO: @Ferdi: Hast du dir das so gedacht? Oder Enum? Ich überlass das Design vom Eventmanager dir :P
	public static final int ANIMATION_COMPLETE = 1;
	public static final int PRODUCT_COLLECTED = 2;
	
	
	/**
	 * The singleton instance of the event manager.
	 */
	private static final EventManager instance = new EventManager();
	/**
	 * The list of EventListeners that are registered with the manager.
	 */	
	protected List<EventListener> listeners;
	
	/**
	 * The private constructor of the event manager.
	 * Use getInstance() to get hold of the manager.
	 */
	private EventManager() {
		listeners = new LinkedList<EventListener>();
	}

	/**
	 * Returns the singleton instance of the event manager.
	 * @return	An instance of the EventManager class.
	 */
	public static EventManager getInstance() {
		return instance;
	}
	
	/**
	 * Adds an EventListener to the manager.
	 * 
	 * @param listener	The listener to add.
	 * @return	True on success and false if the listener is already registered.
	 */
	public boolean addListener(EventListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes an EventListener from the manager.
	 * 
	 * @param listener	The EventListener to remove from the manager.
	 * @return	True on success and false if the listener is not in the list.
	 */
	public boolean removeListener(EventListener listener) {
		if (!listeners.contains(listener)) {
			return false;
		}
		listeners.remove(listener);
		return true;		
	}
	
	/**
	 * Dispatches the event to the registered event listeners.
	 * 
	 * @param eventId 	The unique identifier of the event.
	 * @param eventData	Data necessary for the listeners to handle the event properly.
	 */
	public void dispatchEvent(int eventID, Object eventData) {
		ListIterator<EventListener> itr = listeners.listIterator();
		while(itr.hasNext()) {
			itr.next().handleEvent(eventID, eventData);
		}
	}
	
}
