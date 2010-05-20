package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;


public class LevelRenderer implements Renderer {
	private Context context;
	private Bundle mSavedInstance;
	private GL10 gl;
	
	private boolean[] keystates = new boolean[4];
	private int score;
	private int crime = 0;  //TODO: change value when crime committed
	private int screenWidth;
	private int screenHeight; 
	
	private textureManager manager;
	private Tablet bunny;
	private Tablet cop;
	private Tablet gold;
	private int gold_000;
	private int gold_00;
	private int gold_0;
	private float goldOffsetX;
	private float goldOffsetY;
	
	private float posX;
	private float posY;
	private float mapOffset_x;
	private float mapOffset_y;
	
	private final static int BUNNY_WIDTH = 55;
	private final static int BUNNY_HEIGHT = 60;
	private final static int LEVEL_WIDTH = 5;
	private final static int LEVEL_HEIGHT = 6;
	private final static int LEVEL_TILESIZE = 100;
	private final static int ACTION_WIDTH = 5;
	private final static int ACTION_HEIGHT = 6;
	private final static int ACTION_TILESIZE = 100;
	
	private final float BUNNY_MOVEMENT_UNIT = 9.0f; 
	private final float COP_MOVEMENT_UNIT = 6.0f;
	
	private final static String BUNNY_X = "BUNNY_X";
	private final static String BUNNY_Y = "BUNNY_Y";
	private final static String COP_X = "COP_X";
	private final static String COP_Y = "COP_Y";
	private final static String MAP_OFFSET_X = "MAP_OFFSET_X";
	private final static String MAP_OFFSET_Y = "MAP_OFFSET_Y";
	private final static String SCORE = "SCORE";
	
	private ArrayList<Tablet> actiontextures;
		
	private static int levelMap[][] = {  
		{ 1, 1, 1, 5, 1 },
		{ 0, 0, 0, 2, 0 },
		{ 5, 1, 1, 3, 1 },
		{ 2, 8, 9, 2, 0 },
		{ 2,10,11, 2, 0 },
		{ 4, 1, 1, 4, 1 }
	};
	
	private static int actionMap[][] = {
		{ 1, 1, 1, 2, 1 },
		{ 0, 0, 0, 2, 0 },
		{ 1, 1, 1, 3, 1 },
		{ 2, 0, 0, 2, 0 },
		{ 2, 0, 0, 2, 0 },
		{ 4, 1, 1, 4, 1 }
	};
	
