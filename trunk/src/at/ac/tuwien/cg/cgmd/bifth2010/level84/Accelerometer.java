/**
 * Connects to device's accelerometer if available and retrieves data.
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.content.Context;
import android.hardware.*;
import android.util.Log;

/**
 * @author Georg
 *
 */
public class Accelerometer implements SensorEventListener{
	
	public boolean isAccelerometerAvailable;
	public boolean isMagneticFieldAvailable;
	
	private SensorManager sm;
	
	private float accelerometerValues[];
	private float magneticFieldValues[];
	private float mDeviceOrientation[] = new float[16];
	
	public Accelerometer (Context context) throws UnsupportedOperationException {
		sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		
		for (Sensor s : sm.getSensorList(Sensor.TYPE_ALL))
			switch (s.getType()) {
				case Sensor.TYPE_ACCELEROMETER:
					isAccelerometerAvailable = true;
					sm.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
					break;
				case Sensor.TYPE_MAGNETIC_FIELD:
					isMagneticFieldAvailable = true;
					sm.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);
					break;
			}
		
        if (!isAccelerometerAvailable)
             throw new UnsupportedOperationException("Accelerometer is not available."); 
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER: accelerometerValues = event.values; break;
			case Sensor.TYPE_MAGNETIC_FIELD: magneticFieldValues = event.values; break;
		}
		
		if (accelerometerValues != null && magneticFieldValues != null)
			SensorManager.getRotationMatrix(mDeviceOrientation, null, accelerometerValues, magneticFieldValues);
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public float[] getMDeviceOrientation() {
		return mDeviceOrientation;
	}

	public void setMDeviceOrientation(float[] mDeviceOrientation) {
		this.mDeviceOrientation = mDeviceOrientation;
	}
}
