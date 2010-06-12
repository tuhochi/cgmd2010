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

	/**
	 * main render class
	 * @author Georg, Gerald
	 */
	
	/** our {@link LevelActivity}**/
	private LevelActivity activity;
	/** our street model**/
	private ModelStreet street;
	/** list with all gem models **/
	private List<Model> gems;
	
	/** variable used for calculations of deltatime**/
	private long lastTime = -1;
	/** the {@link Accelerometer}**/
	private Accelerometer accelerometer;
	
	/** {link {@link TextView} component for showing framerate**/
	private TextView tfFps;
	/** {link {@link TextView} component for showing money amount**/
	private TextView tfPoints;
	/** {link {@link TextView} component used as font shadow for money amount**/
	private TextView tfPointsShadow;
	
	/** framerate variable**/
	private int fps = 0;
	/** variable for handling results {@link ProgressManager}**/
	private ProgressManager progman;
		
	/** distance to the levelend**/
	float streetMeter = 0;
	/** format of the streetMeter output **/
	DecimalFormat df = new DecimalFormat("0");
	
	/** if game is still active or at the end **/
	private boolean gamefinished = false;
	
	
	/** Handler for FPS timer */
	private Handler updateFps = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfFps.setText(fps + " FPS");
			fps = 0;
		}
	};
	
	/** Handler for (money-) points  */
	private Handler updatePoints = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			tfPoints.setText("$" + progman.getRemainingValue());
			tfPointsShadow.setText(tfPoints.getText());
		}
	};
	
	/** Handler for gameTime - how many meters to the end of the level */
	private Handler updateGameMeter = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (street.isStreetActive()) { streetMeter --; }
			tfFps.setText(df.format(streetMeter) + "m");
		}
	};
	
	 /**
	 * Handler for showing the {@link ResultDialog} at the end of the level*/
	private Handler showResultDialog = new Handler() {
		 	@Override
	    	public void handleMessage(Message msg) {
	    		ResultDialog resultdialog = new ResultDialog(activity,progman);
	    		
	    		resultdialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface resultdialog) {
						activity.finish();
					}
				});
	    		resultdialog.show();	
	    	}
	    };
	
	
	/**
	 * create a new RenderManager for handling the rendering
	 * @param levelActivity {@link LevelActivity}
	 * @param street {@link ModelStreet}
	 * @param gems a list of gem {@link Model}s
	 * @param accelerometer {@link Accelerometer}
	 * @param progman {@link ProgressManager}
	 * @param soundManager {@link SoundManager}
	 */
	public RenderManager(at.ac.tuwien.cg.cgmd.bifth2010.level84.LevelActivity levelActivity, ModelStreet street, List<Model> gems, Accelerometer accelerometer, ProgressManager progman, SoundManager soundManager) {
		this.activity = levelActivity;
		this.street = street;
		this.gems = gems;
		this.accelerometer = accelerometer;
		this.progman = progman;
		this.tfFps = (TextView) levelActivity.findViewById(R.id.l84_TfFps);
		this.tfPoints = (TextView) levelActivity.findViewById(R.id.l84_Points);
		this.tfPointsShadow = (TextView) levelActivity.findViewById(R.id.l84_PointsShadow);
		
		//init the initial state of the distance from start to endpoint of the level
		streetMeter = street.getStreetWidth()-16f;
		
		//init and start the Timer and TimerTask for the level
		Timer fpsUpdateTimer = new Timer();
		fpsUpdateTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				//updateFps.sendEmptyMessage(0);
				updatePoints.sendEmptyMessage(0);
				updateGameMeter.sendEmptyMessage(0);
			}
		}, 1000, 1000);
	}
	
	
	
	/**
	 * Main draw method. Updates and renders all available models.
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		if (!gamefinished)
		{
			//calculate deltaTime
			Date time = new Date();
			long currentTime = time.getTime();
			
			if (lastTime == -1)
				lastTime = time.getTime();
			
			double deltaTime = (double)(currentTime - lastTime) / 1000.0;
			lastTime = time.getTime();
			
			fps++;
			
			//UPDATE -------------------------
			street.update(deltaTime, accelerometer.getOrientation());

			checkStreetEnd(street.getStreetPos()); //check if the street end is reached
			
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
	}
	

	
	/**
	 * check if the end of the level has been reached
	 * @param streetPos recent street Position
	 */
	private void checkStreetEnd(float streetPos)
	{
		if (streetMeter < 1)
		{
			//stop the animation and rendering of the street 
			gamefinished = true;
			street.stopStreet();

			//show the results
			showResultDialog.sendEmptyMessage(0);
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


	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
	 */
	@Override
	public void onDismiss(DialogInterface dialog) {
		//start street animation
		street.startStreet();
	}
}
