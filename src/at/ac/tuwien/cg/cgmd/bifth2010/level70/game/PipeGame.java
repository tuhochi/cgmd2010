package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level00.TestLevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;
/**
 * Pipe game.
 * In the pipe game the user has to find a flow from a start cell to a goal cell
 * by switching different tiles.
 */
public class PipeGame {

	// ----------------------------------------------------------------------------------
	// -- Static Members ----
	private static String STATE_INIT_TILES = "StateInitTiles";
	private static String STATE_GAME_TILES = "StateGameTiles";

	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	private float orthoWidth;  //< Ortho width
	private float orthoHeight; //< Ortho height
	private float orthoScaleX; //< Ortho x scale factor 
	private float orthoScaleY; //< Ortho y scale factor
	
	private int indSource; //< Index of the source tile
	private int indTarget; //< Index of the target tile
	private int rowStart;  //< Index of the start-cell row
	private int rowGoal;   //< Index of the goal-cell row
	
	private TileTexture texTile; //< Tile texture
	private ArrayList<TileEnum> initTiles; //< Initial game tiles
	private ArrayList<TileEnum> gameTiles; //< Current game tiles
	private	ArrayList<TileGeometry> geomTiles; //< Geometry representation of the tiles
	private TileGeometry geomSource; //< Geometry to highlight source tile
	private TileGeometry geomTarget; //< Geometry to highlight target tile
	private TileGeometry geomStart;  //< Start of the flow
	private TileGeometry geomGoal;   //< Goal of the flow
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create the pipe game.
	 */
	public PipeGame(Bundle state, int width, int height) {
		
		setDimension(width, height);
		
		indSource = -1;
		indTarget = -1;
		
		rowStart = 1;
		rowGoal  = 4;
		
		initTiles = new ArrayList<TileEnum>(36);
		gameTiles = new ArrayList<TileEnum>(36);
		geomTiles = new ArrayList<TileGeometry>(36);
		
		// Initialize tiles
		createNewTiles();
		
		// Restore tiles from an saved game state
		if (state != null) {
			int[] stateInit = state.getIntArray(STATE_INIT_TILES);
			int[] stateGame = state.getIntArray(STATE_GAME_TILES);
			if (stateInit != null && stateGame != null) {
				for (int i = 0; i < stateInit.length; i++) {
					initTiles.set(i, TileEnum.valueOf(stateInit[i]));
					gameTiles.set(i, TileEnum.valueOf(stateGame[i]));
				}
			}
		}
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	public void create() {
				
		
		texTile   = new TileTexture();
		
		geomSource = new TileGeometry(0, 0);
		geomSource.setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_SOURCE_SEL));
		
