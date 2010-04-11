package at.ac.tuwien.cg.cgmd.bifth2010.level13;

/**
 * 
 * @author arthur/sebastian (group 13)
 *
 */
public class CollisionHandler {
	
	
	/**
	 * checks if a collision between the player and an object would occur
	 * @return
	 */
	public static boolean checkBackgroundCollision(int map[][]) {
		return checkBackgroundCollision(map, null);
		
	}
	
	/**
	 * checks if a collision between an object and a wall would occur
	 * @return
	 */
	
	public static boolean checkBackgroundCollision(int[][] map,GameObject gO) {
		int gameObjectXMin;
		int gameObjectYMin;
		int gameObjectXMax;
		int gameObjectYMax;
		
		if (gO == null){
			gameObjectXMin = (int)(PlayerObject.center.x + GameObject.offset.x);
			gameObjectYMin = (int)(PlayerObject.center.y + GameObject.offset.y);
			gameObjectXMax = gameObjectXMin + GameObject.BLOCKSIZE;
			gameObjectYMax = gameObjectYMin + GameObject.BLOCKSIZE;
		}else{
			gameObjectXMin = (int)(gO.position.x );
			gameObjectYMin = (int)(gO.position.y );
			gameObjectXMax = gameObjectXMin + GameObject.BLOCKSIZE;
			gameObjectYMax = gameObjectYMin + GameObject.BLOCKSIZE;
		}
		
		//find corresponding tiles
		int xMin = gameObjectXMin / GameObject.BLOCKSIZE;
		int xMax = gameObjectXMax / GameObject.BLOCKSIZE;
		int yMin = gameObjectYMin / GameObject.BLOCKSIZE;
		int yMax = gameObjectYMax / GameObject.BLOCKSIZE;
	
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
							gameObjectYMax <= Math.abs(i - map.length +1) * GameObject.BLOCKSIZE
							)) {
						continue;
					}
					
					else {//collision detected
						
						if (gO != null){
							//Set object back to the last tile it was on before
							if (gO.moveVec.x == 0)
								gO.position.y  =   (Math.abs(i - map.length +1) * GameObject.BLOCKSIZE + gO.moveVec.signY() * GameObject.BLOCKSIZE*-1);
							else
								gO.position.x =  (j * GameObject.BLOCKSIZE + gO.moveVec.signX() * GameObject.BLOCKSIZE*-1);
						}
						
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * checks if a collision between the player and an object occurs
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean checkPlayerObjectCollision(int x, int y) {
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
