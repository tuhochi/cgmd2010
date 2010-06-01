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
import android.widget.ListView;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;


public class LevelRenderer implements Renderer {
	private Context context;
	private Bundle mSavedInstance;
	private GL10 gl;

	private final float BUNNY_DEFAULT_MOVEMENT_UNIT = 6.0f;
	private final float COP_DEFAULT_MOVEMENT_UNIT = 4.0f;
	private final float DEFAULT_FPS = 12.0f;
	public final static int BUNNY_WIDTH = 60;
	public final static int BUNNY_HEIGHT = 60;
	public final static int COP_WIDTH = 50;
	public final static int COP_HEIGHT = 50;
	private final static int LEVEL_WIDTH = 5;
	private final static int LEVEL_HEIGHT = 6;
	private final static int LEVEL_TILESIZE = 100;
	private final static int ACTION_WIDTH = 5;
	private final static int ACTION_HEIGHT = 6;
	private final static int ACTION_TILESIZE = 100;
	public final static int CONTROL_SIZE = 150;
	private final static float CATCH_DISTANCE = COP_WIDTH;
	
	private boolean drawLock = false;
	private float[] keystates = new float[4];
	private int score = 100;
	private int crime = 0;
	private int copCounter = 0;
	private int screenWidth;
	private int screenHeight; 
	private long oldFrameTime = 0;
	private long startFrameTime = 0;
	private int frameCounter = 0;
	private float fps = DEFAULT_FPS;
	private float scale = 1.0f;

	private LevelSurfaceView glv;
	private textureManager manager;
	private ArrayList<Tablet> cops;
	private Tablet bunny;
	private Tablet gold;
	private Tablet control;
	private int gold_000;
	private int gold_00;
	private int gold_0;

	private float posX;
	private float posY;
	private float mapOffset_x;
	private float mapOffset_y;
	private float cnewX = 0;
	private float cnewY = 0;
	
	private float BUNNY_MOVEMENT_UNIT = 9.0f; 
	private float COP_MOVEMENT_UNIT = 6.0f;
	private int CONTROL_X;
	private int CONTROL_Y;
	private int makeCopsPuff = 0;
	private int crimeCounter = 0;

	private final static String BUNNY_X = "BUNNY_X";
	private final static String BUNNY_Y = "BUNNY_Y";
	private final static String COP_NUMBER = "COP_NUMBER";
	private final static String COP_X = "COP_X";
	private final static String COP_Y = "COP_Y";
	private final static String MAP_OFFSET_X = "MAP_OFFSET_X";
	private final static String MAP_OFFSET_Y = "MAP_OFFSET_Y";
	private final static String SCORE = "SCORE";
	private final static String CRIME = "CRIME";
	private final static String STARTFRAMETIME = "STARTFRAMETIME";
	private final static String PAUSETIME = "PAUSETIME";

	private ArrayList<Tablet> actiontextures;


	private static int levelMap[][] = {  
		{ 3, 1, 1, 3, 1 },
		{ 2, 0, 0, 2, 0 },
		{ 3, 1, 1, 3, 1 },
		{ 2, 8, 9, 2, 0 },
		{ 2,10,11, 2, 0 },
		{ 3, 1, 1, 3, 1 }
	};

	private static int actionMap[][] = {
		{ 1, 1, 1, 2, 1 },
		{ 0, 0, 0, 2, 0 },
		{ 1, 1, 1, 3, 1 },
		{ 2, 0, 0, 2, 0 },
		{ 2, 0, 0, 2, 0 },
		{ 3, 1, 1, 3, 1 }
	};