		geomTarget = new TileGeometry(0, 0);
		geomTarget.setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_TARGET_SEL));
		
		geomStart  = new TileGeometry(-4.0f, -3.0f + rowStart * TileGeometry.TILE_SIZE);
		geomStart.setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_START_GOAL));

		geomGoal  = new TileGeometry(3.0f, -3.0f + rowGoal * TileGeometry.TILE_SIZE);
		geomGoal.setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_START_GOAL));
				
		// Create tile geometry
		for (int iy = 0; iy < 6; iy++) {
			for (int ix = 0; ix < 6; ix++) {
				float llx = -3.0f + ix * TileGeometry.TILE_SIZE;
				float lly = -3.0f + iy * TileGeometry.TILE_SIZE;
				TileGeometry geom = new TileGeometry(llx, lly);
				geom.setTexBuffer(texTile.getTexBuffer(gameTiles.get(iy * 6 + ix)));
				geomTiles.add(geom);
			}
		}
	}
	
	/**
	 * Set screen dimension.
	 * @param width The screen width.
	 * @param height The screen height.
	 */
	public void setDimension(int width, int height) {
		float ratio = (float)width / (float) height;
		if (ratio > 1.0) {
			orthoWidth  = 4.0f * ratio;
			orthoHeight = 4.0f;
		} 
		else {
			orthoWidth  = 4.0f;
			orthoHeight = 4.0f / ratio;
		}
		orthoScaleX =  2.0f * orthoWidth  / (float)width;
		orthoScaleY = -2.0f * orthoHeight / (float)height;
	}
	
	
	/**
	 * Draw method for the pipe game.
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
			
		gl.glOrthof(-orthoWidth, orthoWidth, -orthoHeight, orthoHeight, -1.0f, 1.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glActiveTexture(0);
		texTile.bind();
		for (TileGeometry it : geomTiles) {
			it.draw(gl);
		}
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		if (indSource != -1) {
			float llx = geomTiles.get(indSource).getLlx();
			float lly = geomTiles.get(indSource).getLly();
			gl.glPushMatrix();
			gl.glTranslatef(llx, lly, 0);
			geomSource.draw(gl);
			gl.glPopMatrix();
		}

		if (indTarget != -1) {
			float llx = geomTiles.get(indTarget).getLlx();
			float lly = geomTiles.get(indTarget).getLly();
			gl.glPushMatrix();
			gl.glTranslatef(llx, lly, 0);
			geomTarget.draw(gl);
			gl.glPopMatrix();
		}
		gl.glDisable(GL10.GL_BLEND);
		
		// Draw start and goal marker
		geomStart.draw(gl);
		geomGoal.draw(gl);
				
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL10.GL_MODELVIEW);

		texTile.unbind();
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	
	/**
	 * OnClick event for a tile.
	 * The source tile is highlighted in red and the target tile is highlighted
	 * in green. To switch tiles select a source and a target tile and click 
	 * again on the target tile.
	 * @param event The MotionEvent.
	 */
	public void onClick(MotionEvent event) {
		int index = getIndex(event.getRawX(), event.getRawY());
		if (index == -1) {
			indSource = -1;
			indTarget = -1;
			return;
		}
		
		// Click on emtpy tile
		if (gameTiles.get(index) == TileEnum.TILE_EMPTY) {
			if (indTarget == index) {
				TileEnum type = initTiles.get(index);
				gameTiles.set(index, type);
				geomTiles.get(index).setTexBuffer(texTile.getTexBuffer(type));
				indSource = -1;
				indTarget = -1;
			}
			else {
				indSource = -1;
				indTarget = index;
			}

		}
		// Click on obstacle
		else if (gameTiles.get(index) == TileEnum.TILE_OBSTACLE) {
			indTarget = -1;
		}
		// Click on pipe tile
		else {
			if (indSource == -1) {
				indSource = index;
				indTarget = -1;
			}
			else if (indSource == index) {
				indSource = -1;
				indTarget = -1;
			}
			else if (indTarget == index) {
				TileEnum typeSource = gameTiles.get(indSource);
				TileEnum typeTarget = gameTiles.get(indTarget);
				
				gameTiles.set(indSource, typeTarget);
				geomTiles.get(indSource).setTexBuffer(texTile.getTexBuffer(typeTarget));
				
				gameTiles.set(indTarget, typeSource);
				geomTiles.get(indTarget).setTexBuffer(texTile.getTexBuffer(typeSource));
				
				indSource = -1;
				indTarget = -1;
			}
			else {
				indTarget = index;
			}
		}
		
		boolean result = checkTile(0, rowStart, DirectionEnum.DIR_RIGHT);
		if (result) {
			Log.i("PipeGame", "Goal reached");
			
			// Set the progress the user has made (must be between 0-100)
			SessionState s = new SessionState();
			s.setProgress(10);
			LevelActivity.getInstance().setResult(Activity.RESULT_OK, s.asIntent());
			LevelActivity.getInstance().finish();
//			for (TileGeometry it : geomTiles) {
//				it.setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_EMPTY));
//			}
//			geomTiles.get(13).setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_OBSTACLE));
//			geomTiles.get(8).setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_OBSTACLE));
//			geomTiles.get(9).setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_OBSTACLE));
//			geomTiles.get(16).setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_OBSTACLE));
//			geomTiles.get(26).setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_OBSTACLE));
//			geomTiles.get(27).setTexBuffer(texTile.getTexBuffer(TileEnum.TILE_OBSTACLE));
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
    	
    	tiles = new int[gameTiles.size()];
    	for (int i = 0; i < tiles.length; i++) {
    		tiles[i] = gameTiles.get(i).ordinal();
    	}
    	state.putIntArray(STATE_GAME_TILES, tiles);
    }
    
    
    /**
     * Restore actual game stat.
     * @param outState The game state
     */
    public void onRestoreState(Bundle state) {
    	
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
		float ox = orthoScaleX * x - orthoWidth;
		float oy = orthoScaleY * y + orthoHeight;
		
		if (ox > -3.0f && ox < 3.0f && 
			oy > -3.0f && oy < 3.0f) {
			int ix = (int)(ox + 3.0f);
			int iy = (int)(oy + 3.0f);
			return iy * 6 + ix;
		}
		else {
			return -1;
		}
	}
	
	
	/**
	 * Check if the user find a solution for a flow from a start to a end-cell.
	 * @param ix Tile x index.
	 * @param iy Tile y index
	 * @param dir Flow direction.
	 * @return true if there is a solution, otherwise false.
	 */
	private boolean checkTile(int ix, int iy, DirectionEnum dir) {
		TileEnum tile = gameTiles.get(iy * 6 + ix);

		// Check if tile is valid for the flow direction and determine 
		// the index of the next tile and direction
		DirectionEnum ndir = dir;
		int inx = ix;
		int iny = iy;
		if (dir == DirectionEnum.DIR_LEFT) {
			if (tile == TileEnum.TILE_HORIZONTAL) {
				ndir = DirectionEnum.DIR_LEFT;
				--inx;
			}
			else if (tile == TileEnum.TILE_RIGHT_DOWN) {
				ndir = DirectionEnum.DIR_DOWN;
				--iny;
			}
			else if (tile == TileEnum.TILE_RIGHT_UP) {
				ndir = DirectionEnum.DIR_UP;
				++iny;
			}
			else {
				// Tile is not valid
				return false; 
			}
		}
		else if (dir == DirectionEnum.DIR_RIGHT) {
			if (tile == TileEnum.TILE_HORIZONTAL) {
				ndir = DirectionEnum.DIR_RIGHT;
				++inx;
			}
			else if (tile == TileEnum.TILE_LEFT_DOWN) {
				ndir = DirectionEnum.DIR_DOWN;
				--iny;
			}
			else if (tile == TileEnum.TILE_LEFT_UP) {
				ndir = DirectionEnum.DIR_UP;
				++iny;
			}
			else {
				// Tile is not valid
				return false; 
			}
		}
		else if (dir == DirectionEnum.DIR_UP) {
			if (tile == TileEnum.TILE_VERTICAL) {
				ndir = DirectionEnum.DIR_UP;
				++iny;
			}
			else if (tile == TileEnum.TILE_LEFT_DOWN) {
				ndir = DirectionEnum.DIR_LEFT;
				--inx;
			}
			else if (tile == TileEnum.TILE_RIGHT_DOWN) {
				ndir = DirectionEnum.DIR_RIGHT;
				++inx;
			}
			else {
				// Tile is not valid
				return false;
			}
		}
		else if (dir == DirectionEnum.DIR_DOWN) {
			if (tile == TileEnum.TILE_VERTICAL) {
				ndir = DirectionEnum.DIR_DOWN;
				--iny;
			}
			else if (tile == TileEnum.TILE_LEFT_UP) {
				ndir = DirectionEnum.DIR_LEFT;
				--inx;
			}
			else if (tile == TileEnum.TILE_RIGHT_UP) {
				ndir = DirectionEnum.DIR_RIGHT;
				++inx;
			}
			else {
				// Tile is not valid
				return false;
			}
		}
		
		// Goal reached
		if (ix == 5 && iy == rowGoal) {
			if (tile == TileEnum.TILE_HORIZONTAL || 
			    tile == TileEnum.TILE_RIGHT_UP || 
				tile == TileEnum.TILE_RIGHT_DOWN) {
				return true;
			}
		}
		
		// Outside of field
		if (inx < 0 || inx > 5 || iny < 0 || iny > 5) {
			return false;
		}
		
		return checkTile(inx, iny, ndir);
	}
	
	
	private void createNewTiles() {
		
		// Create initial tiles
		ArrayList<Integer> randInds = new ArrayList<Integer>(36);
		for (int i = 0; i < 36; i++) {
			randInds.add(i);
			gameTiles.add(TileEnum.TILE_EMPTY);
			initTiles.add(TileEnum.TILE_EMPTY);
		}
		Collections.shuffle(randInds);
		int hoCnt = 4;
		int rdCnt = 3;
		int luCnt = 3;
		
		int veCnt = 8;
		int ruCnt = 8;
		int ldCnt = 10;
		
		int ind = 0;
		for (int i = 0; i < hoCnt; i++) {
			initTiles.set(randInds.get(ind), TileEnum.TILE_HORIZONTAL);
			++ind;
		}
		for (int i = 0; i < rdCnt; i++) {
			initTiles.set(randInds.get(ind), TileEnum.TILE_RIGHT_DOWN);
			++ind;
		}
		for (int i = 0; i < luCnt; i++) {
			initTiles.set(randInds.get(ind), TileEnum.TILE_LEFT_UP);
			++ind;
		}
		for (int i = 0; i < veCnt; i++) {
			initTiles.set(randInds.get(ind), TileEnum.TILE_VERTICAL);
			++ind;
		}
		for (int i = 0; i < ruCnt; i++) {
			initTiles.set(randInds.get(ind), TileEnum.TILE_RIGHT_UP);
			++ind;
		}
		for (int i = 0; i < ldCnt; i++) {
			initTiles.set(randInds.get(ind), TileEnum.TILE_LEFT_DOWN);
			++ind;
		}
	}
}
