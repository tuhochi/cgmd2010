package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class RenderManager implements Renderer,OnDismissListener {

	private LevelActivity activity;
	private ModelStreet street;
	private List<Model> gems;
	
	private long lastTime = -1;
	private Accelerometer accelerometer;
	
	private TextView tfFps;
	private TextView tfPoints;
	private TextView tfPointsShadow;
	
	private int fps = 0;
	private ProgressManager progman;
	
	float streetMeter = 0;
	DecimalFormat df = new DecimalFormat("0");

	/** Handler for FPS timer */
	private Handler updateFps = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfFps.setText(fps + " FPS");
			fps = 0;
		}
	};
	
	/** Handler for points */
	private Handler updatePoints = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfPoints.setText("$" + progman.getRemainingValue());
			tfPointsShadow.setText(tfPoints.getText());
		}
	};
	
	/** Handler for gameTime */
	private Handler updateGameTime = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (street.isStreetActive()) { streetMeter --; }
			tfFps.setText(df.format(streetMeter) + "m");
		}
	};
	
	
	public RenderManager(at.ac.tuwien.cg.cgmd.bifth2010.level84.LevelActivity levelActivity, ModelStreet street, List<Model> gems, Accelerometer accelerometer, ProgressManager progman, SoundManager soundManager) {
		this.activity = levelActivity;
		this.street = street;
		this.gems = gems;
		this.accelerometer = accelerometer;
		this.progman = progman;
		this.tfFps = (TextView) levelActivity.findViewById(R.id.l84_TfFps);
		this.tfPoints = (TextView) levelActivity.findViewById(R.id.l84_Points);
		this.tfPointsShadow = (TextView) levelActivity.findViewById(R.id.l84_PointsShadow);
		
		streetMeter = street.getStreetWidth()-8f;
		
		Timer fpsUpdateTimer = new Timer();
		fpsUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateFps.sendEmptyMessage(0);
				updatePoints.sendEmptyMessage(0);
				updateGameTime.sendEmptyMessage(0);
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
		
		if (lastTime == -1)
			lastTime = time.getTime();
		
		double deltaTime = (double)(currentTime - lastTime) / 1000.0;
		lastTime = time.getTime();
		
		fps++;
		
		//TODO: lifecycle ... Timer Bug beheben.
		
		//UPDATE -------------------------
		street.update(deltaTime, accelerometer.getOrientation());
		
		checkStreetEnd(street.getStreetPos()); //if the street end is near -> call finish method to finish activity
		
		//Log.i("streetPos", "position: " + street.getStreetPos());
		
		//DRAW ---------------------------
		gl.glLoadIdentity();
		
		//At first: Render street with drains
		street.draw(gl);
		
		//Afterwards: Render gems.
		ListIterator<Model> i = gems.listIterator();
		while(i.hasNext()) {
			Model m = i.next();
			if (m instanceof ModelGem)
				((ModelGem)m).update(deltaTime, street.getStreetPos(), accelerometer.getOrientation(), progman);
			m.draw(gl);
		}
	}
	
	private void checkStreetEnd(float streetPos)
	{
		//if (streetPos < ((-street.width / 2) + 8f))
		if (streetMeter < 1)
		{
			//stop street translation
			street.stopStreet();
			
			//get money values and show resultdialog
			Message moneyvalues = new Message();
			moneyvalues.arg1 = progman.getStartValue();
			moneyvalues.arg2 = progman.getRemainingValue();
			LevelActivity.showResults.sendMessage(moneyvalues);
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
		
		//update current time if lifecycle was interrupted.
		Date time = new Date();
		lastTime = time.getTime();
		
		//Load textures of all available models.
		street.loadGLTexture(gl, (Context)this.activity);
		ListIterator<Model> i = gems.listIterator();
		while(i.hasNext())
			i.next().loadGLTexture(gl, (Context)this.activity);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GL10.GL_FLAT);

		//Disable Z-Buffer for nice gem-falling-into-the-drains-effect.
		/*gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);*/
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}



	@Override
	public void onDismiss(DialogInterface dialog) {
		street.startStreet();
	}
}
