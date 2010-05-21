/**
 * Connects to device's accelerometer if available and retrieves data.
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;

/**
 * @author Georg
 *
 */
public class Accelerometer extends OrientationEventListener{
	
	private final int filterSize = 5;
	private int[] filter = new int[filterSize];
	private float sum = 0;
	private int filterIndex = 0;
	
	private float orientation = 180.0f;
	private boolean isOrientationAvailable;
	
	public Accelerometer (Context context) {
		super(context, SensorManager.SENSOR_DELAY_FASTEST);
		
		isOrientationAvailable = this.canDetectOrientation();
		
		this.enable();
	}
	
	public void onOrientationChanged(int orientation) {
		
		//Filter sensor data.
		sum -= filter[filterIndex];
		filter[filterIndex] = orientation - 270	;
		sum += filter[filterIndex];
		filterIndex = (++filterIndex >= filterSize) ? 0 : filterIndex;
		
		this.orientation = sum / (float)filterSize;
	}

	/**
	 * @return the orientation in degrees
	 */
	public float getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int r) {
		this.orientation = r - 180;
	}

	/**
	 * @return the isOrientationAvailable
	 */
	public boolean isOrientationAvailable() {
		return isOrientationAvailable;
	}
}
