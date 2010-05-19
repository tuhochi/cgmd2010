package at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GeometryFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.SpriteTexture;


/**
 * @author Christoph Winklhofer
 */
public class TrainGame {

	
	
	
	
	// ----------------------------------------------------------------------------------
	// -- Static Members ----
	
	private static String STATE_INIT_TILES  = "StateInitTiles";
	private static String STATE_GAME_TILES  = "StateGameTiles";
	private static String STATE_TRAIN_TILES = "StateTrainTiles";
	private static String STATE_HIGHLIGHT_TILES = "StateHighlightTiles";

	
	private static final float WORLD_WIDTH_HALF  = 5.0f;
	private static final float WORLD_HEIGHT_HALF = 3.0f;
	private static final float WORLD_RATIO  = WORLD_WIDTH_HALF / WORLD_HEIGHT_HALF;
	
	private static final int TILE_X   = 10;
	private static final int TILE_Y   = 6;
	private static final int TILE_CNT = 60;
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	
	private boolean isRotateView;	 //< Rotate view when height greater than width
	private float   screenWidth;	 //< Screen resolution - width
	private float   screenHeight;    //< Screen resolution - height
	private float   orthoWidthHalf;  //< Half world size - width
	private float   orthoHeightHalf; //< Half world size - height
	
	
	private ArrayList<TileEnum> initTiles; //< Initial game tiles
	private ArrayList<Tile>     gameTiles; //< Current game tiles
	private float               timeStart; //< Start time of the train
	
	private boolean isOpenGl; 		//< Is OpenGl initialized
	private SpriteTexture texTile;  //< Tile texture
	private SpriteTexture texTrain; //< Train texture
	
	private Train train;
	
