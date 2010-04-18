/**
 * Connects to device's accelerometer if available and retrieves data.
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.content.Context;
import android.hardware.*;
import android.view.OrientationEventListener;
import android.util.Log;

/**
 * @author Georg
 *
 */
public class Accelerometer extends OrientationEventListener{
	
	private int orientation = 0;
	private boolean isOrientationAvailable;
	
	public Accelerometer (Context context) {
		super(context, SensorManager.SENSOR_DELAY_GAME);
		
		isOrientationAvailable = this.canDetectOrientation();
		
		Log.i("orientation", "CONSTRUCTED!");
		Log.i("orientation", "is orientation available: " + isOrientationAvailable);
		
		this.enable();
	}
	
	public void onOrientationChanged(int orientation) {
		//Log.i("orientation", orientation + "¡");
		this.orientation = orientation;
	}

	/**
	 * @return the orientation in degrees
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * @return the isOrientationAvailable
	 */
	public boolean isOrientationAvailable() {
		return isOrientationAvailable;
	}
}
