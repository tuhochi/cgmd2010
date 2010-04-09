package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;

/**
 * The Class OrientationManager handles the registration/unregistration of the orientation listener and supports a getter if the sensor is listening.
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class OrientationManager {

	/** The orientation sensor. */
	private static Sensor orientationSensor; 
	
	/** The sensor manager. */
	private static SensorManager sensorManager;
	
	/** The orientation listener. */
	private static OrientationListener orientationListener; 
	
	/** The booleans if the sensor is listening and if the sensor is supported by the mobile . */
	private static boolean isListening, isSupported; 
	
	/** The constant for LEFT. */
	private static int LEFT = 0; 
	
	/** The constant for RIGHT. */
	private static int RIGHT = 1; 
	
	/** The current side. */
	private static int currentSide = -1; 
	
	// create SensorEventListener, which listens to the events from the orientation sensor
	
	/** The orientation sensor event listener. */
	private static SensorEventListener orientationSensorEventListener = new SensorEventListener() {
	
		private float roll; 
		
		/**
		 * Called when the sensor has changed
		 * @param evt the SensorEvent delivered
		 */
		// 0: azimuth, 1: pitch, 2: roll
		public void onSensorChanged(SensorEvent evt) {
			
			roll = evt.values[2]; 
			
//			Log.i("roll:",String.valueOf(roll));
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

		/**
		 * Called when the accuracy changed
		 * @param sensor the Sensor affected by the change
		 * @param accuracy the accuracy 
		 */
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};


	// start listener
	/**
	 * Register listener.
	 *
	 * @param listener the listener registered
	 */
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
	/**
	 * Unregister listener.
	 *
	 * @param listener the listener to unregister
	 */
	public static void unregisterListener(OrientationListener listener) {
		isListening = false; 
		try {
			if (sensorManager != null && orientationSensorEventListener != null)
				sensorManager.unregisterListener(orientationSensorEventListener, orientationSensor);
			
		} catch (Throwable t) {
			// handle exception here
		}
	}
	
	/**
	 * Checks if the sensor is listening.
	 *
	 * @return true, if it is listening
	 */
	public static boolean isListening() {
		return isListening; 
	}
	
	/**
	 * Checks if the sensor is supported by the phone.
	 *
	 * @return true, if it is supported
	 */
	public static boolean isSupported() {
		return isSupported; 
	}
}
