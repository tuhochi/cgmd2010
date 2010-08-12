package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

/**
 * The listener interface for receiving orientation events.
 * The class that is interested in processing a orientation
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOrientationListener<code> method. When
 * the orientation event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public interface OrientationListener {
	
	/**
	 * Called when the orienation changes
	 * @param azimuth the azimuth
	 * @param pitch the pitch
	 * @param roll the roll
	 */
	public void onOrientationChanged(float azimuth, float pitch, float roll); 
	
	// Phone is rolling to the right (phone's x-axis relative to horizontal axis) 
	/**
	 * Called when the mobile rolls right
	 */
	public void onRollRight();
	
	//Phone is rolling to the left (phone's x-axis relative to horizontal axis) 
	/**
	 * Called when the mobile rolls left 
	 */
	public void onRollLeft();
	
	/**
	 * Checks if the main char is in the dead zone 
	 */
	public void isInDeadZone();

}
