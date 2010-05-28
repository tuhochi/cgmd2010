package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.content.Context;
import android.content.SharedPreferences;


public class LevelRenderer implements Renderer {
	private Context context;
	private Bundle mSavedInstance;
	private GL10 gl;

	private float[] keystates = new float[4];
	private int score = 100;
	private int crime = 0;
	private int screenWidth;
	private int screenHeight; 
	private long oldFrameTime = 0;
	private int frameCounter = 0;
	private float fps = 20.0f;

	private textureManager manager;
	private Tablet bunny;
	private Tablet cop;
	private Tablet gold;
	private Tablet control;
	private Tablet button;
	private int gold_000;
	private int gold_00;
	private int gold_0;

	private float posX;
	private float posY;
	private float mapOffset_x;
	private float mapOffset_y;
	private float cnewX = 0;
	private float cnewY = 0;

	private final static int BUNNY_WIDTH = 55;
	private final static int BUNNY_HEIGHT = 60;
	private final static int LEVEL_WIDTH = 5;
	private final static int LEVEL_HEIGHT = 6;
	private final static int LEVEL_TILESIZE = 100;
	private final static int ACTION_WIDTH = 5;
	private final static int ACTION_HEIGHT = 6;
	private final static int ACTION_TILESIZE = 100;
	private final static int CONTROL_SIZE = 100;
	private final static int BUTTON_SIZE = 50;

	private final float BUNNY_MOVEMENT_UNIT = 9.0f; 
	private final float COP_MOVEMENT_UNIT = 6.0f;
	private int CONTROL_X;
	private int CONTROL_Y;
	private int BUTTON_X;
	private int BUTTON_Y;

