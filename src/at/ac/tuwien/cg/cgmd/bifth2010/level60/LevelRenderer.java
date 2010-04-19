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
//	private Tablet tablet;
//	private Tablet cop;
//	private Tablet background;
	private Context context;
	public int screenWidth;
	public int screenHeight;
	private textureManager manager; 
	
	
	public static int levelMap[][] = {  
		{ 1, 1, 0, 0 },
		{ 0, 0, 0, 0 },
		{ 0, 0, 0, 0 },
		{ 0, 0, 0, 0 }
	};
	
//	public static int actionMap[][] = {
//		
//	};
	
	public LevelRenderer(Context context) {
		this.context = context;
	}
	
	public void moveObject(int x, int y) {
		int xPos = manager.getGameObject("bunny").getX();
		int yPos = manager.getGameObject("bunny").getY();
		
		if (x + xPos >= 0 && x + xPos <= screenWidth-60 &&
			y + yPos >= 0 && y + yPos <= screenHeight-60
			&& !checkCollision(xPos,yPos,x,y))
		{
			manager.getGameObject("bunny").move(x, y);
		}
		
		//txtView.setText("Position: " + x + " " + y);
	}
	
	public boolean checkCollision(int xPos, int yPos, int xwise, int ywise) {
		return false;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -1.0f);
		
		Tablet block = null;
		
//		background.draw(gl);
//		tablet.draw(gl);
//		cop.draw(gl);
		
		//Step through level map and draw all background blocks
		for (int i = 0; i < levelMap.length; i++) {
			for (int j = 0; j < levelMap[i].length; j++) {
				switch (levelMap[i][j]) {
					case 1: 
					block = manager.getGameObject("streetHor");
					break;
					case 2: 
					block = manager.getGameObject("streetVerr");
					break;
					case 3:
					block = manager.getGameObject("intersection");
					break;
					case 4: 
					block = manager.getGameObject("TintersectionTop");
					break;
					case 5: 
					block = manager.getGameObject("TintersectionBottom");
					break;
					case 6: 
					block = manager.getGameObject("TintersectionLeft");
					break;
					case 7: 
					block = manager.getGameObject("TintersectionRight");
					break;
					case 8: 
					block = manager.getGameObject("smallHousefl");
					break;
					case 9:
					block = manager.getGameObject("smallHousefr");
					break;
					case 10: 
					block = manager.getGameObject("smallHousebl");
					break;
					case 11: 
					block = manager.getGameObject("smallHousebr");
					break;
					case 12: 
					block = manager.getGameObject("housefl");
					break;
					case 13:
					block = manager.getGameObject("housefr");
					break;
					case 14:
					block = manager.getGameObject("housebl");
					break;
					case 15:
					block = manager.getGameObject("housebr");
					break;
					case 16:
					block = manager.getGameObject("housecl");
					break;
					case 17:
					block = manager.getGameObject("housecr");
				}
				block.setXY(j*100, i*100);
				block.draw(gl);
			}
		}
		
		//Step through background map and draw all background blocks
//		for (int i = 0; i < actionMap.length; i++) {
//			for (int j = 0; j < actionMap[i].length; j++) {
//				switch (actionMap[i][j]) {
//					case 1
//						manager.getGameObject("bat").draw(gl);
//					break;
//					case 2 
//						manager.getGameObject("spray").draw(gl);
//					break;
//					case 3
//					manager.getGameObject("window").draw(gl);
//					break;
//					case 4 
//						manager.getGameObject("wall").draw(gl);
//					break;
//				}
//			}
//		}
		
		
		manager.getGameObject("cop").setXY(20, 60);
		manager.getGameObject("cop").draw(gl);
		manager.getGameObject("bunny").draw(gl);
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
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);		
		
		
		//create all needed textures
		this.manager = new textureManager(context, gl);
		
//		//manager.loadTexture("Bunny", R.drawable.l60_bunny_front);
//		
//		//background = new Tablet(context, 480, 320, 0, 0, R.drawable.l60_town_small, gl);
////		cop = new Tablet(context, 50, 50, 20, 100, R.drawable.l60_cop_front_l, gl);
////		tablet = new Tablet(context, 70, 70, 0, 0, R.drawable.l60_bunny_front, gl);
//		//all map elements...texture manager?
		
	}
}
