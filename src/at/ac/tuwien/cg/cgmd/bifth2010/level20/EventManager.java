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
	
	/** Event dispatched when an Animator finished its animation. */
	public static final int ANIMATION_COMPLETE = 1;
	/** Event dispatched when a product was collected. */
	public static final int PRODUCT_COLLECTED = 2;
	/** Event dispatched when a new shopping cart was picked up. */
	public static final int SHOPPING_CART_COLLECTED = 3;
	/** Event dispatched when the bunny crashed into an obstacle. */
	public static final int OBSTACLE_CRASH = 4;
	/** Event dispatched when the bunny continues running after a crash. */
	public static final int BUNNY_RUN = 5;
	/** Event dispatched when an obstacle was avoided. */
	public static final int OBSTACLE_AVOIDED = 6;	
	/** Event dispatched when a product fell into the shopping cart. */
	public static final int PRODUCT_HIT_CART = 7;	
	/** Event dispatched when a quantity of a product requiring a discount was collected. */
	public static final int DISCOUNT_ACQUIRED = 8;	
	/** Event dispatched when the bunny is at its most left position. */
	public static final int BUNNY_MOST_LEFT = 9;
	
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
	public void dispatchEvent(int eventId, Object eventData) {
		ListIterator<EventListener> itr = listeners.listIterator();
		while(itr.hasNext()) {
			itr.next().handleEvent(eventId, eventData);
		}
	}
	
}
