package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;

/**
 * 
 * @author arthur/sebastian (group 13)
 *
 * Static class that contains methods for collision detection and handling.
 * These methods are invoked from the respective classes.
 *
 */
public class CollisionHelper {
	
	
	/**
	 * checks if a collision between the player and an object would occur
	 * @return
	 */
	public static boolean checkBackgroundCollision(int map[][]) {
		return checkBackgroundCollision(map, null);
		
	}
	
	/**
	 * checks if a collision between an object and a wall would occur.
	 * called within draw function of the object that needs to be checked against the Background
	 *
	 * @return
	 */
	
	public static boolean checkBackgroundCollision(int[][] map,GameObject gO) {
		int gameObjectXMin;
		int gameObjectYMin;
		int gameObjectXMax;
		int gameObjectYMax;
		
		float centerX;
		float centerY;
		
		centerX = ((MyRenderer.screenWidth / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		centerY = ((MyRenderer.screenHeight / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		
		
		if (gO == null){
			gameObjectXMin = (int)(centerX + GameObject.offset.x);
			gameObjectYMin = (int)(centerY + GameObject.offset.y);
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
		float centerX;
		float centerY;
		
		centerX = ((MyRenderer.screenWidth / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		centerY = ((MyRenderer.screenHeight / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
	
		
		//calculate player's min/max x/y - coordinates
		int playerXMin = (int)(centerX + GameObject.offset.x);
		int playerYMin = (int)(centerY + GameObject.offset.y);
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