	private Tile  tileSource; //< Selected source tile
	private Tile  tileTarget; //< Selected target tile
	
	

	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create the pipe game.
	 */
	public TrainGame(Bundle state, int width, int height) {
			
		isOpenGl = false;
		
		tileSource = null;
		tileTarget = null;
		
		initTiles = new ArrayList<TileEnum>(TILE_CNT);
		gameTiles = new ArrayList<Tile>(TILE_CNT);
		
		
		// Create new tiles
		createNewGame();
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	
	void createNewGame() {
		
		StringBuilder sbLevelConf = new StringBuilder();
		StringBuilder sbLevelGame = new StringBuilder();
		StringBuilder sbLevelInit = new StringBuilder();
		StringBuilder sbLevelRail = new StringBuilder();

		// -- Read level data from file --
		try {
			Context     ctx = LevelActivity.getInstance();
			InputStream is  = ctx.getResources().openRawResource(R.raw.l70_level1);
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			
			// Level configuration
			buf.readLine();
			sbLevelConf.append(buf.readLine());
			
			// Game tiles
			buf.readLine();
			sbLevelGame.append(buf.readLine());
			sbLevelGame.append(buf.readLine());
			sbLevelGame.append(buf.readLine());
			sbLevelGame.append(buf.readLine());
			sbLevelGame.append(buf.readLine());
			sbLevelGame.append(buf.readLine());
			
			// Init tiles
			buf.readLine();
			sbLevelInit.append(buf.readLine());
			sbLevelInit.append(buf.readLine());
			sbLevelInit.append(buf.readLine());
			sbLevelInit.append(buf.readLine());
			sbLevelInit.append(buf.readLine());
			sbLevelInit.append(buf.readLine());
			
			// Rail tiles
			buf.readLine();
			sbLevelRail.append(buf.readLine());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		String[] railCnt = sbLevelRail.toString().split(",");
		
		LinkedList<TileEnum> levelRails = new LinkedList<TileEnum>();
		levelRails.addAll(Collections.nCopies(Integer.parseInt(railCnt[0]), TileEnum.TILE_HORIZONTAL));
		levelRails.addAll(Collections.nCopies(Integer.parseInt(railCnt[1]), TileEnum.TILE_VERTICAL));
		levelRails.addAll(Collections.nCopies(Integer.parseInt(railCnt[2]), TileEnum.TILE_LEFT_UP));
		levelRails.addAll(Collections.nCopies(Integer.parseInt(railCnt[3]), TileEnum.TILE_LEFT_DOWN));
		levelRails.addAll(Collections.nCopies(Integer.parseInt(railCnt[4]), TileEnum.TILE_RIGHT_UP));
		levelRails.addAll(Collections.nCopies(Integer.parseInt(railCnt[5]), TileEnum.TILE_RIGHT_DOWN));
		Collections.shuffle(levelRails);
		
		
		// Fill game tiles and init tiles
		String[] levelGameTiles = sbLevelGame.toString().split(",");
		String[] levelInitTiles = sbLevelInit.toString().split(",");
		for (int i = 0; i < TILE_CNT; i++) {
			Tile tile = new Tile(this);
			tile.setType(TileEnum.valueOf(Integer.parseInt(levelGameTiles[i])));
			gameTiles.add(tile);
			if (levelInitTiles[i].equals("r")) {
				initTiles.add(levelRails.remove());
			}
			else {
				initTiles.add(TileEnum.valueOf(Integer.parseInt(levelInitTiles[i])));
			}
		}
		
		// 
		String[] levelConf = sbLevelConf.toString().split(",");
		int iyTile = Integer.parseInt(levelConf[0]);
		int iGoal  = Integer.parseInt(levelConf[1]);
		float time = Float.parseFloat(levelConf[2]);
		timeStart  = Float.parseFloat(levelConf[3]);
		
		gameTiles.get(iGoal).setIsGoal(true);
		
		train = new Train(this, time, 1, iyTile, 0);

		
		//train = new Train(this, time, -2, iyTile, 2.0f / 8.0f * time);
		//trains.add(train);
		
		//train = new Train(this, time, -3, iyTile, 4.0f / 8.0f * time);
		//trains.add(train);
		
		//train = new Train(this, time, -4, iyTile, 6.0f / 8.0f * time);
		//train.setLast(true);
		//trains.add(train);
		
		/***
		for (int i = 0; i < TILE_CNT; i++) {
			gameTiles.add(new Tile());
			initTiles.add(TileEnum.TILE_GRASS);
		}
		gameTiles.get(0).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(10).setType(TileEnum.TILE_ROCK);
		gameTiles.get(20).setType(TileEnum.TILE_ROCK);
		gameTiles.get(30).setType(TileEnum.TILE_ROCK);
		gameTiles.get(40).setType(TileEnum.TILE_ROCK);
		gameTiles.get(50).setType(TileEnum.TILE_ROCK);
		gameTiles.get(1).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(11).setType(TileEnum.TILE_ROCK);
		gameTiles.get(21).setType(TileEnum.TILE_ROCK);
		gameTiles.get(31).setType(TileEnum.TILE_ROCK);
		gameTiles.get(41).setType(TileEnum.TILE_ROCK);
		gameTiles.get(51).setType(TileEnum.TILE_ROCK);
		
		gameTiles.get(8).setType(TileEnum.TILE_TREE);
		gameTiles.get(18).setType(TileEnum.TILE_TREE);
		gameTiles.get(28).setType(TileEnum.TILE_TREE);
		gameTiles.get(38).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(48).setType(TileEnum.TILE_TREE);
		gameTiles.get(58).setType(TileEnum.TILE_TREE);
		gameTiles.get(9).setType(TileEnum.TILE_ROCK);
		gameTiles.get(19).setType(TileEnum.TILE_ROCK);
		gameTiles.get(29).setType(TileEnum.TILE_ROCK);
		gameTiles.get(39).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(49).setType(TileEnum.TILE_ROCK);
		gameTiles.get(59).setType(TileEnum.TILE_ROCK);
		
		
		gameTiles.get(0).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(1).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(2).setType(TileEnum.TILE_LEFT_UP);
		gameTiles.get(12).setType(TileEnum.TILE_VERTICAL);
		gameTiles.get(22).setType(TileEnum.TILE_VERTICAL);
		gameTiles.get(32).setType(TileEnum.TILE_RIGHT_DOWN);
		gameTiles.get(33).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(34).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(35).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(36).setType(TileEnum.TILE_LEFT_DOWN);
		gameTiles.get(26).setType(TileEnum.TILE_LEFT_UP);
		gameTiles.get(25).setType(TileEnum.TILE_RIGHT_DOWN);
		gameTiles.get(15).setType(TileEnum.TILE_VERTICAL);
		gameTiles.get(5).setType(TileEnum.TILE_RIGHT_UP);
		gameTiles.get(6).setType(TileEnum.TILE_HORIZONTAL);
		gameTiles.get(7).setType(TileEnum.TILE_HORIZONTAL);
		train = new Train(this, -4.0f, 1, 0);
		wagoon = new Train(this, -5.0f, 0, 1.3f);
		***/
	}
	
	/**
	 * Create all OpenGL specific members.
	 */
	public void createOpenGl(GL10 gl) {
		
		texTile = new SpriteTexture(R.drawable.l70_tiles, 64, 13);
		
		for (int iy = 0; iy < TILE_Y; iy++) {
			for (int ix = 0; ix < TILE_X; ix++) {
				float cx = -4.5f + ix;
				float cy = -2.5f + iy;
				gameTiles.get(iy * TILE_X + ix).createOpenGl(cx, cy, texTile);
			}
		}
		
		texTrain = new SpriteTexture(R.drawable.l70_train, 64, 5);
		train.createOpenGl(texTrain);
		
		//wagoon.createOpenGl(0, 0, texTrain);
		//wagoon.setTexBuffer(3);
		
		
		isOpenGl = true;
	}
	
	/**
	 * Set screen dimension.
	 * @param width The screen width.
	 * @param height The screen height.
	 */
	public void setScreen(GL10 gl, int width, int height) {
		
		// Set screen width and height
		screenWidth  = width;
		screenHeight = height;
		
		// Find maximum as width and set minimum as height.
		float w  = Math.max(width, height);
		float h = Math.min(width, height);
		float ratio = w / h;
		if (ratio < WORLD_RATIO + 0.001f) {
			orthoWidthHalf  = WORLD_WIDTH_HALF;
			orthoHeightHalf = orthoWidthHalf / ratio;
		}
		else {
			orthoHeightHalf = WORLD_HEIGHT_HALF;
			orthoWidthHalf  = orthoHeightHalf * ratio;
		}
					
		// Rotate screen if width is smaller than height and swap width and height.
		if (width < height) {
			isRotateView = true;
			float temp      = orthoWidthHalf;
			orthoWidthHalf  = orthoHeightHalf;
			orthoHeightHalf = temp;
		}
		else {
			isRotateView = false;
		}
		
		// Set viewport and ortho projection
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-orthoWidthHalf, orthoWidthHalf, -orthoHeightHalf, orthoHeightHalf, -1.0f, 1.0f);
	}
	
	
	public Tile getTile(int ix, int iy) {
		return gameTiles.get(iy * TILE_X + ix);
	}
	
	public void update(float dt) {
		if (!isOpenGl) {
			return;
		}
		
		if (timeStart > 0) {
			timeStart -= dt;
		} 
		else {
			train.update(dt);
		}
		
		for (Tile it : gameTiles) {
			it.update(dt);
		}
	}
	
	
	/**
	 * Draw method for the pipe game.
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		if (isRotateView) {
			gl.glRotatef(90, 0, 0, 1);
		}

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glActiveTexture(0);
		texTile.bind();
		for (Tile it : gameTiles) {
			it.draw(gl);
		}
		
		texTile.unbind();
		
		texTrain.bind();
		train.draw(gl);
		texTrain.unbind();
		
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	
	public void onCheckGameState() {
		if (train.isGoalReached()) {
			train.setTileTime(2.0f);
		}
	}
	
	public void onGameover() {
	    LevelActivity.getInstance().handler.post(LevelActivity
                .getInstance().fpsUpdateRunnable);
	}
	
	public void onMotionEvent(MotionEvent event) {
		
		int index = getIndex(event.getRawX(), event.getRawY());
		
		// No tile selected
		if (index == -1) {
			tileSource = deselect(tileSource);
			tileTarget = deselect(tileTarget);
			return;
		}
		
		Tile tile = gameTiles.get(index);
		
		// Tile selectable
		if (!tile.isSelectable()) {
			tileSource = deselect(tileSource);
			tileTarget = deselect(tileTarget);
			return;
		}
		
		if (tile.isInitial()) {
			// Switch initial tile
			if (tile == tileTarget) {
				TileEnum next = initTiles.get(index);
				tile.setStateSwitch(next);
				tileTarget = null;
			}
			// Highlight initial tile
			else {
				tileSource = deselect(tileSource);
				tileTarget = deselect(tileTarget);
				tileTarget = tile;
				tileTarget.setStateSelect();
			}
		}
		else {
			if (tileTarget != null) {
				if (tile == tileSource || tile == tileTarget) {
					// Switch source and target tile
					tileSource.setStateSwitch(tileTarget.getType());
					tileTarget.setStateSwitch(tileSource.getType());
					tileSource = null;
					tileTarget = null;
				}
				else {
					// Deselect - When another tile is selected
					tileSource = deselect(tileSource);
					tileTarget = deselect(tileTarget);
					
					// Select source tile 
					tileSource = tile;
					tileSource.setStateSelect();
				}
			}
			else if (tileSource != null) {
				if (tile == tileSource) {
					// Deselect - Source tile is selected again
					tileSource = deselect(tileSource);
				}
				else {
					// Select target tile
					tileTarget = tile;
					tileTarget.setStateSelect();
				}
			}
			else {
				// Select source tile 
				tileSource = tile;
				tileSource.setStateSelect();
			}
		}
	}
	
	
	
	/**
     * Save actual game state. Store initTiles and gameTiles.
     * @param outState The game state
     */
    public void onSaveState(Bundle state) {

        int[] tiles = new int[initTiles.size()];
    	for (int i = 0; i < tiles.length; i++) {
    		tiles[i] = initTiles.get(i).ordinal();
    	}
    	state.putIntArray(STATE_INIT_TILES, tiles);
    	
    	ArrayList<Integer> trainTiles = new ArrayList<Integer>();
    	int[] highlights = {-1, -1};
    	tiles = new int[gameTiles.size()];
    	for (int i = 0; i < tiles.length; i++) {
    	    Tile tile = gameTiles.get(i);
    		tiles[i] = tile.getType().ordinal();
    		if (tile.isStateTrain()) {
    		    trainTiles.add(i);
    		}
    		if (tile == tileSource) {
    		    highlights[0] = i;
    		}
    		if (tile == tileTarget) {
                highlights[1] = i;
            }
    	}
    	state.putIntArray(STATE_GAME_TILES, tiles);
    	state.putIntArray(STATE_HIGHLIGHT_TILES, highlights);
    	state.putIntegerArrayList(STATE_TRAIN_TILES, trainTiles);
    	train.onSaveState(state);
    }
    
    
    /**
     * Restore actual game stat.
     * @param outState The game state
     */
    public void onRestoreState(Bundle state) {
    	if (state != null) {
			int[] stateInit = state.getIntArray(STATE_INIT_TILES);
			int[] stateGame = state.getIntArray(STATE_GAME_TILES);
			int[] stateHighlight = state.getIntArray(STATE_HIGHLIGHT_TILES);
			if (stateInit != null && stateGame != null) {
				for (int i = 0; i < TILE_CNT; i++) {
					initTiles.set(i, TileEnum.valueOf(stateInit[i]));
					gameTiles.get(i).setType(TileEnum.valueOf(stateGame[i]));
				}
				ArrayList<Integer> trainTiles = state.getIntegerArrayList(STATE_TRAIN_TILES);
				for (Integer it : trainTiles) {
				    gameTiles.get(it).setStateTrain();
				}
				if (stateHighlight[0] != -1) {
				    tileSource = gameTiles.get(stateHighlight[0]);
				    tileSource.setStateRestoreHighlight();
				}
				if (stateHighlight[1] != -1) {
                    tileTarget = gameTiles.get(stateHighlight[1]);
                    tileTarget.setStateRestoreHighlight();
                }
			}
			train.onRestoreState(state);
		}
    }
	
    
    /*public TileEnum getTile(float wx, float wy) {

    	if (wx > -WORLD_WIDTH_HALF  && wx < WORLD_WIDTH_HALF && 
    		wy > -WORLD_HEIGHT_HALF && wy < WORLD_HEIGHT_HALF) {
    		int ix = (int)(wx + WORLD_WIDTH_HALF);
    		int iy = (int)(wy + WORLD_HEIGHT_HALF);
    		return gameTiles.get(iy * TILE_X + ix);
    	}
    	return TileEnum.TILE_HORIZONTAL;

    }*/
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----
	
	/**
	 * Return the index of the selected tile during the onClick event.
	 * @param x The screen x click coordinate 
	 * @param y The screen y click coordinate.
	 * @return Tile index or -1 if no tile was hit.
	 */
	private int getIndex(float x, float y) {
		
		// Calculate the world coordinate.
		float wx, wy;
		if (isRotateView) {
			wx = y * -2 * orthoHeightHalf / screenHeight + orthoHeightHalf;
			wy = x * -2 * orthoWidthHalf / screenWidth + orthoWidthHalf;
		}
		else {
			wx = x *  2 * orthoWidthHalf / screenWidth - orthoWidthHalf;
			wy = y * -2 * orthoHeightHalf / screenHeight + orthoHeightHalf;
		}
		  
		// Inside the world bounding box
		if (wx > -WORLD_WIDTH_HALF  && wx < WORLD_WIDTH_HALF && 
			wy > -WORLD_HEIGHT_HALF && wy < WORLD_HEIGHT_HALF) {
			int ix = (int)(wx + WORLD_WIDTH_HALF);
			int iy = (int)(wy + WORLD_HEIGHT_HALF);
			return iy * TILE_X + ix;
		}
		else {
			return -1;
		}
	}
	
	
	private Tile deselect(Tile tile) {
		if (tile != null) {
			tile.setStateDeselect();
		}
		return null;
	}
	
	
	/**
	 * Check if the user find a solution for a flow from a start to a end-cell.
	 * @param ix Tile x index.
	 * @param iy Tile y index
	 * @param dir Flow direction.
	 * @return true if there is a solution, otherwise false.
	 */
//	private boolean checkTile(int ix, int iy, DirectionEnum dir) {
//		TileEnum tile = gameTiles.get(iy * 6 + ix);
//
//		// Check if tile is valid for the flow direction and determine 
//		// the index of the next tile and direction
//		DirectionEnum ndir = dir;
//		int inx = ix;
//		int iny = iy;
//		if (dir == DirectionEnum.DIR_LEFT) {
//			if (tile == TileEnum.TILE_HORIZONTAL) {
//				ndir = DirectionEnum.DIR_LEFT;
//				--inx;
//			}
//			else if (tile == TileEnum.TILE_RIGHT_DOWN) {
//				ndir = DirectionEnum.DIR_DOWN;
//				--iny;
//			}
//			else if (tile == TileEnum.TILE_RIGHT_UP) {
//				ndir = DirectionEnum.DIR_UP;
//				++iny;
//			}
//			else {
//				// Tile is not valid
//				return false; 
//			}
//		}
//		else if (dir == DirectionEnum.DIR_RIGHT) {
//			if (tile == TileEnum.TILE_HORIZONTAL) {
//				ndir = DirectionEnum.DIR_RIGHT;
//				++inx;
//			}
//			else if (tile == TileEnum.TILE_LEFT_DOWN) {
//				ndir = DirectionEnum.DIR_DOWN;
//				--iny;
//			}
//			else if (tile == TileEnum.TILE_LEFT_UP) {
//				ndir = DirectionEnum.DIR_UP;
//				++iny;
//			}
//			else {
//				// Tile is not valid
//				return false; 
//			}
//		}
//		else if (dir == DirectionEnum.DIR_UP) {
//			if (tile == TileEnum.TILE_VERTICAL) {
//				ndir = DirectionEnum.DIR_UP;
//				++iny;
//			}
//			else if (tile == TileEnum.TILE_LEFT_DOWN) {
//				ndir = DirectionEnum.DIR_LEFT;
//				--inx;
//			}
//			else if (tile == TileEnum.TILE_RIGHT_DOWN) {
//				ndir = DirectionEnum.DIR_RIGHT;
//				++inx;
//			}
//			else {
//				// Tile is not valid
//				return false;
//			}
//		}
//		else if (dir == DirectionEnum.DIR_DOWN) {
//			if (tile == TileEnum.TILE_VERTICAL) {
//				ndir = DirectionEnum.DIR_DOWN;
//				--iny;
//			}
//			else if (tile == TileEnum.TILE_LEFT_UP) {
//				ndir = DirectionEnum.DIR_LEFT;
//				--inx;
//			}
//			else if (tile == TileEnum.TILE_RIGHT_UP) {
//				ndir = DirectionEnum.DIR_RIGHT;
//				++inx;
//			}
//			else {
//				// Tile is not valid
//				return false;
//			}
//		}
//		
//		// Goal reached
//		if (ix == 5 && iy == rowGoal) {
//			if (tile == TileEnum.TILE_HORIZONTAL || 
//			    tile == TileEnum.TILE_RIGHT_UP || 
//				tile == TileEnum.TILE_RIGHT_DOWN) {
//				return true;
//			}
//		}
//		
//		// Outside of field
//		if (inx < 0 || inx > 5 || iny < 0 || iny > 5) {
//			return false;
//		}
//		
//		return checkTile(inx, iny, ndir);
//	}
	
	
//	private void createNewTiles() {
//		
//		// Create initial tiles
//		ArrayList<Integer> randInds = new ArrayList<Integer>(36);
//		for (int i = 0; i < TILE_CNT; i++) {
//			randInds.add(i);
//			gameTiles.add(TileEnum.TILE_CLOUD);
//			initTiles.add(TileEnum.TILE_GRASS);
//		}
//		Collections.shuffle(randInds);
//		int hoCnt = 4;
//		int rdCnt = 3;
//		int luCnt = 3;
//		
//		int veCnt = 8;
//		int ruCnt = 8;
//		int ldCnt = 10;
//		
//		int ind = 0;
//		for (int i = 0; i < hoCnt; i++) {
//			initTiles.set(randInds.get(ind), TileEnum.TILE_HORIZONTAL);
//			++ind;
//		}
//		for (int i = 0; i < rdCnt; i++) {
//			initTiles.set(randInds.get(ind), TileEnum.TILE_GRASS);
//			++ind;
//		}
//		for (int i = 0; i < luCnt; i++) {
//			initTiles.set(randInds.get(ind), TileEnum.TILE_LAKE);
//			++ind;
//		}
//		for (int i = 0; i < veCnt; i++) {
//			initTiles.set(randInds.get(ind), TileEnum.TILE_ROCK);
//			++ind;
//		}
//		for (int i = 0; i < ruCnt; i++) {
//			initTiles.set(randInds.get(ind), TileEnum.TILE_RIGHT_UP);
//			++ind;
//		}
//		for (int i = 0; i < ldCnt; i++) {
//			initTiles.set(randInds.get(ind), TileEnum.TILE_LEFT_DOWN);
//			++ind;
//		}
//		
//		initTiles.set(0, TileEnum.TILE_HORIZONTAL);
//		gameTiles.set(0, TileEnum.TILE_HORIZONTAL);
//		initTiles.set(1, TileEnum.TILE_LEFT_UP);
//		gameTiles.set(1, TileEnum.TILE_LEFT_UP);
//	}
}
