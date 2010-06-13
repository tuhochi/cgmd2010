/**
 * Connects to device's accelerometer if available and retrieves data.
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;

/**
 * Abstracts a device's accelerometer. Checks if it is available at all and if it is, 
 * updates the orientation with speed of SENSOR_DELAY_FASTEST. Additionally, filtering is applied.
 * @author Georg
 */
public class Accelerometer extends OrientationEventListener{
	
	/** Number of elements within the filtering-array */
	private static final int FILTER_SIZE = 5;
	/** Filter array */
	private float[] filter = new float[FILTER_SIZE];
	/** Current sum of values that is averaged/filtered */
	private float sum = 0;
	/** Current index within the filtering array */
	private int filterIndex = 0;
	
	/** Device's orientation in degrees */
	private float orientation = 360.0f;
	/** Flag, if accelerometer/orientation is available in the device */
	private boolean isOrientationAvailable;
	
	/**
	 * Creates an interface to the accelerometer.
	 * @param context necessary to get the @link SensorManager in order to establish sensor connection.
	 */
	public Accelerometer (Context context) {
		super(context, SensorManager.SENSOR_DELAY_FASTEST);
		
		isOrientationAvailable = this.canDetectOrientation();
		
		this.enable();
	}
	
	/**
	 * Called, if orientation changes. Filters the new value by averaging with previous values and stores it. 
	 * Retrieve the current orientation via getOrientation.
	 * @param orientation recieved orientation from the device
	 */
	public void onOrientationChanged(int orientation) {
		
		//Remove old value from filter kernel.		
		sum -= filter[filterIndex];
		
		//90¡ abziehen, um den 0-durchgang auf eine 180¡ drehung des devices zu bringen. ansonsten gibt es einen
		//logischen filtering-fehler (delta zwischen den einzelnen filterwerten mšglicherweise > 180¡).
		filter[filterIndex] = orientation - 90f;
		if (filter[filterIndex] < 0) filter[filterIndex] += 360;
		
		sum += filter[filterIndex];
		filterIndex = (++filterIndex >= FILTER_SIZE) ? 0 : filterIndex;
		
		//in summe 270¡ abziehen (180 + zeile 47), um korrekte device orientierung zu erhalten.
		this.orientation = sum / (float)FILTER_SIZE - 180f;
	}

	/**
	 * Returns the current filtered device orientation in degrees.
	 * @return orientation in degrees
	 */
	public float getOrientation() {
		return orientation;
	}
	
	/**
	 * Only necessary if the device doesn't provide orientation-sensing and the value is set via some GUI element 
	 * (e.g., slider). 180 is subtracted from r since the slider used in the level ranges from 0 to 360 (there are no 
	 * negative values allowed). This results in an angle from -180 to +180.
	 * @param alpha the angle in degrees (0-360) 
	 */
	public void setOrientation(int alpha) {
		this.orientation = alpha - 180;
	}

	/**
	 * Returns whether orientation sensing is available or not.
	 * @return true, if orientation sensing is available on the device. Otherwise: false.
	 */
	public boolean isOrientationAvailable() {
		return isOrientationAvailable;
	}
}
