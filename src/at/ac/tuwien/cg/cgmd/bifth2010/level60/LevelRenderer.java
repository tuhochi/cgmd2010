package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;


public class LevelRenderer implements Renderer {
	private Context context;
	public int screenWidth;
	public int screenHeight;
	private textureManager manager; 
	private float posX;
	private float posY;
	private float mapOffset_x;//, savedMapOffset_x;
	private float mapOffset_y;//, savedMapOffset_y;
	private boolean[] keystates = new boolean[4];
	private Bundle mSavedInstance;
	
	private final static int BUNNY_WIDTH = 55;
	private final static int BUNNY_HEIGHT = 60;
	private final static int LEVEL_WIDTH = 5;
	private final static int LEVEL_HEIGHT = 6;
	private final static int LEVEL_TILESIZE = 100;
	private final static int ACTION_WIDTH = 5;
	private final static int ACTION_HEIGHT = 6;
	
	private final float BUNNY_MOVEMENT_UNIT = 9.0f; 
	private final float COP_MOVEMENT_UNIT = 6.0f;
	
	private Tablet bunny;
	private Tablet cop;
	private final static String BUNNY_X = "BUNNY_X";
	private final static String BUNNY_Y = "BUNNY_Y";
	private final static String COP_X = "COP_X";
	private final static String COP_Y = "COP_Y";
	private final static String MAP_OFFSET_X = "MAP_OFFSET_X";
	private final static String MAP_OFFSET_Y = "MAP_OFFSET_Y";
	
	private final static HashMap<Integer, String> keks = new HashMap<Integer, String>(){
        {
        	put(0, "blank");
            put(1, "streetHor");
            put(2, "streetVer");
            put(3, "intersection");
            put(4, "TintersectionTop");
            put(5, "TintersectionBottom");
            put(6, "TintersectionLeft");
            put(7, "TintersectionRight");
            put(8, "smallHousefl");
            put(9, "smallHousefr");
            put(10, "smallHousebl");
            put(11, "smallHousebr");
            put(12, "housefl");
            put(13, "housefr");
            put(14, "housebl");
            put(15, "housebr");
            put(16, "housecl");
            put(17, "housecr");
        }
	};

	
	public static int levelMap[][] = {  
		{ 1, 1, 1, 5, 1 },
		{ 0, 0, 0, 2, 0 },
		{ 5, 1, 1, 3, 1 },
		{ 2, 8, 9, 2, 0 },
		{ 2,10,11, 2, 0 },
		{ 4, 1, 1, 4, 1 }
	};
	
	public static int actionMap[][] = {
		{ 1, 1, 1, 2, 1 },
		{ 0, 0, 0, 2, 0 },
		{ 1, 1, 1, 3, 1 },
		{ 2, 0, 0, 2, 0 },
		{ 2, 0, 0, 2, 0 },
		{ 4, 1, 1, 4, 1 }
	};
	
	public LevelRenderer(Context context, Bundle msavedinstance) {
		this.context = context;
		mapOffset_x = mapOffset_y = 0 ;// = savedMapOffset_x = savedMapOffset_y = 0;
		posX = posY = 0;
		mSavedInstance = msavedinstance;
		for (int i=0;i<4;i++) keystates[i] = false;
	}
	
	public void setKey(int code) {
		keystates[code] = true;
	}
	
	public void releaseKey(int code) {
		keystates[code] = false;
	}
	
	public void moveBunny(float x, float y) {
		float xPos = bunny.getX();
		float yPos = bunny.getY();
		
		if (!checkCollision(xPos,yPos,x,y) && 
			xPos+x >= 0 && yPos+y >= 0 && 
			xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
			yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
				bunny.move(x, y);
				posX = x; posY = y;
				//if (x + xPos + mapOffset_x < 0 || 
					//x + xPos + mapOffset_x + BUNNY_WIDTH > screenWidth)
				if (xPos > screenWidth/2 && xPos < (LEVEL_WIDTH*LEVEL_TILESIZE)-screenWidth/2)
						moveMap(x, 0);
				//if (y + yPos + mapOffset_y < 0 || 
					//y + yPos + mapOffset_y + BUNNY_HEIGHT > screenHeight) 
				if (yPos > screenHeight/2 && yPos < (LEVEL_HEIGHT*LEVEL_TILESIZE)-screenHeight/2)
						moveMap(0, y);
		}
	}
	
	public void moveCop(float x, float y) {
		float xPos = cop.getX();
		float yPos = cop.getY();
		
		if (!checkCollision(xPos,yPos,x,y) && 
			xPos+x >= 0 && yPos+y >= 0 && 
			xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
			yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
				cop.move(x, y);
		}
	}
	
	public void moveMap(float x, float y) {
		Tablet.addMapOffset(x, y);
		mapOffset_x -= x;
		mapOffset_y -= y;
	}
	