	private final static String BUNNY_X = "BUNNY_X";
	private final static String BUNNY_Y = "BUNNY_Y";
	private final static String COP_X = "COP_X";
	private final static String COP_Y = "COP_Y";
	private final static String MAP_OFFSET_X = "MAP_OFFSET_X";
	private final static String MAP_OFFSET_Y = "MAP_OFFSET_Y";
	private final static String SCORE = "SCORE";
	private final static String CRIME = "CRIME";

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
		for (int i=0;i<4;i++) keystates[i] = 0;
		actiontextures = new ArrayList<Tablet>();
	}

	public void setKey (int code, float amount) {
		keystates[code] = amount;
	}

	public void releaseKey(int code) {
		keystates[code] = 0;
	}

	public void onTouch(MotionEvent event) {
		int centerX = CONTROL_X + CONTROL_SIZE/2;
		int centerY = CONTROL_Y + CONTROL_SIZE/2;
		float radius = CONTROL_SIZE/2;
		float x = event.getX();
		float y = screenHeight - event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			float length = (float)Math.sqrt(Math.pow(centerX-x,2)+Math.pow(centerY-y,2));
			if (length <= radius) { //lies inside
//				Log.d("LevelRenderer", "Inside");
				
				float dx = (x - centerX)/length;
				float dy = (y - centerY)/length;
				
				setKey(0, dx*BUNNY_MOVEMENT_UNIT);
				setKey(2, dy*BUNNY_MOVEMENT_UNIT);
			}
			else if (x >= 0 && x <= BUTTON_SIZE && y >= 0 && y <= BUTTON_SIZE)
				performAction();
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			releaseKey(0);
			releaseKey(2);
		}
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
			switch (actionMap[tile_y][tile_x]) {
			case 1:	//spraytag action
				// 	put spraytag tablet here
				actiontextures.add(new Tablet(this.context, 36, 36, (int)xPos-18, (int)yPos-18, manager.getTexture("spraytag"), gl));
				crime++;
				break;
			}
		}
	}

	private void moveBunny(float x, float y) {
		float xPos = bunny.getX();
		float yPos = bunny.getY();
		float newX = 0;
		float newY = 0;
		float myX = 0;
		float myY = 0;
//		float altX;
//		float altY;

		if (!checkCollision(xPos,yPos,x,y) && 
				xPos+x >= 0 && yPos+y >= 0 && 
				xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
				yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
			bunny.move(x, y);
			posX = x; posY = y;
			myX = x; myY = y;
		}
		else {
			//strafe along edges
			if(Math.abs(y) >= Math.abs(x)) { //wants to move up or down
				if (x > 0) { //strafe RIGHT
					newX = BUNNY_MOVEMENT_UNIT;
					newY = 0;
				}
				else { //strafe LEFT
					newX = -BUNNY_MOVEMENT_UNIT;
					newY = 0;
				}
			}
			else { //wants to move left or right
				if (y > 0) { //strafe UP
					newX = 0;
					newY = BUNNY_MOVEMENT_UNIT;
				}
				else { //strafe DOWN
					newX = 0;
					newY = -BUNNY_MOVEMENT_UNIT;
				}
			}
			//strafe
			if (!checkCollision(xPos,yPos,newX,newY) && 
					xPos+newX >= 0 && yPos+newY >= 0 && 
					xPos+newX <= LEVEL_WIDTH*LEVEL_TILESIZE && 
					yPos+newY <= LEVEL_HEIGHT*LEVEL_TILESIZE) {
				bunny.move(newX, newY);
//				posX = newX; posY = newY;
				myX = newX; myY = newY;
			}
		}
		
		//move map
		if (xPos > screenWidth/2 && xPos < (LEVEL_WIDTH*LEVEL_TILESIZE)-screenWidth/2)
			moveMap(myX, 0);

		if (yPos > screenHeight/2 && yPos < (LEVEL_HEIGHT*LEVEL_TILESIZE)-screenHeight/2)
			moveMap(0, myY);
	}

	private void moveCop(float x, float y) {
		float xPos = cop.getX();
		float yPos = cop.getY();
		float bunnyX = bunny.getX();
		float bunnyY = bunny.getY();
		float newX;
		float newY;
		float altX;
		float altY;


		if (!checkCollision(xPos,yPos,x,y) && 
				xPos+x >= 0 && yPos+y >= 0 && 
				xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
				yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
			cop.move(x, y);
			cnewX = 0; cnewY = 0;
		}
		else {
			if(Math.abs(y) >= Math.abs(x)) {
				if (bunnyX > xPos) { //RIGHT
					newX = COP_MOVEMENT_UNIT;
					newY = 0;
				}
				else { //LEFT
					newX = -COP_MOVEMENT_UNIT;
					newY = 0;
				}

				if (bunnyY > yPos) {//UP
					altX = 0;
					altY = COP_MOVEMENT_UNIT;
				}
				else { //DOWN
					altX = 0;
					altY = -COP_MOVEMENT_UNIT;
				}	
			}
			else {
				if (bunnyY > yPos) {//UP
					newX = 0;
					newY = COP_MOVEMENT_UNIT;
				}
				else { //DOWN
					newX = 0;
					newY = -COP_MOVEMENT_UNIT;
				}

				if (bunnyX > xPos) { //RIGHT
					altX = COP_MOVEMENT_UNIT;
					altY = 0;
				}
				else { //LEFT
					altX = -COP_MOVEMENT_UNIT;
					altY = 0;
				}
			}

			if (cnewX == -newX && cnewY == -newY) {
				newX = cnewX; newY = cnewY;
			}
			if (cnewX == -altX && cnewY == -altY) {
				altX = cnewX; altY = cnewY;
			}

			if (!checkCollision(xPos,yPos,newX,newY) && 
					xPos+newX >= 0 && yPos+newY >= 0 && 
					xPos+newX <= LEVEL_WIDTH*LEVEL_TILESIZE && 
					yPos+newY <= LEVEL_HEIGHT*LEVEL_TILESIZE) {
				cop.move(newX, newY);
				cnewX = newX; cnewY = newY;
			}
			else if (!checkCollision(xPos,yPos,altX,altY) && 
					xPos+altX >= 0 && yPos+altY >= 0 && 
					xPos+altX <= LEVEL_WIDTH*LEVEL_TILESIZE && 
					yPos+altY <= LEVEL_HEIGHT*LEVEL_TILESIZE) {
				cop.move(altX, altY);
				cnewX = altX; cnewY = altY;
			}
			xPos = cop.getX();
			yPos = cop.getY();

		}
	}


	private void moveMap(float x, float y) {
		Tablet.addMapOffset(x, y);
		mapOffset_x -= x;
		mapOffset_y -= y;
	}

	private boolean checkCollision(float xPos, float yPos, float xwise, float ywise) {
		//find out which tile we want to go to
		int tile1_x = 0, tile1_y = 0, tile2_x = 0, tile2_y = 0;

		if (Math.abs(ywise)<Math.abs(xwise)) {
			if (xwise > 0) {
				tile1_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
				tile1_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
				tile2_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
				tile2_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
			} else if (xwise < 0) {
				tile1_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
				tile1_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
				tile2_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
				tile2_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
			}
		}
		else {
			if (ywise > 0) {

				tile1_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
				tile1_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
				tile2_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
				tile2_y = (int)((yPos+BUNNY_HEIGHT+ywise) / (float)LEVEL_TILESIZE);
			} else if (ywise < 0){
				tile1_x = (int)((xPos+BUNNY_WIDTH+xwise) / (float)LEVEL_TILESIZE);
				tile1_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
				tile2_x = (int)((xPos+xwise) / (float)LEVEL_TILESIZE);
				tile2_y = (int)((yPos+ywise) / (float)LEVEL_TILESIZE);
			}
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
		score -= crime * 10;
		updateScore();
	}

	private void handleCop() {
		if (crime > 0) {
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
			
			if (oldFrameTime%1000==0) {
				if (dy<0) {
					cop.changeTexture(manager.getTexture("cop_front_l"));
				} else {
					cop.changeTexture(manager.getTexture("cop_back_l"));
				}
			} else if (oldFrameTime%1000==500) {
				if (dy<0) {
					cop.changeTexture(manager.getTexture("cop_front_r"));
				} else {
					cop.changeTexture(manager.getTexture("cop_back_r"));
				}
			}
			
			
			if (bx != cx && by != cy) {
				moveCop(dx*COP_MOVEMENT_UNIT, dy*COP_MOVEMENT_UNIT);
	
				//check if cop catches bunny
				if (d_len <= COP_MOVEMENT_UNIT*3) {
					bunnyWasCaught();
					crime = 0;
				}
			}
		}
	}

	private void drawDigit(GL10 gl, int position, int offset) {
		manager.getGameObject(goldLUT.get(position)).setXY(offset,screenHeight-30);		
		manager.getGameObject(goldLUT.get(position)).draw(gl);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		
		if (frameCounter%10==9) {
			if (oldFrameTime!=0)
			fps = 10.0f/(float)(System.currentTimeMillis()-oldFrameTime)*1000.0f;
			oldFrameTime = System.currentTimeMillis();
		}
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -1.0f);


		// update
//		if (keystates[0]) moveBunny(-BUNNY_MOVEMENT_UNIT, 0);
//		if (keystates[1]) moveBunny(BUNNY_MOVEMENT_UNIT, 0);
//		if (keystates[2]) moveBunny(0, BUNNY_MOVEMENT_UNIT);
//		if (keystates[3]) moveBunny(0, -BUNNY_MOVEMENT_UNIT);
		moveBunny(keystates[0], keystates[2]);
		handleCop();
		
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

		control.draw(gl);
		button.draw(gl);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.gl = gl;
		screenWidth = width;
		screenHeight = height;
		CONTROL_X = screenWidth - CONTROL_SIZE;
		CONTROL_Y = 0;
		BUTTON_X = 0;
		BUTTON_Y = 0;
		Log.d("LevelRenderer", "X From: " + CONTROL_X + " to " + (CONTROL_X+CONTROL_SIZE));
		Log.d("LevelRenderer", "Y From: " + CONTROL_Y + " to " + (CONTROL_Y+CONTROL_SIZE));

		gold.setXY(5, screenHeight-30);
		control.setXY(CONTROL_X, CONTROL_Y);
		button.setXY(BUTTON_X, BUTTON_Y);

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
		cop = manager.getGameObject("cop_front_r");

		gold = manager.getGameObject("gold");
		gold_000 = 1;
		gold_00 = 0;
		gold_0 = 0;

		control = manager.getGameObject("control");
		button = manager.getGameObject("button");

		if(mSavedInstance != null)
		{
			if(mSavedInstance.containsKey(MAP_OFFSET_X) && mSavedInstance.containsKey(MAP_OFFSET_Y)) {
				mapOffset_x = mSavedInstance.getFloat(MAP_OFFSET_X);
				mapOffset_y = mSavedInstance.getFloat(MAP_OFFSET_Y);

				Log.d("LevelRenderer", "Load mapOffsetx: " + mSavedInstance.getFloat(MAP_OFFSET_X));
				Log.d("LevelRenderer", "Load mapOffsety: " + mSavedInstance.getFloat(MAP_OFFSET_Y));

				Tablet.setMapOffset(mapOffset_x, mapOffset_y);
			}
			if(mSavedInstance.containsKey(BUNNY_X) && mSavedInstance.containsKey(BUNNY_Y) &&
					mSavedInstance.containsKey(COP_X) && mSavedInstance.containsKey(COP_Y)) {
				bunny.setXY(mSavedInstance.getFloat(BUNNY_X), mSavedInstance.getFloat(BUNNY_Y));
				cop.setXY(mSavedInstance.getFloat(COP_X), mSavedInstance.getFloat(COP_Y));

				Log.d("LevelRenderer", "Load bunny pos: x " + mSavedInstance.getFloat(BUNNY_X) + " y " + mSavedInstance.getFloat(BUNNY_Y));
				Log.d("LevelRenderer", "Load cop pos: x " + mSavedInstance.getFloat(COP_X) + " y " + mSavedInstance.getFloat(COP_Y));
			}
			if(mSavedInstance.containsKey(SCORE) && mSavedInstance.containsKey(CRIME))
			{
				score = mSavedInstance.getInt(SCORE);
				crime = mSavedInstance.getInt(CRIME);

				Log.d("LevelRenderer", "Load score: " + score);
				Log.d("LevelRenderer", "Load crime: " + crime);
			}
			updateScore();
			//moveScore(mapOffset_x, mapOffset_y);
		}
		else {
			score = 100;
			mapOffset_x = mapOffset_y = 0;
			bunny.setXY(100, 20);
			cop.setXY(20, 60);
			Tablet.setMapOffset(mapOffset_x, mapOffset_y);
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
		outState.putFloat(MAP_OFFSET_X, mapOffset_x);
		outState.putFloat(MAP_OFFSET_Y, mapOffset_y);
		outState.putInt(SCORE, score);
		outState.putInt(CRIME, crime);

		Log.d("LevelRenderer", "Save bunny pos: x " + bunny.getX() + " y " + bunny.getY());
		Log.d("LevelRenderer", "Save cop pos: x " + cop.getX() + " y " + cop.getY());
		Log.d("LevelRenderer", "Save mapOffsetx: " + (mapOffset_x));
		Log.d("LevelRenderer", "Save mapOffsety: " + (mapOffset_y));
		Log.d("LevelRenderer", "Save score: " + score);
		Log.d("LevelRenderer", "Save crime: " + crime);
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
}
