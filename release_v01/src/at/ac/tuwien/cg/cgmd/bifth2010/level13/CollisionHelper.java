package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;

/**
 * 
 * @author group13
 *
 * static class that contains methods for collision detection and handling
 * these methods are invoked from the respective classes
 *
 */
public class CollisionHelper {


	/**
	 * checks if a collision between the player and the map would occur
	 * 
	 * @param offset the offset of the whole scene
	 * 
	 * @return true if a collision is detected
	 */
	public static boolean checkBackgroundCollision(Vector2 offset) {
		return checkBackgroundCollision(null, offset, null);
	}

	/**
	 * checks if a collision between an object and the map would occur.
	 * called within draw function of the object that needs to be checked against the background
	 *
	 * @param g0 object to check
	 * @param offset the offset of the whole scene
	 * @param position position of the object
	 * 
	 * @return true if a collision is detected
	 */

	public static boolean checkBackgroundCollision(GameObject gO, Vector2 offset, Vector2 position) {
		//max and min values of object which should be checked
		int gameObjectXMin;
		int gameObjectYMin;
		int gameObjectXMax;
		int gameObjectYMax;

		//center of the screen
		int centerX;
		int centerY;

		//calculate center of screen
		centerX = ((MyRenderer.getScreenWidth() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		centerY = ((MyRenderer.getScreenHeight() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;


		//calculate min/max values of object
		if(gO == null){
			//if object is the player, consider offset
			gameObjectXMin = centerX + offset.x;
			gameObjectYMin = centerY + offset.y;
			gameObjectXMax = gameObjectXMin + GameObject.BLOCKSIZE;
			gameObjectYMax = gameObjectYMin + GameObject.BLOCKSIZE;
		}
		else {
			gameObjectXMin = position.x;
			gameObjectYMin = position.y;
			gameObjectXMax = gameObjectXMin + GameObject.BLOCKSIZE;
			gameObjectYMax = gameObjectYMin + GameObject.BLOCKSIZE;
		}

		//find corresponding min/max tiles
		int xMin = gameObjectXMin / GameObject.BLOCKSIZE;
		int xMax = gameObjectXMax / GameObject.BLOCKSIZE;
		int yMin = gameObjectYMin / GameObject.BLOCKSIZE;
		int yMax = gameObjectYMax / GameObject.BLOCKSIZE;

		//get level-map
		int map[][] = GameControl.getInstance().getMap();
		//transform to array indices (array starts top left, drawing starts bottom left)
		yMax = map.length - yMax - 1;
		yMin = map.length - yMin - 1;

		//check critical tiles
		for(int i = yMax; i <= yMin; i++) { 
			for(int j = xMin; j <= xMax; j++) {
				//if tile is solid
				if(map[i][j] == 1) {
					//check if they intersect
					if((gameObjectXMin >= (j+1) * GameObject.BLOCKSIZE  ||
							gameObjectXMax <= j * GameObject.BLOCKSIZE ||
							gameObjectYMin >= (Math.abs(i - map.length + 1)+1) * GameObject.BLOCKSIZE ||
							gameObjectYMax <= Math.abs(i - map.length +1) * GameObject.BLOCKSIZE)) {
						//no collision
						continue;
					}
					else {
						//collision detected
						return true;
					}
				}
			}
		}
		return false;
	}


	/**
	 * checks if a collision between the player and an object occurs
	 * 
	 * @param x	x-position of object
	 * @param y y-position of object
	 * 
	 * @return true if a collision is detected
	 */
	public static boolean checkPlayerObjectCollision(int x, int y) {
		//center of screen
		int centerX;
		int centerY;

		//calculate center
		centerX = ((MyRenderer.getScreenWidth() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		centerY = ((MyRenderer.getScreenHeight() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;

		//calculate player's min/max x/y - coordinates
		int playerXMin = centerX + GameObject.offset.x;
		int playerYMin = centerY + GameObject.offset.y;
		int playerXMax = playerXMin + GameObject.BLOCKSIZE;
		int playerYMax = playerYMin + GameObject.BLOCKSIZE;

		//check if player and object intersect
		if((playerXMin >= x + GameObject.BLOCKSIZE ||
				playerXMax <= x ||
				playerYMin >= y + GameObject.BLOCKSIZE ||
				playerYMax <= y)) {
			//no collision
			return false;
		}
		else {
			//collision detected
			return true;
		}
	}
}
