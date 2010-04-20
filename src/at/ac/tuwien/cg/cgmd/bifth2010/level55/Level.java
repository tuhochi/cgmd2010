package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Represents the level in the game. Contains all layers. 
 * @author Wolfgang Knecht
 *
 */
public class Level {
	
	Quad interfaceQuad;
	Texture textureTest;
	
	TileLayer frontLayer;
	TileLayer secondLayer;
	TileLayer thirdLayer;
	
	/**
	 * Initializes the level
	 * @param gl The OpenGL context
	 * @param context The Activity context
	 */
	public void init(GL10 gl, Context context) {
		textureTest = new Texture();
		textureTest.create(R.drawable.l55_testtexture);
		
		frontLayer=new TileLayer();
		frontLayer.init(gl, 1.0f, 1.0f, R.raw.l55_level, R.drawable.l55_front_layer_tex, 4, 4, context);
		
		secondLayer=new TileLayer();
		secondLayer.init(gl, 0.5f, 2.0f, R.raw.l55_level, R.drawable.l55_testtexture, 2, 4, context);
		
		thirdLayer=new TileLayer();
		thirdLayer.init(gl, 0.25f, 3.0f, R.raw.l55_level, R.drawable.l55_testtexture, 2, 4, context);
	}
	
	/**
	 * Renders the level
	 * @param gl The OpenGL context
	 */
	public void draw(GL10 gl) {   
        textureTest.bind(gl);
        
        //thirdLayer.draw(gl);
        //secondLayer.draw(gl);
        frontLayer.draw(gl);
	}
	
	/**
	 * Changes the active state of a coin.
	 * @param x The x-position of the coin to change in the level grid.
	 * @param y The y-position of the coin to change in the level grid.
	 * @return The contribution of the coin to the current game-points
	 */
	public int changeCoinState(int x, int y) {
		return frontLayer.changeCoinState(x, y);
	}
	
	/**
	 * Returns the type of a tile at a given position.
	 * @param x The x-position of the tile in the level grid.
	 * @param y The y-position of the tile in the level grid.
	 * @return The type of tile. -1 if there is no tile a the given position.
	 */
	public int getTypeAt(int x, int y) {
		return frontLayer.getTypeAt(x, y);//.tiles_vector[x][y];
	}
	
	/**
	 * Sets the position of the level to scroll it.
	 * @param x The x-position the level.
	 * @param y The y-positoin of the level.
	 */
	public void setPosition(float x, float y) {
		frontLayer.posX=x;
		frontLayer.posY=y;
		
		secondLayer.posX=x;
		secondLayer.posY=y;
		
		thirdLayer.posX=x;
		thirdLayer.posY=y;
	}
}
