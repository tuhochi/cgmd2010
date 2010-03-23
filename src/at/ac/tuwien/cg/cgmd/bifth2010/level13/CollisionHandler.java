
package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.List;

public class CollisionHandler {

	
	static int beerPlayerCollisions = 0;
	
	
	public static int checkBeerPlayerCollision(){
		
		return beerPlayerCollisions;
		
	}
	
	public static void handleScreenBoundaryCheck(GameObject avatar){
		
		//Limits the avatar to the screen boundaries (very hacky)
		 
		
		if(!(avatar.getPos().x+avatar.getSpeed().x < 3.5 && avatar.getPos().x+avatar.getSpeed().x > -3.5)||
		   !(avatar.getPos().y+avatar.getSpeed().y < 5.2 && avatar.getPos().y+avatar.getSpeed().y > -5.2)){
			avatar.stop();
		}
		
	}
	
	
	
	public static void reset(){
		beerPlayerCollisions = 0;
	}
	
	
	public static void responseLoop(List<GameObject> gameObjects){
		
		//Basic aabb collision detection
		reset();
		
		
		
		for (int i = 0; i < gameObjects.size(); i++){
			GameObject collidorA = gameObjects.get(i);

			
			for (int j = i+1; j < gameObjects.size(); j++){
				
				
				GameObject collidorB = gameObjects.get(j);

				if (collidorB.getActive()&& collidorA.getActive()){
				if (collidorA.getMinX() > collidorB.getMaxX() ||
					collidorA.getMaxX() < collidorB.getMinX() ||
					collidorA.getMinY() > collidorB.getMaxY() ||
					collidorA.getMaxY() < collidorB.getMinY()
					){
					//No collision
					
				}else{
					//Collision, handle response
					
					CollisionHandler.handleCollision(collidorA, collidorB);
				}
				
			}
				}
			}
			
	}
	
	
	
	public static void handleCollision(GameObject collidorA, GameObject collidorB){
		
	
		
		if((collidorA instanceof Beer && collidorB instanceof Player) ||
				(collidorB instanceof Beer && collidorA instanceof Player) ){
			beerPlayerCollisions++;
			if (collidorA instanceof Beer)
				collidorA.setActive(false);
			else
				collidorB.setActive(false);
		}
	}
	
	
	/*
	collidorA.setPos(collidorA.X()-collidorA.getSpeedX(),collidorA.Y()-collidorA.getSpeedY());
	collidorB.setPos(collidorB.X()-collidorB.getSpeedX(),collidorB.Y()-collidorB.getSpeedY());
	
	//Set speed to avoid getting stuck
	
	collidorA.setSpeedX(0);
	collidorA.setSpeedY(0);
	collidorB.setSpeedX(0);
	collidorB.setSpeedY(0);
*/
	
}
