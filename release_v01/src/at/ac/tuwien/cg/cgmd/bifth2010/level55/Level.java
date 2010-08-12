package at.ac.tuwien.cg.cgmd.bifth2010.level55;


import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
	
	Handler handleUIChanges;
	
	/**
	 * Initializes the level
	 * @param gl The OpenGL context
	 * @param context The Activity context
	 */
	public void init(GL10 gl, Context context, String coinStates) {
		frontLayer=new TileLayer();
		frontLayer.init(gl, 1.0f, 1.0f, R.raw.l55_level, R.drawable.l55_front_layer_tex, 4, 4, context);
		
		if (!coinStates.equals("")) {
			int charPos=0;
	        for (int i=0; i<frontLayer.maxVBOPosX; i++) {
	        	for (int j=0; j<frontLayer.maxVBOPosY; j++) {
	        		for (int k=0; k<frontLayer.vbo_vector[i][j].coinCount; k++) {
	        			if (coinStates.charAt(charPos)=='1') {
	        				frontLayer.vbo_vector[i][j].coins[k].active=true;
	        			}
	        			charPos++;
	        		}
	        	}
	        }
		}
		
		secondLayer=new TileLayer();
		secondLayer.init(gl, 0.77f, 1.0f, R.raw.l55_middle, R.drawable.l55_middle_layer_tex, 4, 4, context);
		
		thirdLayer=new TileLayer();
		thirdLayer.init(gl, 0.25f, 1.0f, R.raw.l55_back, R.drawable.l55_back_layer_tex, 2, 4, context);
		
		handleUIChanges=((LevelActivity)context).handleUIChanges;
	}
	
	/**
	 * Renders the level
	 * @param gl The OpenGL context
	 */
	public void draw(GL10 gl) {          
        thirdLayer.draw(gl);
        secondLayer.draw(gl);
        frontLayer.draw(gl);
	}
	
	public void finish() {
		handleUIChanges.sendEmptyMessage(2);
	}
	
	/**
	 * Changes the active state of a coin and updates gamepoints.
	 * @param x The x-position of the coin to change in the level grid.
	 * @param y The y-position of the coin to change in the level grid.
	 * @return The contribution of the coin to the current game-points
	 */
	public int changeCoinState(int x, int y) {
		int result=frontLayer.changeCoinState(x, y);
		Message msg=new Message();
		msg.what=1;
		msg.arg1=result;
		handleUIChanges.sendMessage(msg);
		
		return result;
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
