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
	
	private int orientation = 180;
	private boolean isOrientationAvailable;
	
	public Accelerometer (Context context) {
		super(context, SensorManager.SENSOR_DELAY_FASTEST);
		
		//Doesn't really work. Emulator's canDetectOrientation results in "true" - which is wrong unfortunately.
		isOrientationAvailable = this.canDetectOrientation();
		
		//Other way to check if sensor is available.
		//Result: Won't work! Because: INFO/sensor available(253): Goldfish 3-axis Accelerometer (within the Emulator!)
		//So, we have to disable it globally.
		
		/*SensorManager sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
		for (Iterator<Sensor> i = sensors.iterator(); i.hasNext();)
			Log.i("sensor available", i.next().getName());*/
		
		Log.i("orientation", "is orientation available: " + isOrientationAvailable);
		
		this.enable();
	}
	
	public void onOrientationChanged(int orientation) {
		this.orientation = orientation - 90;
	}

	/**
	 * @return the orientation in degrees
	 */
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int r) {
		this.orientation = r;
	}

	/**
	 * @return the isOrientationAvailable
	 */
	public boolean isOrientationAvailable() {
		return isOrientationAvailable;
	}
}
