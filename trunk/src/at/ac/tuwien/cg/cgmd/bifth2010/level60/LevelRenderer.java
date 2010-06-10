package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.app.Activity;
import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

@SuppressWarnings({ "serial" })
public class LevelRenderer implements Renderer {
	private Context context;
	private Bundle mSavedInstance;
	private GL10 gl;

	private final float BUNNY_DEFAULT_MOVEMENT_UNIT = 6.0f;
	private final float COP_DEFAULT_MOVEMENT_UNIT = 4.0f;
	private final float DEFAULT_FPS = 22.0f;
	public final static int BUNNY_WIDTH = 45;
	public final static int BUNNY_HEIGHT = 50;
	public final static int BUNNY_INIT_X = 0;
	public final static int BUNNY_INIT_Y = 0;
	public final static int COP_WIDTH = 45;
	public final static int COP_HEIGHT = 50;
	public final static int LEVEL_WIDTH = 9;
	public final static int LEVEL_HEIGHT = 7;
	public final static int LEVEL_TILESIZE = 100;
	public final static int CONTROL_SIZE = 150;
	private final static float CATCH_DISTANCE = COP_WIDTH/2;
	private final static int NUMBER_OF_CARS = 2;
	private final static int CAR_LENGTH = 70;
	private final static int CAR_WIDTH = 30;
	
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
	private ArrayList<GameObject> cars;
	private Tablet bunny;
	private Tablet gold;
	private Tablet control;
	private int gold_000;
	private int gold_00;
	private int gold_0;

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
		{ 3, 1, 1, 1, 1, 1, 1, 1, 3},
		{ 2,10,10, 2, 9, 0,11, 8, 3},
		{ 3, 1, 1, 2, 0, 0, 0, 1, 3},
		{ 3, 9, 8, 3, 9, 0,11, 8, 2},
		{ 3, 1, 1, 3, 1, 3, 1, 1, 3},
		{ 2,10,11, 2, 8, 2, 9,11, 2},
		{ 3, 1, 1, 3, 1, 1, 1, 1, 3}
	};

	private final HashMap<Integer, String> tileLUT = new HashMap<Integer, String>(){
		{
			put(0, "blank");
			put(1, "streetHor");
			put(2, "streetVer");
			put(3, "intersection");
			put(4, "TintersectionTop");
			put(5, "TintersectionBottom");
			put(6, "TintersectionLeft");
			put(7, "TintersectionRight");
			put(8, "houseGlass");
			put(9, "houseDoor1");
			put(10, "houseDoor2");
			put(11, "houseWall");
			put(12, "housefl");
			put(13, "housefr");
			put(14, "housebl");
			put(15, "housebr");
			put(16, "housecl");
			put(17, "housecr");
			put(20, "blank");
		}
	};

	private final HashMap<Integer, String> goldLUT = new HashMap<Integer, String>(){
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
		mSavedInstance = msavedinstance;
		for (int i=0;i<4;i++) keystates[i] = 0;
		actiontextures = new ArrayList<Tablet>();
		cops = new ArrayList<Tablet>();
		cars = new ArrayList<GameObject>();
	}

	public void setKey (int code, float amount) {	keystates[code] = amount; }
	public void releaseKey(int code) { keystates[code] = 0; }

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
		Point bunnyPos = new Point(xPos, yPos);

		Iterator<Tablet> it = actiontextures.iterator();
		boolean proximity = false;
		boolean acted = false;

		while(it.hasNext()) {
			Tablet t = it.next();
			if (Math.abs(t.getX() - xPos) < LEVEL_TILESIZE/2 && Math.abs(t.getY() - yPos) < LEVEL_TILESIZE/2) proximity = true; 
		}

		if (!proximity && !drawLock) {
			Iterator<GameObject>git = cars.iterator();
			float leastdistance = LEVEL_WIDTH*LEVEL_TILESIZE;
			GameObject n = cars.get(0); 
			while (git.hasNext()) {
				GameObject t = git.next();
				Point tabletpos = new Point(t.getX()+t.getWidth()/2.0f, t.getY()+t.getHeight()/2.0f);
				if (Point.distance(tabletpos, bunnyPos) < leastdistance) {
					leastdistance = Point.distance(tabletpos, bunnyPos);
					n = t;
				}
			}
			if (leastdistance <= LEVEL_TILESIZE && n.getTexture() == manager.getTexture("car0")) {
				//destroy the car
				n.destroy();
				acted = true;
				crime += 30;
				crimeCounter++;
			}
			
			if (!acted) {
				// 	put spraytag tablet here 
				actiontextures.add(new Tablet(36, 36, (int)xPos-18, (int)yPos-18, manager.getTexture("spraytag")));
				acted = true;
				crime += 5;
				crimeCounter++;
			}
			
			if (acted) {	
				if (copCounter == 0 || Math.ceil((float)crime/20.0f)>copCounter) {
					cops.add(new Tablet(COP_WIDTH, COP_HEIGHT, 10, 10, manager.getTexture("cop_front_l")));
					copCounter++;
				}
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
			if (y<0 && (bunny.getTexture() == manager.getTexture("bunny_back_l") || bunny.getTexture() == manager.getTexture("bunny_back_r"))) {
				bunny.changeTexture(manager.getTexture("bunny_front_l"));
				bunny.setXY(bunny.getX()+5*scale, bunny.getY());

			} else if (y>0 && (bunny.getTexture() == manager.getTexture("bunny_front_l") || bunny.getTexture() == manager.getTexture("bunny_front_r"))) {
				bunny.changeTexture(manager.getTexture("bunny_back_l"));
				bunny.setXY(bunny.getX()-5*scale, bunny.getY());

			}

		} else if (frameCounter%10==5) {
			if (y<0 && (bunny.getTexture() == manager.getTexture("bunny_back_l") || bunny.getTexture() == manager.getTexture("bunny_back_r"))) {
				bunny.changeTexture(manager.getTexture("bunny_front_r"));
				bunny.setXY(bunny.getX()+5*scale, bunny.getY());
			} else if (y>0 && (bunny.getTexture() == manager.getTexture("bunny_front_l") || bunny.getTexture() == manager.getTexture("bunny_front_r"))) {
				bunny.changeTexture(manager.getTexture("bunny_back_r"));
				bunny.setXY(bunny.getX()-5*scale, bunny.getY());
			}
		}

		int coll = checkCollision(xPos,yPos,x,y, BUNNY_WIDTH, BUNNY_HEIGHT); 
		if (coll == Point.IDENT)	{
			bunny.move(x, y);
			myX = x; myY = y;
		} else {
			//strafe along edges
			if (x > 0) newX = BUNNY_MOVEMENT_UNIT;
			else if (x < 0) newX = -BUNNY_MOVEMENT_UNIT;
			else newX = 0;
			if (y > 0) newY = BUNNY_MOVEMENT_UNIT;
			else if (y < 0) newY = -BUNNY_MOVEMENT_UNIT;
			else newY = 0;
			
			if ((coll & Point.RIGHT) > 0 || (coll & Point.LEFT) > 0) newX = 0; // left/right
			if ((coll & Point.UPPER) > 0 || (coll & Point.LOWER) > 0) newY = 0; // up/down
				
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
		if (coll==Point.IDENT)	{
			cop.move(x, y);
			cnewX = 0; cnewY = 0;
		} else {
			if(Math.abs(y) >= Math.abs(x)) {
				if (bunnyX > xPos) { //RIGHT
					newX = COP_MOVEMENT_UNIT;
					newY = 0;
				} else { //LEFT
					newX = -COP_MOVEMENT_UNIT;
					newY = 0;
				}
				if (bunnyY > yPos) {//UP
					altX = 0;
					altY = COP_MOVEMENT_UNIT;
				} else { //DOWN
					altX = 0;
					altY = -COP_MOVEMENT_UNIT;
				}	
			} else {
				if (bunnyY > yPos) {//UP
					newX = 0;
					newY = COP_MOVEMENT_UNIT;
				} else { //DOWN
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

			if (checkCollision(xPos,yPos,newX,newY, COP_WIDTH, COP_HEIGHT)==Point.IDENT && 
					xPos+newX >= 0 && yPos+newY >= 0 && 
					xPos+newX <= LEVEL_WIDTH*LEVEL_TILESIZE && 
					yPos+newY <= LEVEL_HEIGHT*LEVEL_TILESIZE) {
				cop.move(newX, newY);
				cnewX = newX; cnewY = newY;
			} else if (checkCollision(xPos,yPos,altX,altY, COP_WIDTH, COP_HEIGHT)==Point.IDENT && 
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
		// returns directions in which collisions happen when moving by xwise/ywise (Point. LEFT/RIGHT/UPPER/LOWER)
		int result = 0;
		
		Rectangle targetPos = new Rectangle(new Point(xPos+xwise, yPos+ywise), tabletWidth, tabletHeight);
		Rectangle targetPosX = new Rectangle(new Point(xPos+xwise, yPos), tabletWidth, tabletHeight);
		Rectangle targetPosY = new Rectangle(new Point(xPos, yPos+ywise), tabletWidth, tabletHeight);

		// check world boundaries
		int ll = targetPos.getLowerLeft().getRelation(new Point(0, 0));
		int ur = targetPos.getUpperRight().getRelation(new Point(LEVEL_WIDTH*LEVEL_TILESIZE, LEVEL_HEIGHT*LEVEL_TILESIZE));
		if ((ll & Point.LEFT) > 0) result |= Point.LEFT;
		if ((ll & Point.LOWER) > 0) result |= Point.LOWER;
		if ((ur & Point.RIGHT) > 0) result |= Point.RIGHT;
		if ((ur & Point.UPPER) > 0) result |= Point.UPPER;
		
		// check against all blocking tiles
		for (int i=0;i<levelMap.length;i++) {
			for (int j=0;j<levelMap[i].length;j++) {
				if (levelMap[i][j] == 0 || levelMap[i][j] > 7) {	// only check against _blocking_ tiles
					Rectangle tile = new Rectangle(new Point(j*LEVEL_TILESIZE, i*LEVEL_TILESIZE), LEVEL_TILESIZE, LEVEL_TILESIZE);
					int ol = tile.overlap(targetPos);
					if (ol != Point.IDENT) {
						int olx = tile.overlap(targetPosX);
						int oly = tile.overlap(targetPosY);
						if (olx != Point.IDENT) {
							if (xwise > 0) result |= Point.RIGHT;
							else result |= Point.LEFT;
						}
						if (oly != Point.IDENT) {
							if (ywise > 0) result |= Point.UPPER;
							else result |= Point.LOWER;
						}
					}
				}
			}
		}
		
		// check against arbitrary objects -- cars
		Iterator<GameObject> it = cars.iterator();
		while (it.hasNext()) {
			Tablet c = it.next();
			Rectangle tile = new Rectangle(new Point(c.getX(), c.getY()), c.getWidth(), c.getHeight());
			int ol = tile.overlap(targetPos);
			if (ol != Point.IDENT) {
				int olx = tile.overlap(targetPosX);
				int oly = tile.overlap(targetPosY);
				if (olx != Point.IDENT) {
					if (xwise > 0) result |= Point.RIGHT;
					else result |= Point.LEFT;
				}
				if (oly != Point.IDENT) {
					if (ywise > 0) result |= Point.UPPER;
					else result |= Point.LOWER;
				}
			}
		}
		return result;
	}

	private void bunnyWasCaught () {
		score -= crime*((float)crimeCounter/(float)crimeCounter+2);
		crimeCounter = 0;
		crime = 0;
		copCounter = 0;
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
				if (dy<0 && (cop.getTexture() == manager.getTexture("cop_back_l") || cop.getTexture() == manager.getTexture("cop_back_r"))) {
					cop.changeTexture(manager.getTexture("cop_front_l"));
					cop.setXY(cop.getX()-5*scale, cop.getY());
				} else if (dy>0 && (cop.getTexture() == manager.getTexture("cop_front_l") || cop.getTexture() == manager.getTexture("cop_front_r"))) {
					cop.changeTexture(manager.getTexture("cop_back_l"));
					cop.setXY(cop.getX()+5*scale, cop.getY());
				}
			} else if (frameCounter%10==5) {
				if (dy<0 && (cop.getTexture() == manager.getTexture("cop_back_l") || cop.getTexture() == manager.getTexture("cop_back_r"))) {
					cop.changeTexture(manager.getTexture("cop_front_r"));
					cop.setXY(cop.getX()-5*scale, cop.getY());
				} else if (dy>0 && (cop.getTexture() == manager.getTexture("cop_front_l") || cop.getTexture() == manager.getTexture("cop_front_r"))) {
					cop.changeTexture(manager.getTexture("cop_back_r"));
					cop.setXY(cop.getX()+5*scale, cop.getY());
				}
			}

			if (bx != cx && by != cy) {
				moveCop(dx*COP_MOVEMENT_UNIT, dy*COP_MOVEMENT_UNIT, cop);
				if (d_len-3.0f <= CATCH_DISTANCE) bunnyWasCaught();
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
					block.setXY(j*LEVEL_TILESIZE, i*LEVEL_TILESIZE);
					block.draw(gl);
				}

			}
		}
		
		//draw cars
		Iterator<GameObject> it = cars.iterator();
		while (it.hasNext()) {
			GameObject n = it.next();
			n.update();	
			n.draw(gl);
		}

		//draw action stuff
		drawLock = true;
		Iterator<Tablet> tit = actiontextures.iterator();
		while (tit.hasNext()) {
			tit.next().draw(gl);
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
				cops.add(new Tablet(COP_WIDTH, COP_HEIGHT, mSavedInstance.getFloat(COP_X + i), mSavedInstance.getFloat(COP_Y + i), manager.getTexture("cop_front_l")));

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
			bunny.setXY(BUNNY_INIT_X, BUNNY_INIT_Y);
			Tablet.setMapOffset(mapOffset_x, mapOffset_y);
			
			//create cars
			Random rnd = new Random();
			int rnr1=0, rnr2=0;
			int oldcar1=0, oldcar2=0;
			for (int i=0;i<NUMBER_OF_CARS;i++) {
				do { 
					rnr1 = rnd.nextInt(LEVEL_WIDTH);
					rnr2 = rnd.nextInt(LEVEL_HEIGHT);
				}
				while ((rnr1 == 0 && rnr2 == 0 && (oldcar1 != rnr1 || oldcar2 != rnr2)) || 
					   (levelMap[rnr2][rnr1] < 1 || levelMap[rnr2][rnr1] > 7) ||
						levelMap[rnr2][rnr1] == 3);
				// now that we know which tile to put the car on, place it there
				GameObject newcar = new GameObject(GameObject.OBJECTCLASS_CAR, CAR_WIDTH, CAR_LENGTH, rnr1*LEVEL_TILESIZE*scale, rnr2*LEVEL_TILESIZE*scale, manager.getTexture("car0"), manager);
				if (levelMap[rnr2][rnr1] == 1 || levelMap[rnr2][rnr1] == 4 || levelMap[rnr2][rnr1] == 5) {
					newcar.rotate(90.0f);
					newcar.setWidthHeight(CAR_LENGTH, CAR_WIDTH);
					newcar.setOffset(CAR_LENGTH, 0);
				}
				cars.add(newcar);
			}
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
