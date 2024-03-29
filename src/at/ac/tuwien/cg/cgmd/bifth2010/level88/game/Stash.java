package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import javax.microedition.khronos.opengles.GL10;


/**
 * Class for the stashes
 * @author Asperger, Radax
 */
public class Stash {
	/**
	 * Game instance of the level
	 */
	private Game game;
	/**
	 * Position of the stash. This position is
	 * a logical coordinate on the map.
	 */
	public int currentPosX, currentPosY;
	/**
	 * Position of the stash. This position is
	 * used to translate the stash for rendering (so
	 * it is displayed at the right position).
	 */
	public float translateX, translateY;
	/**
	 * Size of the stash. Ranges from 1 (small)
	 * to 3 (large)
	 */
	public int size;
	/**
	 * Time the stash has to be hidden. If >0
	 * the stash is hidden and gold can't be
	 * hidden in the stash. The stash also won't be
	 * rendered if it is hidden.
	 */
	public float hideTime;
	/**
	 * Maximum hide time.
	 */
	public float maxHideTime;


	/**
	 * Constructor
	 * @param _game game context
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param _size size of the stash
	 */
	public Stash(Game _game, int x, int y, int _size) {
		game = _game;
		setPosition(x, y);
		size = _size;

		maxHideTime = 120; // 2 minutes hide time after stash is used
		hideTime = 0;
	}

	/**
	 * Update the stash: check if stash is active and check if bunny has reached the stash
	 * @param elapsedSeconds time between the last update and now
	 */
	public void update(float elapsedSeconds) {
		if( hideTime>0 ) {
			hideTime -= elapsedSeconds;
			return;
		}

		if( currentPosX==game.bunny.currentPosX && currentPosY==game.bunny.currentPosY ) {
			game.looseGold(3*size);
			hideTime = maxHideTime;
		}
	}
	
	
	/**
	 * Draw the stash
	 * @param gl OpenGL context of android
	 */
	public void draw(GL10 gl) {
		if( hideTime>0 ) return;
		
		gl.glPushMatrix();
		gl.glTranslatef(translateX, translateY, 0);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
		gl.glPopMatrix();
	}

	/**
	 * Set the position of the stash and calculate translation vector
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public void setPosition(int x, int y) {
		currentPosX = x;
		currentPosY = y;
		
		translateX = currentPosX*game.map.groundXDir.x + currentPosY*game.map.groundYDir.x;
		translateY = currentPosX*game.map.groundXDir.y + currentPosY*game.map.groundYDir.y;
	}

}