	private final static HashMap<Integer, Integer> keks = new HashMap<Integer, Integer>() {
		{
			put(0, 0);
			put(1, 5);
			put(2, 20);
			put(3, 50);
		}
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
			put(20, "blank");
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

	public LevelRenderer(Context context, Bundle msavedinstance, LevelSurfaceView glv) {
		this.context = context;
		this.glv = glv;
		mapOffset_x = mapOffset_y = 0 ;
		posX = posY = 0;
		mSavedInstance = msavedinstance;
		for (int i=0;i<4;i++) keystates[i] = 0;
		actiontextures = new ArrayList<Tablet>();
		cops = new ArrayList<Tablet>();
	}

	public void setKey (int code, float amount) {
		keystates[code] = amount;
	}

	public void releaseKey(int code) {
		keystates[code] = 0;
	}

	public void onTouch(MotionEvent event) {
		int centerX = CONTROL_X + (int)((float)CONTROL_SIZE*scale)/2;
		int centerY = CONTROL_Y + (int)((float)CONTROL_SIZE*scale)/2;

		float x = event.getX()-CONTROL_X;
		float y = screenHeight - event.getY()-CONTROL_Y;

		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			float length = (float)Math.sqrt(Math.pow(centerX-x-CONTROL_X,2)+Math.pow(centerY-y-CONTROL_Y,2));
			if (x > 0 && x < CONTROL_SIZE*scale && y > 0 && y < CONTROL_SIZE*scale) { //lies inside
				//movement or action?
				if (x >= CONTROL_SIZE*scale/3 && x <= 2*CONTROL_SIZE*scale/3 && 
						y >= CONTROL_SIZE*scale/3 && y <= 2*CONTROL_SIZE*scale/3) 
					performAction();
				else {
					float dx = (x + CONTROL_X - centerX)/length;
					float dy = (y + CONTROL_Y - centerY)/length;
					if (dx > 1.0f) dx = 1.0f;
					if (dy > 1.0f) dy = 1.0f;
					setKey(0, dx*BUNNY_MOVEMENT_UNIT);
					setKey(2, dy*BUNNY_MOVEMENT_UNIT);
				}
			}
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
		boolean acted = false;

		while(it.hasNext()) {
			Tablet t = it.next();
			if (Math.abs(t.getX() - xPos) < ACTION_TILESIZE/2 && Math.abs(t.getY() - yPos) < ACTION_TILESIZE/2) proximity = true; 
		}

		if (!proximity && !drawLock) {
			switch (actionMap[tile_y][tile_x]) {
			case 1:	//spraytag action
				// 	put spraytag tablet here 
				actiontextures.add(new Tablet(this.context, 36, 36, (int)xPos-18, (int)yPos-18, manager.getTexture("spraytag"), gl));
				acted = true;
				break;
			}

			if (acted) {	
				if (copCounter == 0 || Math.ceil(crime/20)>copCounter)
					cops.add(new Tablet(context, COP_WIDTH, COP_HEIGHT, 0, 0, manager.getTexture("cop_front_l"), gl));

				crime += keks.get(actionMap[tile_y][tile_x]);
				crimeCounter++;
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

		if (frameCounter%10==0) {
			if (y<0) bunny.changeTexture(manager.getTexture("bunny_front_l"));
			else if (y>0) bunny.changeTexture(manager.getTexture("bunny_back_l"));

		} else if (frameCounter%10==5) {
			if (y<0) bunny.changeTexture(manager.getTexture("bunny_front_r"));
			else if (y>0) bunny.changeTexture(manager.getTexture("bunny_back_r"));
		}

		int coll = checkCollision(xPos,yPos,x,y, BUNNY_WIDTH, BUNNY_HEIGHT); 
		if (coll == 0)	{
			bunny.move(x, y);
			posX = x; posY = y;
			myX = x; myY = y;
		}
		else {
			//strafe along edges
			if (x > 0) newX = BUNNY_MOVEMENT_UNIT;
			else if (x < 0) newX = -BUNNY_MOVEMENT_UNIT;
			else newX = 0;
			if (y > 0) newY = BUNNY_MOVEMENT_UNIT;
			else if (y < 0) newY = -BUNNY_MOVEMENT_UNIT;
			else newY = 0;
			
			if ((coll & 1) > 0 || (coll & 2) > 0) newX = 0; // left/right
			if ((coll & 4) > 0 || (coll & 8) > 0) newY = 0; // up/down
				
			bunny.move(newX, newY);
			myX = newX; myY = newY;
		}

		//move map
		if (xPos*scale > screenWidth/2 && xPos < (LEVEL_WIDTH*LEVEL_TILESIZE)-screenWidth/(2*scale))
			moveMap(myX, 0);

		if (yPos*scale > screenHeight/2 && yPos < (LEVEL_HEIGHT*LEVEL_TILESIZE)-screenHeight/(2*scale))
			moveMap(0, myY);
	}

	private void moveCop(float x, float y, Tablet cop) {
		float xPos = cop.getX();
		float yPos = cop.getY();
		float bunnyX = bunny.getX();
		float bunnyY = bunny.getY();
		float newX;
		float newY;
		float altX;
		float altY;

		int coll = checkCollision(xPos,yPos,x,y, COP_WIDTH, COP_HEIGHT);
		if (coll==0 && 
				xPos+x >= 0 && yPos+y >= 0 && 
				xPos+x <= LEVEL_WIDTH*LEVEL_TILESIZE && 
				yPos+y <= LEVEL_HEIGHT*LEVEL_TILESIZE)	{
			cop.move(x, y);
			cnewX = 0; cnewY = 0;
		} else {
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

			if (checkCollision(xPos,yPos,newX,newY, COP_WIDTH, COP_HEIGHT)==0 && 
					xPos+newX >= 0 && yPos+newY >= 0 && 
					xPos+newX <= LEVEL_WIDTH*LEVEL_TILESIZE && 
					yPos+newY <= LEVEL_HEIGHT*LEVEL_TILESIZE) {
				cop.move(newX, newY);
				cnewX = newX; cnewY = newY;
			}
			else if (checkCollision(xPos,yPos,altX,altY, COP_WIDTH, COP_HEIGHT)==0 && 
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

	private int checkCollision(float xPos, float yPos, float xwise, float ywise, float tabletWidth, float tabletHeight) {
		//find out which tile we want to go to
		//ul ur ol or
		int result = 0;
		int b1=0, b2=0, b3=0, b4=0, c1=0, c2=0, c3=0, c4=0, d1=0, d2=0, d3=0, d4=0;
		
		// calculate where we would be if we go xwise&ywise(d), only xwise(b) and only ywise(c)
		xPos += xwise;
		int btile1_x = (int)((xPos) / (float)LEVEL_TILESIZE), btile1_y = (int)((yPos) / (float)LEVEL_TILESIZE), btile2_x = (int)((xPos+tabletWidth) / (float)LEVEL_TILESIZE), btile2_y = (int)((yPos) / (float)LEVEL_TILESIZE), btile3_x = (int)((xPos) / (float)LEVEL_TILESIZE), btile3_y = (int)((yPos+tabletHeight) / (float)LEVEL_TILESIZE), btile4_x = (int)((xPos+tabletWidth) / (float)LEVEL_TILESIZE), btile4_y = (int)((yPos+tabletHeight) / (float)LEVEL_TILESIZE);
		xPos -= xwise;
		yPos += ywise;
		int ctile1_x = (int)((xPos) / (float)LEVEL_TILESIZE), ctile1_y = (int)((yPos) / (float)LEVEL_TILESIZE), ctile2_x = (int)((xPos+tabletWidth) / (float)LEVEL_TILESIZE), ctile2_y = (int)((yPos) / (float)LEVEL_TILESIZE), ctile3_x = (int)((xPos) / (float)LEVEL_TILESIZE), ctile3_y = (int)((yPos+tabletHeight) / (float)LEVEL_TILESIZE), ctile4_x = (int)((xPos+tabletWidth) / (float)LEVEL_TILESIZE), ctile4_y = (int)((yPos+tabletHeight) / (float)LEVEL_TILESIZE);
		xPos += xwise;
		int dtile1_x = (int)((xPos) / (float)LEVEL_TILESIZE), dtile1_y = (int)((yPos) / (float)LEVEL_TILESIZE), dtile2_x = (int)((xPos+tabletWidth) / (float)LEVEL_TILESIZE), dtile2_y = (int)((yPos) / (float)LEVEL_TILESIZE), dtile3_x = (int)((xPos) / (float)LEVEL_TILESIZE), dtile3_y = (int)((yPos+tabletHeight) / (float)LEVEL_TILESIZE), dtile4_x = (int)((xPos+tabletWidth) / (float)LEVEL_TILESIZE), dtile4_y = (int)((yPos+tabletHeight) / (float)LEVEL_TILESIZE);
		
		if (xPos <= 0) result |= 2;
		if (xPos+tabletWidth >= LEVEL_WIDTH*LEVEL_TILESIZE) result |= 1;
		if (yPos <= 0) result |= 8;
		if (yPos+tabletHeight >= LEVEL_HEIGHT*LEVEL_TILESIZE) result |= 4;
		
		// check if we're out of boundaries in any of those cases
		if (btile1_x < 0 || btile2_x < 0 || btile3_x < 0 || btile4_x < 0) result |= 2;
		if (btile1_x >= LEVEL_WIDTH || btile2_x >= LEVEL_WIDTH || btile3_x >= LEVEL_WIDTH || btile4_x >= LEVEL_WIDTH) result |= 1;
		if (ctile1_y < 0 || ctile2_y < 0 || ctile3_y < 0 || ctile4_y < 0) result |= 8;
		if (ctile1_y >= LEVEL_HEIGHT || ctile2_y >= LEVEL_HEIGHT || ctile3_y >= LEVEL_HEIGHT || ctile4_y >= LEVEL_HEIGHT) result |= 4;
		
		if ((result & 1) > 0) { b2=20; b4=20; d2=20; d4=20; }
		if ((result & 2) > 0) { b1=20; b3=20; d1=20; d3=20; }
		if ((result & 4) > 0) { c3=20; c4=20; d3=20; d4=20; }
		if ((result & 8) > 0) { c1=20; c2=20; d3=20; d4=20; }
		if (btile1_y < 0 || btile1_y >= LEVEL_HEIGHT || btile1_x < 0 || btile1_x >= LEVEL_WIDTH) b1 = 20;
		if (btile2_y < 0 || btile2_y >= LEVEL_HEIGHT || btile2_x < 0 || btile2_x >= LEVEL_WIDTH) b2 = 20;
		if (btile3_y < 0 || btile3_y >= LEVEL_HEIGHT || btile3_x < 0 || btile3_x >= LEVEL_WIDTH) b3 = 20;
		if (btile4_y < 0 || btile4_y >= LEVEL_HEIGHT || btile4_x < 0 || btile4_x >= LEVEL_WIDTH) b4 = 20;
		if (ctile1_y < 0 || ctile1_y >= LEVEL_HEIGHT || ctile1_x < 0 || ctile1_x >= LEVEL_WIDTH) c1 = 20;
		if (ctile2_y < 0 || ctile2_y >= LEVEL_HEIGHT || ctile2_x < 0 || ctile2_x >= LEVEL_WIDTH) c2 = 20;
		if (ctile3_y < 0 || ctile3_y >= LEVEL_HEIGHT || ctile3_x < 0 || ctile3_x >= LEVEL_WIDTH) c3 = 20;
		if (ctile4_y < 0 || ctile4_y >= LEVEL_HEIGHT || ctile4_x < 0 || ctile4_x >= LEVEL_WIDTH) c4 = 20;
		if (dtile1_y < 0 || dtile1_y >= LEVEL_HEIGHT || dtile1_x < 0 || dtile1_x >= LEVEL_WIDTH) d1 = 20;
		if (dtile2_y < 0 || dtile2_y >= LEVEL_HEIGHT || dtile2_x < 0 || dtile2_x >= LEVEL_WIDTH) d2 = 20;
		if (dtile3_y < 0 || dtile3_y >= LEVEL_HEIGHT || dtile3_x < 0 || dtile3_x >= LEVEL_WIDTH) d3 = 20;
		if (dtile4_y < 0 || dtile4_y >= LEVEL_HEIGHT || dtile4_x < 0 || dtile4_x >= LEVEL_WIDTH) d4 = 20;
		
		// now let's see what's there
		if (b1 != 20) b1 = levelMap[btile1_y][btile1_x]; 
		if (b2 != 20) b2 = levelMap[btile2_y][btile2_x]; 
		if (b3 != 20) b3 = levelMap[btile3_y][btile3_x]; 
		if (b4 != 20) b4 = levelMap[btile4_y][btile4_x];
		if (c1 != 20) c1 = levelMap[ctile1_y][ctile1_x]; 
		if (c2 != 20) c2 = levelMap[ctile2_y][ctile2_x]; 
		if (c3 != 20) c3 = levelMap[ctile3_y][ctile3_x]; 
		if (c4 != 20) c4 = levelMap[ctile4_y][ctile4_x];
		if (d1 != 20) d1 = levelMap[dtile1_y][dtile1_x]; 
		if (d2 != 20) d2 = levelMap[dtile2_y][dtile2_x]; 
		if (d3 != 20) d3 = levelMap[dtile3_y][dtile3_x]; 
		if (d4 != 20) d4 = levelMap[dtile4_y][dtile4_x];
		
		// rechts=1 links=2 oben=4 unten=8
		if (d1 >= 8 || d1 == 0 || d2 >= 8 || d2 == 0 || d3 >= 8 || d3 == 0 || d4 >= 8 || d4 == 0) {
			//only xwise
			if (b1 >= 8 || b1 == 0 || b2 >= 8 || b2 == 0 || b3 >= 8 || b3 == 0 || b4 >= 8 || b4 == 0) {
				if (xwise > 0) result |= 1;
				else result |= 2;
			}
			//only ywise
			if (c1 >= 8 || c1 == 0 || c2 >= 8 || c2 == 0 || c3 >= 8 || c3 == 0 || c4 >= 8 || c4 == 0) {
				if (ywise > 0) result |= 4;
				else result |= 8;
			}
		}
		return result;
	}

	private void bunnyWasCaught () {
		score -= crime*crimeCounter;
		crimeCounter = 0;
		crime = 0;
		//poff
		makeCopsPuff = 1;
		if (score <= 0) endGame();
		updateScore();
	}

	private void handleCop(Tablet cop) {
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

			if (frameCounter%10==0) {
				if (dy<0) cop.changeTexture(manager.getTexture("cop_front_l"));
				else cop.changeTexture(manager.getTexture("cop_back_l"));
			} else if (frameCounter%10==5) {
				if (dy<0) cop.changeTexture(manager.getTexture("cop_front_r"));
				else cop.changeTexture(manager.getTexture("cop_back_r"));
			}

			if (bx != cx && by != cy) {
				moveCop(dx*COP_MOVEMENT_UNIT, dy*COP_MOVEMENT_UNIT, cop);

				//check if cop catches bunny
				if (d_len-3.0f <= CATCH_DISTANCE)
					bunnyWasCaught();

			}
		}
	}

	private void drawDigit(GL10 gl, int position, int offset) {
		manager.getGameObject(goldLUT.get(position)).setXY(offset*scale,screenHeight-(int)(30.0f*scale));		
		manager.getGameObject(goldLUT.get(position)).draw(gl);
	}

	private void drawTime(int minutes, int seconds) {
		int seconds_0 = seconds % 10;
		int seconds_00 = (int)((float)seconds/10.0f);
		manager.getGameObject(goldLUT.get(minutes)).setXY(20*scale, screenHeight-(int)(60.0f*scale));
		manager.getGameObject(goldLUT.get(minutes)).draw(gl);
		manager.getGameObject(goldLUT.get(seconds_00)).setXY(40*scale, screenHeight-(int)(60.0f*scale));
		manager.getGameObject(goldLUT.get(seconds_00)).draw(gl);
		manager.getGameObject(goldLUT.get(seconds_0)).setXY(50*scale, screenHeight-(int)(60.0f*scale));
		manager.getGameObject(goldLUT.get(seconds_0)).draw(gl);
	}

	private void endGame() {
		SessionState sessionState = glv.getState();
		((LevelActivity)context).setResult(Activity.RESULT_OK, sessionState.asIntent());
		((LevelActivity)context).finish();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		int seconds = 0;
		int minutes = 0;
		int currentTime;

		if (startFrameTime == 0) startFrameTime = System.currentTimeMillis();
		currentTime = (int)(System.currentTimeMillis()-startFrameTime) / 1000;
		if (currentTime < 60) {
			minutes = 1;
			seconds = 59 - currentTime;
		} else if (currentTime < 119) {
			minutes = 0;
			seconds = 120 - currentTime;
		} else {
			endGame();
		}

		if (frameCounter%10==9) {
			if (oldFrameTime!=0) fps = 10.0f/(float)(System.currentTimeMillis()-oldFrameTime)*1000.0f;
			else fps = 100.0f;
			oldFrameTime = System.currentTimeMillis();
		}
		COP_MOVEMENT_UNIT = COP_DEFAULT_MOVEMENT_UNIT * DEFAULT_FPS/fps;
		BUNNY_MOVEMENT_UNIT = BUNNY_DEFAULT_MOVEMENT_UNIT * DEFAULT_FPS/fps;

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -1.0f);


		// update
//		if (keystates[0]) moveBunny(-BUNNY_MOVEMENT_UNIT, 0);
//		if (keystates[1]) moveBunny(BUNNY_MOVEMENT_UNIT, 0);
//		if (keystates[2]) moveBunny(0, BUNNY_MOVEMENT_UNIT);
//		if (keystates[3]) moveBunny(0, -BUNNY_MOVEMENT_UNIT);
		if (keystates[0] != 0.0f && keystates[2] != 0.0f) moveBunny(keystates[0], keystates[2]);

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
		drawLock = true;
		Iterator<Tablet> it = actiontextures.iterator();
		while (it.hasNext()) {
			it.next().draw(gl);
		}

		// render
		Iterator<Tablet> copIt = cops.iterator();
		while (copIt.hasNext()) {
			Tablet currentCop = copIt.next();
			if (makeCopsPuff != 0) currentCop.changeTexture(manager.getTexture("cloud"+makeCopsPuff));
			handleCop(currentCop);

			if(currentCop.getY() > bunny.getY())
				currentCop.draw(gl);
		}

		bunny.draw(gl);

		copIt = cops.iterator();
		while (copIt.hasNext()) {
			Tablet currentCop = copIt.next();
			if (makeCopsPuff != 0 && frameCounter%20==0) {
				currentCop.changeTexture(manager.getTexture("cloud"+makeCopsPuff));
				if (makeCopsPuff < 3) makeCopsPuff++;
			}
			if(currentCop.getY() <= bunny.getY())
				currentCop.draw(gl);
		}

		drawLock = false;
		if (makeCopsPuff == 3) {
			cops.clear();
			makeCopsPuff = 0;
		}

		//write score
		gold.draw(gl);
		int offset = 0;
		for (int i = 1; i<4; i++) {
			switch (i) {
			case 1:
				offset = 25;
				drawDigit(gl,gold_000, offset);
			case 2:
				offset = 35;
				drawDigit(gl,gold_00, offset);
			case 3:
				offset = 45;
				drawDigit(gl,gold_0, offset);
			}
		}

		drawTime(minutes, seconds);
		control.draw(gl);

		frameCounter++;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.gl = gl;
		screenWidth = width;
		screenHeight = height;
		Tablet.setResolution(width, height);
		if (width > height) scale = (float)width/(float)Tablet.INTENDED_RES_X;
		else scale = (float)height/(float)Tablet.INTENDED_RES_Y;
		CONTROL_X = screenWidth - (int)((float)CONTROL_SIZE*scale);
		CONTROL_Y = 0;

		gold.setXY(5, screenHeight-(int)(30.0f*scale));
		control.setXY(CONTROL_X, CONTROL_Y);

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
		bunny = manager.getGameObject("bunny_front_l");

		gold = manager.getGameObject("gold");
		gold_000 = 1;
		gold_00 = 0;
		gold_0 = 0;

		control = manager.getGameObject("control");
		//button = manager.getGameObject("button");
		
		oldFrameTime = 0;
		startFrameTime = 0;
		frameCounter = 0;
		fps = 100.0f;
		
		if (mSavedInstance != null && mSavedInstance.containsKey(MAP_OFFSET_X) && mSavedInstance.containsKey(MAP_OFFSET_Y) &&
				mSavedInstance.containsKey(BUNNY_X) && mSavedInstance.containsKey(BUNNY_Y) && mSavedInstance.containsKey(COP_NUMBER) &&
				mSavedInstance.containsKey(SCORE) && mSavedInstance.containsKey(CRIME) && mSavedInstance.containsKey(STARTFRAMETIME))
		{
			mapOffset_x = mSavedInstance.getFloat(MAP_OFFSET_X);
			mapOffset_y = mSavedInstance.getFloat(MAP_OFFSET_Y);

			startFrameTime = mSavedInstance.getLong(STARTFRAMETIME) - (System.currentTimeMillis() - mSavedInstance.getLong(PAUSETIME)); 
				
			Log.d("LevelRenderer", "Load mapOffsetx: " + mSavedInstance.getFloat(MAP_OFFSET_X));
			Log.d("LevelRenderer", "Load mapOffsety: " + mSavedInstance.getFloat(MAP_OFFSET_Y));

			Tablet.setMapOffset(mapOffset_x, mapOffset_y);
						
			bunny.setXY(mSavedInstance.getFloat(BUNNY_X), mSavedInstance.getFloat(BUNNY_Y));
			copCounter = mSavedInstance.getInt(COP_NUMBER);
			
			for (int i=0;i<copCounter;i++)
				cops.add(new Tablet(context, COP_WIDTH, COP_HEIGHT, mSavedInstance.getFloat(COP_X + i), mSavedInstance.getFloat(COP_Y + i), manager.getTexture("cop_front_l"), gl));

			Log.d("LevelRenderer", "Load bunny pos: x " + mSavedInstance.getFloat(BUNNY_X) + " y " + mSavedInstance.getFloat(BUNNY_Y));
			Log.d("LevelRenderer", "Load cop pos: x " + mSavedInstance.getFloat(COP_X) + " y " + mSavedInstance.getFloat(COP_Y));
		
			score = mSavedInstance.getInt(SCORE);
			crime = mSavedInstance.getInt(CRIME);

			Log.d("LevelRenderer", "Load score: " + score);
			Log.d("LevelRenderer", "Load crime: " + crime);
		
			updateScore();
		} else {
			score = 100;
			mapOffset_x = mapOffset_y = 0;
			bunny.setXY(100, 20);
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
		outState.putFloat(MAP_OFFSET_X, mapOffset_x);
		outState.putFloat(MAP_OFFSET_Y, mapOffset_y);
		outState.putInt(SCORE, score);
		outState.putInt(CRIME, crime);
		outState.putInt(COP_NUMBER, cops.size());
		outState.putLong(STARTFRAMETIME, startFrameTime);
		outState.putLong(PAUSETIME, System.currentTimeMillis());
		
		Iterator<Tablet> ci = cops.iterator();
		int i=0;
		while (ci.hasNext()) {
			Tablet c = ci.next();
			outState.putFloat(COP_X + i, c.getX());
			outState.putFloat(COP_Y + i, c.getY());
			i++;
		}

		Log.d("LevelRenderer", "Save bunny pos: x " + bunny.getX() + " y " + bunny.getY());
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
