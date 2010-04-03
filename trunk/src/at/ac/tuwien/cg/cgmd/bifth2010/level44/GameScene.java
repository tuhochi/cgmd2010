package at.ac.tuwien.cg.cgmd.bifth2010.level44;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.io.InputGesture;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Rabbit;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Texture;


public class GameScene extends GLSurfaceView implements Renderer {
	private PhysicalObject rabbit;
	private GameThread gameThread;
	private Queue<InputGesture> inputQueue = new LinkedList<InputGesture>();
	
	public GameScene(Context context) {
		super(context);
		setRenderer(this);
		setRenderMode(RENDERMODE_CONTINUOUSLY);
		System.err.println("GameScene created");
		rabbit = null;
		gameThread = null;
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		//System.err.println("onDrawFrame");
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		if (rabbit != null) {
			rabbit.draw(gl);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		System.err.println("onSurfaceChanged");
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, height, 0, 0, 1);
		gl.glClearColorx((int)(255*.5), (int)(255*.7), 255, 255);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		System.err.println("onSurfaceCreated");
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		Texture mainTexture = new Texture(gl, getContext(), R.drawable.l44_texture);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mainTexture.getTextureName());
		
		/* Texture settings - might need some tweaking (FIXME) */
		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		rabbit = new PhysicalRabbit(new Rabbit(mainTexture));
		rabbit.setPosition(getWidth()/2, getHeight()/2);
		
		//((RabbitHead)rootItem).setWingAngle(50);
		
		restartGameThread();
	}
	
	private void restartGameThread() {
		stopGameThread();
		
		if (rabbit != null) {
			gameThread = new GameThread(this,rabbit);
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
		inputQueue.add(gesture);
	}
	
	public InputGesture getNextInputGesture() {
		return inputQueue.poll();
	}
}
