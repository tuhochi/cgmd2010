package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelRenderer implements Renderer {
	
	
	LevelCollision level;
	LevelObject bunny, a, b, coins[];
	Context context;
	float positionX = 0;
	float positionY = 0;
	
	
	public LevelRenderer(Context context) {
		this.context = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		//b.draw(gl);
//		level.draw(gl);
		bunny.draw(gl);
		
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0.0f, width, height, 0.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.5f, 0.0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		//LevelObject.clearTextures(gl);
		level = new LevelCollision(gl, context, R.drawable.l50_level01_coll);
		bunny = new LevelObject(gl, context, level, positionX, positionY, 20, 20, R.drawable.l50_rabbit_small);
		bunny.enableGravity(true);
		bunny.enableCollision(true);
		b = new LevelObject(gl, context, level, 0, 0, 41*20, 18*20, R.drawable.l50_level01_coll);

	}
	
	public void movePlayer(float x, float y) {bunny.move(x,y);}
	public void movePlayer(int direction, float amount) {bunny.move(direction,amount);}
	public void setPosition(float x, float y) {positionX = x; positionY = y;}
	public float getPositionX() {return bunny.getPositionX();}
	public float getPositionY() {return bunny.getPositionY();}

}