	private final static HashMap<Integer, String> tileLUT = new HashMap<Integer, String>(){
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
	
	private final static HashMap<Integer, String> goldLUT = new HashMap<Integer, String>(){
        {
        	put(0, "zero");
            put(1, "one");
            put(2, "two");
            put(3, "three");
            put(4, "four");
            put(5, "five");
            put(6, "six");
            put(7, "seven");
            put(8, "eight");
            put(9, "nine");
        }
	};
	
	public LevelRenderer(Context context, Bundle msavedinstance) {
		this.context = context;
		mapOffset_x = mapOffset_y = 0 ;
		posX = posY = 0;
		mSavedInstance = msavedinstance;
		for (int i=0;i<4;i++) keystates[i] = false;
		actiontextures = new ArrayList<Tablet>();
	}
	
	public void setKey(int code) {
		keystates[code] = true;
	}
	
	public void releaseKey(int code) {
		keystates[code] = false;
	}
	
	public void performAction() {
		//see where the bunny is to find out what we can do
		float xPos = bunny.getX()+BUNNY_WIDTH/2;
		float yPos = bunny.getY()+BUNNY_HEIGHT/2;
		int tile_x = (int)((xPos) / (float)ACTION_TILESIZE);
		int tile_y = (int)((yPos) / (float)ACTION_TILESIZE);
		
		Iterator<Tablet> it = actiontextures.iterator();
		boolean proximity = false;
		while(it.hasNext()) {
			Tablet t = it.next();
			if (Math.abs(t.getX() - xPos) < 26 && Math.abs(t.getY() - yPos) < 26) proximity = true; 
		}
		
		if (!proximity) {
			float offset_x = mapOffset_x;
			float offset_y = mapOffset_y;
			switch (actionMap[tile_y][tile_x]) {
				case 1:	//spraytag action
					// 	put spraytag tablet here
					actiontextures.add(new Tablet(this.context, 36, 36, (int)xPos-18, (int)yPos-18, manager.getTexture("spraytag"), gl));
					Tablet.setMapOffset(offset_x, offset_y); //reset ofset cause on new Table it is set to 0
					crime++;
					break;
			}
			mapOffset_x = offset_x;
			mapOffset_y = offset_y;
		}
	}
	
	private void moveBunny(float x, float y) {
		float xPos = bunny.getX();
		float yPos = bunny.getY();
		
		if (!checkCollision(xPos,yPos,x,y) && 
			xPos+x >= 0 && yPos+y >= 0 && 
			xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
			yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
				bunny.move(x, y);
				posX = x; posY = y;
				
				if (xPos > screenWidth/2 && xPos < (LEVEL_WIDTH*LEVEL_TILESIZE)-screenWidth/2)
						moveMap(x, 0);

				if (yPos > screenHeight/2 && yPos < (LEVEL_HEIGHT*LEVEL_TILESIZE)-screenHeight/2)
						moveMap(0, y);
		}
	}
	
	private void moveCop(float x, float y) {
		float xPos = cop.getX();
		float yPos = cop.getY();
		
		if (!checkCollision(xPos,yPos,x,y) && 
			xPos+x >= 0 && yPos+y >= 0 && 
			xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
			yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
				cop.move(x, y);
		}
	}
	
	private void moveMap(float x, float y) {
		Tablet.addMapOffset(x, y);
		mapOffset_x -= x;
		mapOffset_y -= y;
		moveScore(x,y);
	}
	
	private boolean checkCollision(float xPos, float yPos, float xwise, float ywise) {
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
	
	private void bunnyWasCaught () {
//		if (score >= 10) {
//			score -= 10;
			score -= crime * 10;
			
			if (score < 0)
				score = 0;
			updateScore();
//		}
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
		
		if (crime > 0 && bx != cx && by != cy) {
			moveCop(dx*COP_MOVEMENT_UNIT, dy*COP_MOVEMENT_UNIT);
		
			//check if cop catches bunny
			if (d_len < 5) {
				bunnyWasCaught();
				crime = 0;
			}
		}
	}
	
	private void drawDigit(GL10 gl, int position, int offset) {
		manager.getGameObject(goldLUT.get(position)).setXY(offset+goldOffsetX,screenHeight-30+goldOffsetY);		
		manager.getGameObject(goldLUT.get(position)).draw(gl);
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
				block = manager.getGameObject(tileLUT.get(levelMap[i][j]));
				
				if (block != null) {
					block.setXY(j*100, i*100);
					block.draw(gl);
				}
				
			}
		}

		// update
		if (keystates[0]) moveBunny(-BUNNY_MOVEMENT_UNIT, 0);
		if (keystates[1]) moveBunny(BUNNY_MOVEMENT_UNIT, 0);
		if (keystates[2]) moveBunny(0, BUNNY_MOVEMENT_UNIT);
		if (keystates[3]) moveBunny(0, -BUNNY_MOVEMENT_UNIT);
		handleCop();
		
		//draw action stuff
		Iterator<Tablet> it = actiontextures.iterator();
		while (it.hasNext()) {
			it.next().draw(gl);
		}
		
		// render
		cop.draw(gl);
		bunny.draw(gl);
		
		//write score
		gold.draw(gl);
		int offset = 0;
		for (int i = 1; i<4; i++) {
			switch (i) {
				case 1:
					offset = 20;
					drawDigit(gl,gold_000, offset);
				case 2:
					offset = 30;
					drawDigit(gl,gold_00, offset);
				case 3:;
					offset = 40;
					drawDigit(gl,gold_0, offset);
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.gl = gl;
		screenWidth = width;
		screenHeight = height;
		
		bunny.setXY(100, 20);
		cop.setXY(20, 60);
		gold.setXY(5, screenHeight-30);
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
 		GLU.gluOrtho2D(gl, 0, width, 0, height);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig paramEGLConfig) {
		this.gl = gl;
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
		
		gold = manager.getGameObject("gold");
		gold_000 = 1;
		gold_00 = 0;
		gold_0 = 0;
		
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
			if(mSavedInstance.containsKey(SCORE))
				score = mSavedInstance.getInt(SCORE);
			updateScore();
			moveScore(mapOffset_x, mapOffset_y);
		}
		else {
			score = 100;
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public void onSaveInstanceState(Bundle outState) {
		mSavedInstance = outState;
		outState.putFloat(BUNNY_Y, bunny.getY());
		outState.putFloat(BUNNY_X, bunny.getX());
		outState.putFloat(COP_Y, cop.getY());
		outState.putFloat(COP_X, cop.getX());
		outState.putFloat(MAP_OFFSET_X, -mapOffset_x);
		outState.putFloat(MAP_OFFSET_Y, -mapOffset_y);
		outState.putFloat(SCORE, score);
	}
	
	private void updateScore () {
		if (score < 0) score = 0;
		int hundred = (int)Math.floor(score/100);
		int ten = (int)Math.floor((score-(hundred*100))/10);
		int one = (int)Math.floor((score-(hundred*100)-(ten*10)));
		
		gold_000 = hundred;
		gold_00 = ten;
		gold_0 = one;

	}
	
	private void moveScore(float x, float y) {
		gold.move(x, y);
		goldOffsetX += x;
		goldOffsetY += y;
	}
}
