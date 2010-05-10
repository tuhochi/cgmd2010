package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class RenderManager implements Renderer {

	private LevelActivity activity;
	private ModelStreet street;
	private List<Model> gems;
	
	private long lastTime = 0;
	private Accelerometer accelerometer;
	
	private TextView tfFps;
	private TextView tfPoints;
	private int fps = 0;
	private ProgressManager progman;
	private SoundManager soundman;
	
	/** Handler for FPS timer */
	private Handler updateFps = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfFps.setText(fps + " FPS");
			fps = 0;
		}
	};
	
	private Handler updatePoints = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfPoints.setText("$" + progman.getActualMoney());
		}
	};
	
	public RenderManager(LevelActivity activity, ModelStreet street, List<Model> gems, Accelerometer accelerometer, ProgressManager progman, SoundManager soundManager) {
		this.activity = activity;
		this.street = street;
		this.gems = gems;
		this.accelerometer = accelerometer;
		this.progman = progman;
		this.soundman = soundManager;
		this.tfFps = (TextView) activity.findViewById(R.id.l84_TfFps);
		this.tfPoints = (TextView) activity.findViewById(R.id.l84_Points);

		Timer fpsUpdateTimer = new Timer();
		fpsUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateFps.sendEmptyMessage(0);
				updatePoints.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}
	
	
	/**
	 * Main draw method. Updates and renders all available models.
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		Date time = new Date();
		long currentTime = time.getTime();
		
		if (lastTime == 0)
			lastTime = time.getTime();
		
		double deltaTime = (double)(currentTime - lastTime) / 1000.0;
		lastTime = time.getTime();
		
		fps++;
		
		gl.glLoadIdentity();
		
		//At first: Render street with drains
		street.update(gl, deltaTime, accelerometer.getOrientation());
		street.draw(gl);
		
		//TODO: update progress if a certain amount of the street has passed 
		progman.updatePointProgress(20);

		
		//Afterwards: Render gems.
		ListIterator<Model> i = gems.listIterator();
		while(i.hasNext()) {
			ModelGem m = (ModelGem)i.next();
			m.update(gl, deltaTime,street.getStreetPos(),accelerometer.getOrientation(), progman);
			m.draw(gl);
		}
	}

	/**
	 * called when size/orientation changes   
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();	
	}

	/**
	 * called when activity is started
	 * here also the textures should be loaded 
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		progman.setProgress(0);
		
		//Load textures of all available models.
		street.loadGLTexture(gl, (Context)this.activity);
		ListIterator<Model> i = gems.listIterator();
		while(i.hasNext())
			i.next().loadGLTexture(gl, (Context)this.activity);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}
}
