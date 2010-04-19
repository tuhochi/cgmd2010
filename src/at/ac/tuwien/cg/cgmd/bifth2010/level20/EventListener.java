/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/**
 * Implement this to be able to receive events from the 
 * event manager.
 * 
 * @see EventManager
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public interface EventListener {

	/**
	 * @param eventId The event id. @see EventManager
	 * @param eventData An arbitrary Object passed to the listener. 
	 */
	public void handleEvent(int eventId, Object eventData);	
}
