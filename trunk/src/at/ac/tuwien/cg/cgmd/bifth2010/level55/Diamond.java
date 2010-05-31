package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

public class Diamond extends Coin{

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
