/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/**
 * Implement this to be able to receive events from the 
 * event manager.
 * 
 * @see EventManager
 * @author Pilzinho
 */
public interface EventListener {

	public void handleEvent(int eventID, Object eventData);	
}