	public boolean checkCollision(float xPos, float yPos, float xwise, float ywise) {
		//find out which tile we want to go to
		int tile1_x, tile1_y, tile2_x, tile2_y;
		
		if (xwise > 0 && ywise == 0) {
			tile1_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
			tile1_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
			tile2_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
			tile2_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
		} else if (xwise < 0 && ywise == 0) {
			tile1_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
			tile1_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
			tile2_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
			tile2_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
		} else if (xwise == 0 && ywise > 0) {
			tile1_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
			tile1_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
			tile2_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
			tile2_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
		} else {
			tile1_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
			tile1_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
			tile2_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
			tile2_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
		}
		
		if (tile1_x < 0 || tile1_x >= LEVEL_WIDTH ||
			tile1_y < 0 || tile1_y >= LEVEL_HEIGHT ||
			tile2_x < 0 || tile2_x >= LEVEL_WIDTH ||
			tile2_y < 0 || tile2_y >= LEVEL_HEIGHT ||
			levelMap[tile1_y][tile1_x] >= 8 || 
			levelMap[tile1_y][tile1_x] == 0 ||
			levelMap[tile2_y][tile2_x] >= 8 || 
			levelMap[tile2_y][tile2_x] == 0)
			return true;
		return false;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -1.0f);
		
		Tablet block = null;
		
		//Step through level map and draw all background blocks
		for (int i = 0; i < levelMap.length; i++) {
			for (int j = 0; j < levelMap[i].length; j++) {
				block = manager.getGameObject(keks.get(levelMap[i][j]));
				
				if (block != null) {
					block.setXY(j*100, i*100);
					block.draw(gl);
				}
				
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

		// update
		if (keystates[0]) moveBunny(-BUNNY_MOVEMENT_UNIT, 0);
		if (keystates[1]) moveBunny(BUNNY_MOVEMENT_UNIT, 0);
		if (keystates[2]) moveBunny(0, BUNNY_MOVEMENT_UNIT);
		if (keystates[3]) moveBunny(0, -BUNNY_MOVEMENT_UNIT);
		handleCop();
		
		// render
		cop.draw(gl);
		bunny.draw(gl);
	}
	
	private void handleCop() {
		float bx = bunny.getX();
		float by = bunny.getY();
		float cx = cop.getX();
		float cy = cop.getY();
		
		float dx = bx - cx;
		float dy = by - cy;
		float d_len = (float) Math.sqrt(dx*dx+dy*dy);
		d_len += 3.0f;
		dx /= d_len;
		dy /= d_len;
		moveCop(dx*COP_MOVEMENT_UNIT, dy*COP_MOVEMENT_UNIT);
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
		
		//manager.getGameObject("bunny").setXY(width/2-BUNNY_WIDTH/2, height/2-BUNNY_HEIGHT/2);
		//cop.setXY(20, 40);
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
		bunny = manager.getGameObject("bunny");
		cop = manager.getGameObject("cop");
		
		if(mSavedInstance != null)
		{
			if(mSavedInstance.containsKey(BUNNY_X) && mSavedInstance.containsKey(BUNNY_Y) &&
					mSavedInstance.containsKey(COP_X) && mSavedInstance.containsKey(COP_Y))
				bunny.setXY(mSavedInstance.getFloat(BUNNY_X), mSavedInstance.getFloat(BUNNY_Y));
				cop.setXY(mSavedInstance.getFloat(COP_X), mSavedInstance.getFloat(COP_Y));
			if(mSavedInstance.containsKey(MAP_OFFSET_X) && mSavedInstance.containsKey(MAP_OFFSET_Y)) {
				mapOffset_x = mSavedInstance.getFloat(MAP_OFFSET_X);
				mapOffset_y = mSavedInstance.getFloat(MAP_OFFSET_Y);
				Tablet.addMapOffset(mapOffset_x, mapOffset_y);
			}
		}
		else {
			bunny.setXY(100, 20);
			cop.setXY(20, 60);
		}
	}
	
	/*public void saveLevel(SharedPreferences.Editor prefEditor) {
		Tablet bunny = manager.getGameObject("bunny");
//		prefEditor.putFloat("l60_posX", 0);
//		prefEditor.putFloat("l60_posY", 0);
//		prefEditor.putFloat("l60_mapOffset_x", 0);
//		prefEditor.putFloat("l60_mapOffset_y", 0);
		prefEditor.putFloat("l60_posX", bunny.getX());
		prefEditor.putFloat("l60_posY", bunny.getY());
////		
		prefEditor.putFloat("l60_mapOffset_x", mapOffset_x);
		prefEditor.putFloat("l60_mapOffset_y", mapOffset_y);
//		
		//save action map too!
	}
	
	public void loadLevel(SharedPreferences prefs) {
		
		posX = prefs.getFloat("l60_posX",0);
		posY = prefs.getFloat("l60_posY",0);
		//moveMap(prefs.getFloat("l60_mapOffset_x", 0), prefs.getFloat("l60_mapOffset_y", 0));
		
//		Tablet bunny = manager.getGameObject("bunny");
//		
//		bunny.setXY(prefs.getFloat("l60_posX",0), prefs.getFloat("l60_posY",0));
//		moveMap(prefs.getFloat("l60_mapOffset_x", 0), prefs.getFloat("l60_mapOffset_y", 0));
		
		//load action map too!
	}*/
	
	public void onSaveInstanceState(Bundle outState) {
		mSavedInstance = outState;
		outState.putFloat(BUNNY_Y, bunny.getY());
		outState.putFloat(BUNNY_X, bunny.getX());
		outState.putFloat(COP_Y, cop.getY());
		outState.putFloat(COP_X, cop.getX());
		outState.putFloat(MAP_OFFSET_X, -mapOffset_x);
		outState.putFloat(MAP_OFFSET_Y, -mapOffset_y);
	}
}
