package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class LevelRenderer implements Renderer {
	private Tablet tablet;
	private Tablet cop;
	private Tablet background;
	private Context context;
	public int screenWidth;
	public int screenHeight;
	
	public LevelRenderer(Context context) {
		this.context = context;
	}
	
	public void moveObject(int x, int y) {
		int xPos = tablet.getX();
		int yPos = tablet.getY();
		
		if (x + xPos >= 0 && x + xPos <= screenWidth &&
			y + yPos >= 0 && y + yPos <= screenHeight
			&& !checkCollision(xPos,yPos,x,y))
		{
			tablet.move(x, y);
		}
	}
	
	public boolean checkCollision(int xPos, int yPos, int xwise, int ywise) {
		return false;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -1.0f);
		background.draw(gl);
		tablet.draw(gl);
		cop.draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		screenWidth = width;
		screenHeight = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
 		GLU.gluOrtho2D(gl, 0, width, 0, height);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig paramEGLConfig) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	    gl.glEnable(GL10.GL_BLEND);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    //gl.glBlendFunc(GL10.GL_ONE, GL10.GL_SRC_COLOR);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);		
		
		
		background = new Tablet(context, 400, 320, 0, 0, R.drawable.l60_town_small, gl);
		cop = new Tablet(context, 50, 50, 20, 100, R.drawable.l60_cop_front_l, gl);
		tablet = new Tablet(context, 70, 70, 0, 0, R.drawable.l60_bunny_front, gl);
	}
}