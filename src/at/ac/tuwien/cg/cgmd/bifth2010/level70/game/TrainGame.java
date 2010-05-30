package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.util.SpriteTexture;

/**
 * The main class to manage the train game. The train moves from the left side of into
 * the playfield. The game is completed if the train reaches a goal tile. The game is
 * over if the train can not move to the next tile or moves outside the playfield.
 * 
 * The base tile arrangement is loaded from different level files. And the train tiles are
 * randomly placed on the playfield.
 * 
 * @author Christoph Winklhofer
 */
public class TrainGame {

	// ----------------------------------------------------------------------------------
	// -- Static Members ----
	
    /** State to save the initial tiles */
	private static String STATE_INIT_TILES  = "StateInitTiles";
	
	/** State to save the game tiles, move tiles by the user */
	private static String STATE_GAME_TILES  = "StateGameTiles";
	
	/** State to save the tiles where train has already moved on */
	private static String STATE_TRAIN_TILES = "StateTrainTiles";
	
	/** State to save the coin tiles */
	private static String STATE_COIN_TILES = "StateCoinTiles";
	
	/** State to save the highlight source and target tile */
	private static String STATE_HIGHLIGHT_TILES = "StateHighlightTiles";
	
	/** State to save the current start game time */
	private static String STATE_START_TIME = "StateStartTime";
	
	/** State to save game flags, (eg. game over / game complete) */
	private static String STATE_GAME_FLAGS = "StateGameFlags";

	
	// ----------------------------------------------------------------------------------
    // -- Members ----
	
	/** Width of the playfield */
	private static final float WORLD_WIDTH_HALF  = 5.0f;
	
	/** Height of the playfield */
	private static final float WORLD_HEIGHT_HALF = 3.0f;
	
	/** Ratio of the playfield */
	private static final float WORLD_RATIO  = WORLD_WIDTH_HALF / WORLD_HEIGHT_HALF;
	
	/** Number of tiles in x direction */
	private static final int TILE_X   = 10;
	
	/** Number of tiles in the y direction */
	private static final int TILE_Y   = 6;
	
	/** Total number of tiles */
	private static final int TILE_CNT = 60;
	
	/** Rotate view when height greater than width */ 
	private boolean isRotateView;
	
	/** Screen resolution - width */
	private float screenWidth;
	
	/** Screen resolution - height */
	private float screenHeight;
	
	/** Half world size - width */
	private float orthoWidthHalf;
	
	/** Half world size - height */
	private float orthoHeightHalf; 
	
	/** Initial game tiles */
	private ArrayList<TileEnum> initTiles;
	
	/** Current game tiles */
	private ArrayList<Tile> gameTiles;
	
	/** Start time of the train */
	private float timeStart;
	
	/** Is OpenGl initialized */
	private boolean isOpenGl; 
	
	/** Tile texture */
	private SpriteTexture texTile;
	
	/** Train texture */
	private SpriteTexture texTrain;
	
	/** The train */
	private Train train;
	
	/** Selected source tile */
	private Tile  tileSource;
	
	/** Selected target tile */
	private Tile  tileTarget;
	
	/** Game over state */
	private boolean isGameOver;
	
	/** Game complete state */
	private boolean isGameCompleted;
	
