package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

public class Diamond extends Coin{

	/**
	 * Constructor
	 * @param gl The OpenGL context
	 * @param _x The x-position in the level grid
	 * @param _y The y-position in the level grid
	 * @param _active The initial active state of the diamond
	 * @param x1 The first x coordinate of the diamond quad
	 * @param y1 The first y coordinate of the diamond quad
	 * @param x2 The second x coordinate of the diamond quad
	 * @param y2 The second y coordinate of the diamond quad
	 * @param texX1_A The first x texture-coordinate of the active diamond 
	 * @param texY1_A The first y texture-coordinate of the active diamond
	 * @param texX2_A The second x texture-coordinate of the active diamond
	 * @param texY2_A The second y texture-coordinate of the active diamond
	 * @param texX1_I The first x texture-coordinate of the inactive diamond
	 * @param texY1_I The first y texture-coordinate of the inactive diamond
	 * @param texX2_I The second x texture-coordinate of the inactive diamond
	 * @param texY2_I The second y texture-coordinate of the inactive diamond
	 */
	public Diamond(GL10 gl, int x, int y, boolean active, float x1, float y1,
			float x2, float y2, float texX1A, float texY1A, float texX2A,
			float texY2A, float texX1I, float texY1I, float texX2I, float texY2I) {
		super(gl, x, y, active, x1, y1, x2, y2, texX1A, texY1A, texX2A, texY2A, texX1I,
				texY1I, texX2I, texY2I);
	}
	
	/**
	 * Changes the active state of the diamond
	 * @return The contribution of the diamond to the current game-points
	 */
	public int changeActiveState() {
		active=!active;
		
		if (active) {
			return -5;
		}
		return 5;
	}
	
}
