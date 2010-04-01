package at.ac.tuwien.cg.cgmd.bifth2010.level13;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class CollisionHandler {
	/**
	 * checks if a collision between the player and an object would occur
	 * @return
	 */
	public static boolean checkBackgroundCollision(char map[][]) {
		//calculate player's min/max x/y - coordinates
		int playerXMin = (int)(PlayerObject.center.x + GameObject.offset.x);
		int playerYMin = (int)(PlayerObject.center.y + GameObject.offset.y);
		int playerXMax = playerXMin + GameObject.BLOCKSIZE;
		int playerYMax = playerYMin + GameObject.BLOCKSIZE;
		
		//find corresponding tiles
		int xMin = playerXMin / GameObject.BLOCKSIZE;
		int xMax = playerXMax / GameObject.BLOCKSIZE;
		int yMin = playerYMin / GameObject.BLOCKSIZE;
		int yMax = playerYMax / GameObject.BLOCKSIZE;
		
		//transform to array indices (array starts top left, drawing starts bottom left)
		yMax = map.length - yMax - 1;
		yMin = map.length - yMin - 1;

		//check critical tiles
		for(int i = yMax; i <= yMin; i++) { 
			for(int j = xMin; j <= xMax; j++) {
				//if tile is solid
				if(map[i][j] == 1) {
					//check if they intersect
					if((playerXMin >= (j+1) * GameObject.BLOCKSIZE  ||
							playerXMax <= j * GameObject.BLOCKSIZE ||
							playerYMin >= (Math.abs((i - map.length + 1))+1) * GameObject.BLOCKSIZE ||
							playerYMax <= Math.abs(i - map.length +1) * GameObject.BLOCKSIZE
							)) {
						continue;
					}
					else {
						//collision detected
						//TODO set player to edge
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * checks if a collision between the player and a beer occurs
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkBeerCollision(int x, int y) {
		//calculate player's min/max x/y - coordinates
		int playerXMin = (int)(PlayerObject.center.x + GameObject.offset.x);
		int playerYMin = (int)(PlayerObject.center.y + GameObject.offset.y);
		int playerXMax = playerXMin + GameObject.BLOCKSIZE;
		int playerYMax = playerYMin + GameObject.BLOCKSIZE;

		//check if they intersect
		if((playerXMin >= x + GameObject.BLOCKSIZE ||
				playerXMax <= x ||
				playerYMin >= y + GameObject.BLOCKSIZE ||
				playerYMax <= y
		)) {
			return false;
		}
		else {
			//collision detected
			return true;
		}


	}
}