	/** At start up we play a train horn */
	private boolean isPlayTrainSound;
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create the train game.
	 */
	public TrainGame(Bundle state, int width, int height) {
			
		isOpenGl = false;
		
		tileSource = null;
		tileTarget = null;
		
		initTiles = new ArrayList<TileEnum>(TILE_CNT);
		gameTiles = new ArrayList<Tile>(TILE_CNT);
		
		isGameOver = false;
		isGameCompleted = false;
		isPlayTrainSound = true;
		// Create new tiles
		createNewGame();
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Create a new game. Load the base arrengement of the tiles from a level file and 
	 * place the different tile types randomly on the playfield.
	 * There are currently 5 different base arrengements. 
	 * The startTime and the time the train needs to move along one tile are also stored
	 * in the level files.
	 */
	void createNewGame() {
		
		StringBuilder sbLevelConf = new StringBuilder();
		StringBuilder sbLevelGame = new StringBuilder();
		StringBuilder sbLevelInit = new StringBuilder();
		StringBuilder sbLevelRail = new StringBuilder();

		// -- Read level data from file --
		try {
			
		    // Select a random level
			ArrayList<Integer> levels = new ArrayList<Integer>();
			levels.add(R.raw.l70_level1);
			levels.add(R.raw.l70_level2);
			levels.add(R.raw.l70_level3);
			levels.add(R.raw.l70_level4);
			levels.add(R.raw.l70_level5);
			Collections.shuffle(levels);
			
			Context     ctx = LevelActivity.getInstance();
			InputStream is  = ctx.getResources().openRawResource(levels.get(0));
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
			Tile tile = new Tile();
			tile.setType(TileEnum.valueOf(Integer.parseInt(levelGameTiles[i])));
			gameTiles.add(tile);
			if (levelInitTiles[i].equals("r")) {
				initTiles.add(levelRails.remove());
			}
			else {
				initTiles.add(TileEnum.valueOf(Integer.parseInt(levelInitTiles[i])));
			}
		}
		
		String[] levelConf = sbLevelConf.toString().split(",");
		int iyTile = Integer.parseInt(levelConf[0]);
		int iGoal  = Integer.parseInt(levelConf[1]);
		float time = Float.parseFloat(levelConf[2]);
		timeStart  = Float.parseFloat(levelConf[3]);
		
		gameTiles.get(iGoal).setIsGoal(true);
		
		train = new Train(this, time, -1, iyTile, 0);
	}
	
	/**
	 * Create all OpenGL specific members.
	 */
	public void createOpenGl(GL10 gl) {
		
		texTile = new SpriteTexture(R.drawable.l70_tiles, 64, 14);
		
		for (int iy = 0; iy < TILE_Y; iy++) {
			for (int ix = 0; ix < TILE_X; ix++) {
				float cx = -4.5f + ix;
				float cy = -2.5f + iy;
				gameTiles.get(iy * TILE_X + ix).createOpenGl(cx, cy, texTile);
			}
		}
		
		texTrain = new SpriteTexture(R.drawable.l70_train, 64, 5);
		train.createOpenGl(texTrain);
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
	
	
	/**
	 * Return the game tile for the specified index.
	 * @param ix x index of the tile
	 * @param iy y index of the tile
	 * @return The game tile.
	 */
	public Tile getTile(int ix, int iy) {
		return gameTiles.get(iy * TILE_X + ix);
	}
	
	
	/**
	 * Return true if the game is in game over state.
	 * @return Return true if the game is over.
	 */
	public boolean isGameOver() {
	    return isGameOver;
	}
	
	
	/**
	 * Return true if the game is in game complete state.
	 * @return True if the game is complete.
	 */
	public boolean isGameCompleted() {
        return isGameCompleted;
    }
	
	
	/** 
	 * Update the game entitys 
	 * @param dt The delta frame time 
	 */
	public void update(float dt) {
		if (!isOpenGl) {
			return;
		}
		
		if (timeStart > 0) {
			timeStart -= dt;
			if (timeStart < 1.5 && isPlayTrainSound) {
			    SoundManager.getInstance().play(2);
			    isPlayTrainSound = false;
			}
		} 
		else {
			train.update(dt);
		}
		
		for (Tile it : gameTiles) {
			it.update(dt);
		}
	}
	
	
	/**
	 * Draw the game tiles and the train.
	 * @param gl OpenGL context
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
	
	
	/**
	 * The train has reached the goal tile. Display a game complete text.
	 */
	public void onGameCompleted() {
	    if (isGameCompleted) {
	        return;
	    }
	    LevelActivity.getInstance().handler.post(LevelActivity
	                .getInstance().displayGameComplete);
		isGameCompleted = true;
		SoundManager.getInstance().play(2);
	}
	
	
	/** 
	 * The game is over. The train can not move to the next tile or moved outside
	 * the playfield. Display a game over text.
	 */
	public void onGameOver() {
	    if (isGameOver) {
	        return;
	    }
	    LevelActivity.getInstance().handler.post(LevelActivity
                .getInstance().displayGameOver);
	    isGameOver = true;
	    SoundManager.getInstance().play(2);
	}
	
	
	/**
	 * Handle the user input. The tiles can be selected and switch by touchnig them.
	 * @param event The user event.
	 */
	public void onMotionEvent(MotionEvent event) {
		
	    if (isGameOver || isGameCompleted) {
	        int goldCnt = 0;
	        for (Tile it : gameTiles) {
	            if (it.isStateCoin()) {
	                goldCnt++;
	            }
	        }
	        if (isGameCompleted) {
	            goldCnt = 100;
	        }
	        SoundManager.getInstance().stopAll();
	        LevelActivity.getInstance().setStateProgress(goldCnt);
	    }
	    
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
				SoundManager.getInstance().play(1);
			}
			// Highlight initial tile
			else {
				tileSource = deselect(tileSource);
				tileTarget = deselect(tileTarget);
				tileTarget = tile;
				tileTarget.setStateSelect();
				SoundManager.getInstance().play(0);
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
					SoundManager.getInstance().play(1);
				}
				else {
					// Deselect - When another tile is selected
					tileSource = deselect(tileSource);
					tileTarget = deselect(tileTarget);
					
					// Select source tile 
					tileSource = tile;
					tileSource.setStateSelect();
					SoundManager.getInstance().play(0);
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
					tileTarget.setTexBuffer();
					SoundManager.getInstance().play(0);
				}
			}
			else {
				// Select source tile 
				tileSource = tile;
				tileSource.setStateSelect();
				tileSource.setTexBuffer();
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
    	ArrayList<Integer> coinTiles = new ArrayList<Integer>();
    	int[] highlights = {-1, -1};
    	tiles = new int[gameTiles.size()];
    	for (int i = 0; i < tiles.length; i++) {
    	    Tile tile = gameTiles.get(i);
    		tiles[i] = tile.getType().ordinal();
    		if (tile.isStateTrain()) {
    		    trainTiles.add(i);
    		}
    		if (tile.isStateCoin()) {
                coinTiles.add(i);
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
    	state.putIntegerArrayList(STATE_COIN_TILES, coinTiles);
    	state.putFloat(STATE_START_TIME, timeStart);
    	boolean gameFlags[] = new boolean[3];
    	gameFlags[0] = isGameOver;
    	gameFlags[1] = isGameCompleted;
    	gameFlags[2] = isPlayTrainSound;
    	state.putBooleanArray(STATE_GAME_FLAGS, gameFlags);
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
				ArrayList<Integer> coinTiles = state.getIntegerArrayList(STATE_COIN_TILES);
                for (Integer it : coinTiles) {
                    gameTiles.get(it).setStateCoin();
                }
				if (stateHighlight[0] != -1) {
				    tileSource = gameTiles.get(stateHighlight[0]);
				    tileSource.setStateHighlight();
				}
				if (stateHighlight[1] != -1) {
                    tileTarget = gameTiles.get(stateHighlight[1]);
                    tileTarget.setStateHighlight();
                }
				timeStart = state.getFloat(STATE_START_TIME);
				boolean[] gameFlags = state.getBooleanArray(STATE_GAME_FLAGS);
				isGameOver = gameFlags[0];
				isGameCompleted = gameFlags[1];
				isPlayTrainSound = gameFlags[2];
			}
			train.onRestoreState(state);
		}
    }
	
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
	
	
	/**
	 * Deselect the tile.
	 * @param tile The tile to deselct.
	 * @return Return null to initialize the tile
	 */
	private Tile deselect(Tile tile) {
		if (tile != null) {
			tile.setStateDeselect();
		}
		return null;
	}
}
