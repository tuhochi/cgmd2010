package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


public class ShakeDetector implements SensorEventListener{

	// For shake motion detection.
	private SensorManager mSensorManager;
	private long mLastSensorUpdate = -1;
	private float mLastX, mLastY, mLastZ = 0;

	private final Context mContext;
	private final ShakeListener mShakeListener;
	private float mThreshold = 100;
	private long mShakeDetectionDuration = 1000;
	private int mNumberOfRequiredShakes = 3;
	private int mShakeCounter = 0;


	private long mLastShakeDetection =-1;

	public ShakeDetector(Context context, ShakeListener listener){
		mShakeListener = listener;
		mContext = context;
		startListening();
	}

	public ShakeDetector(Context context, ShakeListener listener, float threshold, long detectionDuration, int numberOfRequiredShakes) {
		this(context, listener);
		mThreshold  = threshold;		
		mShakeDetectionDuration = detectionDuration;
		mNumberOfRequiredShakes = numberOfRequiredShakes;
	}

	protected void pause() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
			mSensorManager = null;
		}
		//Log.d("SHAKE", "shakes: "+mShakeCounter);
	}


	public void resume() {
		mLastX = mLastY = mLastZ = 0;
		mShakeCounter = 0;
		startListening();
	}

	private void startListening(){
		mLastSensorUpdate = System.currentTimeMillis();
		if (mSensorManager == null) {
			// start motion detection
			mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
			Sensor s = mSensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
			boolean accelSupported = mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_GAME);

			if (!accelSupported) {
				// on accelerometer on this device
				mSensorManager.unregisterListener(this);
			}
		}
	}




	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();
			// only allow one update every 100ms.
			if ((curTime - mLastSensorUpdate) > 100) {
				long diffTime = (curTime - mLastSensorUpdate);
				mLastSensorUpdate = curTime;

				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];

				float fDx = (x - mLastX);
				fDx=fDx*fDx;
				float fDy = (y - mLastY);
				fDy=fDy*fDy;
				float fDz = (z - mLastZ);
				fDz=fDz*fDz;
				
				float speed = (float) (Math.sqrt(fDx + fDy + fDz) / diffTime * 1000);


				if (speed >= mThreshold) {

					Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
					v.vibrate(75);
					
					if(mShakeListener!=null){
						if(curTime  - mLastShakeDetection< mShakeDetectionDuration){
							mShakeCounter++;
							//String deb1 = "speed " + speed + ", time "+diffTime; 
							//String deb2 = "[lastx,lasty,lastz] ["+mLastX+","+mLastY+","+mLastZ+"]";
							//String deb3 = "[x,y,z] ["+x+","+y+","+z+"]";
							//Log.d("SHAKE", "Shake detected: ");
							//Log.d("SHAKE", deb1);
							//Log.d("SHAKE", deb2);
							//Log.d("SHAKE", deb3);

						} else {
							if(mShakeCounter>mNumberOfRequiredShakes){
								
								mShakeListener.onShakeDetected();
							}
							//Log.d("SHAKE", "Shakes:" + mShakeCounter);
							mLastShakeDetection = curTime;
							mShakeCounter = 0;
						}

					}
				}
				mLastX = x;
				mLastY = y;
				mLastZ = z;
			}
		}

	}


}
