package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class RenderManager implements Renderer {

	private Activity context;
	private List<Model> models;
	private long lastTime = 0;
	float rotation = 4.0f;
	
	private TextView tfFps;
	private int fps = 0;
	
	/** Handler for FPS timer */
	private Handler updateFps = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfFps.setText(fps + " FPS");
			fps = 0;
		}
	};
	
	public RenderManager(Activity context, List<Model> models) {
		this.context = context;
		this.models = models;
		this.tfFps = (TextView) context.findViewById(R.id.l84_TfFps);

		Timer fpsUpdateTimer = new Timer();
		fpsUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateFps.sendEmptyMessage(0);
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
		double deltaTime = (double)(currentTime - lastTime) / 1000.0;
		lastTime = time.getTime();
		
		fps++;
		
		gl.glLoadIdentity();
		
		ListIterator<Model> i = models.listIterator();
		while(i.hasNext()) {
			Model m = i.next();
			m.update(gl, deltaTime);
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

		//Load textures of all available models.
		ListIterator<Model> i = models.listIterator();
		while(i.hasNext())
			i.next().loadGLTexture(gl, (Context)this.context);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}
}