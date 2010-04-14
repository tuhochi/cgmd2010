package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Vibrator;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.observer.Observer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.Crosshairs;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.sound.SoundPlayer;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Landscape;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.RabbitSprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Sprite;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.TextureParts;


public class GameScene extends GLSurfaceView implements Renderer {
	/** the context of the scene */
	private Context context = null;
	/** the flying rabbit */
	private PhysicalRabbit rabbit;
	/** the crosshairs that shoot on the rabbit */
	private Crosshairs crosshairs;
	/** the landscape moving with parallax-effect */
	private Landscape landscape;

	/** thread for game logic */
	private GameThread gameThread;
	/** queue of all inputGestures to process */
	private Queue<InputGesture> inputQueue = new LinkedList<InputGesture>();
	/** soundPlayer */
	private SoundPlayer player = null;
	/** the system's vibrator */
	private Vibrator vibrator = null;

	
	public GameScene(Context context) {
		super(context);
		
		this.context = context;
		
		player = SoundPlayer.getInstance(context);
		// get the system's vibrator
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
		System.err.println("GameScene created");
		rabbit = null;
		landscape = null;
		gameThread = null;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		if (landscape != null) {
			landscape.draw(gl);
		}
		
		if (crosshairs != null) {
			crosshairs.draw(gl);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 1);
		gl.glClearColorx((int)(255*.5), (int)(255*.7), 255, 255);
	}
	
	private void configureTexture(GL10 gl, Texture texture) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTextureName());
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		Texture mainTexture = new Texture(gl, getContext(), R.drawable.l44_texture);
		configureTexture(gl, mainTexture);
		
		rabbit = new PhysicalRabbit(new RabbitSprite(mainTexture), new Sprite(TextureParts.getCoin(mainTexture)), this.getWidth(), this.getHeight());
		rabbit.setPosition(getWidth()/2, getHeight()/2);
		
		crosshairs = new Crosshairs(this, mainTexture, this.getWidth(), this.getHeight());
		crosshairs.setPosition(30, getHeight()/2);
		crosshairs.setRabbit(rabbit);
		
		
		landscape = new Landscape(mainTexture, getWidth(), getHeight());
		landscape.setRabbit((PhysicalRabbit)rabbit);
		
		// Observers for updating UI
		crosshairs.addObserver((Observer)context);
		TimeManager.getInstance().addObserver((Observer)context);
		
		restartGameThread();
	}
	
	private void restartGameThread() {
		stopGameThread();
		
		if (rabbit != null && landscape != null && crosshairs != null) {
			gameThread = new GameThread(this, rabbit, landscape, crosshairs);
			gameThread.start();
		}
	}
	
	public void stopGameThread() {
		if (gameThread != null) {
			gameThread.doQuit();
			gameThread = null;
		}	
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopGameThread();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		restartGameThread();
	}

	public void clearInputQueue() {
		inputQueue.clear();
	}
	
	public void addInputGesture(InputGesture gesture) {
		//if (inputQueue.size() < 1)
		inputQueue.clear();
		inputQueue.add(gesture);
	}
	
	public InputGesture getNextInputGesture() {
		return inputQueue.poll();
	}
	
	public SoundPlayer getSoundPlayer() {
		return player;
	}
	
	public Vibrator getVibrator() {
		return vibrator;
	}
	
	public PhysicalRabbit getRabbit() {
		return rabbit;
	}
}
