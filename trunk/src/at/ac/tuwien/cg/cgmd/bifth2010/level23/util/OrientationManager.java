package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;

public class OrientationManager {

	private static Sensor orientationSensor; 
	private static SensorManager sensorManager;
	private static OrientationListener orientationListener; 
	private static boolean isListening, isSupported; 
	private static int LEFT = 0; 
	private static int RIGHT = 1; 
	private static int currentSide = -1; 
	
	// create SensorEventListener, which listens to the events from the orientation sensor
	
	private static SensorEventListener orientationSensorEventListener = new SensorEventListener() {
	
		private float roll; 
		
		// 0: azimuth, 1: pitch, 2: roll
		public void onSensorChanged(SensorEvent evt) {
			
			roll = evt.values[2]; 
			
			Log.i("roll:",String.valueOf(roll));
			// roll angle not clear, has to be tested
			if (roll > 15)
			{
				currentSide = LEFT; 
			}
			else 
				if (roll < -15) 
				{
				currentSide = RIGHT;
				}
			
			if(roll <15 && roll > - 15)
				currentSide=-1;
			
			
			// call listener dependent of which side is up 
			if (currentSide != -1) {
				if (currentSide ==LEFT)
					orientationListener.onRollLeft();
				if (currentSide == RIGHT)
					orientationListener.onRollRight();  
			}
			else
				orientationListener.isInDeadZone();
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};


	// start listener
	public static void registerListener(OrientationListener listener) {
	
		sensorManager = (SensorManager)LevelActivity.getContext().getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> supportedSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (supportedSensors.size() > 0)
			isSupported = true; 
		else
			isSupported = false; 
		
		//if not supported by device
		if (!isSupported)
			return; 
		
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		isListening = sensorManager.registerListener(orientationSensorEventListener, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
		orientationListener = listener; 
		
	}
	
	//end listener
	public static void unregisterListener() {
		isListening = false; 
		try {
			if (sensorManager != null && orientationSensorEventListener != null)
				sensorManager.unregisterListener(orientationSensorEventListener);
			
		} catch (Throwable t) {
			// handle exception here
		}
	}
	
	public static boolean isListening() {
		return isListening; 
	}
	
	public static boolean isSupported() {
		return isSupported; 
	}
}
