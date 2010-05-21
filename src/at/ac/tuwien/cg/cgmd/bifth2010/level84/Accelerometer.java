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
	private float[] filter = new float[filterSize];
	private float sum = 0;
	private float lastOrientation = Float.MAX_VALUE;
	private int filterIndex = 0;
	
	private float orientation = 360.0f;
	private boolean isOrientationAvailable;
	
	public Accelerometer (Context context) {
		super(context, SensorManager.SENSOR_DELAY_FASTEST);
		
		isOrientationAvailable = this.canDetectOrientation();
		
		this.enable();
	}
	
	public void onOrientationChanged(int orientation) {
		
		//Remove old value from filter kernel.		
		sum -= filter[filterIndex];
		
		//90¡ abziehen, um den 0-durchgang auf eine 180¡ drehung des devices zu bringen. ansonsten gibt es einen
		//logischen filtering-fehler (delta zwischen den einzelnen filterwerten mšglicherweise > 180¡).
		filter[filterIndex] = orientation - 90f;
		if (filter[filterIndex] < 0) filter[filterIndex] += 360;
		
		sum += filter[filterIndex];
		filterIndex = (++filterIndex >= filterSize) ? 0 : filterIndex;
		
		//in summe 270¡ abziehen (180 + zeile 47), um korrekte device orientierung zu erhalten.
		this.orientation = sum / (float)filterSize - 180f;
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
