package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import android.util.Log;

public class UpdateTask implements Runnable {

	static final int FRAMES_PER_SECOND = 10;
	static final int FRAME_DT   = 1000 / FRAMES_PER_SECOND;
	boolean isRunning;
	
	
	Geometry geom;
	
	public UpdateTask(Geometry geom) {
		this.geom = geom;
	}
	
	@Override
	public void run() {
		isRunning = true;
		
		float dt = 0.0f;
		long  milTime = 0;
		long  endTime = 0;
		long  begTime = System.nanoTime();
		while (isRunning) {
			
			synchronized(geom) {
				update(dt);
				geom.notify();
			}
			
			endTime = System.nanoTime();
			milTime = (endTime - begTime);
			milTime /= 1000000;
			begTime = endTime;
			dt      = milTime / 1000.0f;
			try {
				if (FRAME_DT > milTime) {
					Log.i("UpdateTask", "wait second");
					Thread.sleep(FRAME_DT - milTime);
				}
			}
			catch (InterruptedException e) {
				Log.e("UpdateTask", e.getMessage());
			}
		}
	}
	

	private void update(float dt) {
		
		Log.i("UpdateTask", Float.toString(dt));
		geom.pos[1] += 0.1;
		if (geom.pos[1] > 1.0) {
			geom.pos[1] = -1.0f;
		}
	}
}
