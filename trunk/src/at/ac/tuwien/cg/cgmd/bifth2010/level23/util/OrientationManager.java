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
	
	enum Side { 
		LEFT, RIGHT, TOP, BOTTOM;
		}
	
	// create SensorEventListener, which listens to the events from the orientation sensor
	
	private static SensorEventListener orientationSensorEventListener = new SensorEventListener() {
		private Side currentSide = null; 
		private Side oldSide = null; 
		private float roll; 
		
		
		// required method dummies
		public void onAccuracyChanged(int sensor) {
			
		}
		
		// 0: azimuth, 1: pitch, 2: roll
		public void onSensorChanged(SensorEvent evt) {
			roll = evt.values[2]; 
			Log.i("roll:",String.valueOf(roll));
			// roll angle not clear, has to be tested
			if (roll > 45)
				currentSide = Side.RIGHT; 
			else if (roll < -45) 
				currentSide = Side.LEFT; 
			
			// call listener dependent of which side is up 
			if (currentSide != null && !currentSide.equals(oldSide)) {
				switch(currentSide) {
				case LEFT: orientationListener.onRollLeft(); 
					break; 
				case RIGHT: orientationListener.onRollRight(); 
					break; 
				}
				
				oldSide = currentSide; 
				
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};

	// start listener
	public static void registerListener(OrientationListener listener) {
		List<Sensor> supportedSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		sensorManager = (SensorManager)LevelActivity.getContext().getSystemService(Context.SENSOR_SERVICE);
		if (supportedSensors.size() > 0)
			isSupported = true; 
		else
			isSupported = false; 
		
		//if not supported by device
		if (supportedSensors.size() <= 0)
			return; 
		
		orientationSensor = supportedSensors.get(0); 
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
