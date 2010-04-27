package at.ac.tuwien.cg.cgmd.bifth2010.level13;



import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.CopObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.MistressObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.StatusBar;

/**
 * 
 * 
 * @author Sebastian
 *
 * Handles game logic related stuff.
 * 
 * The functions of this class are either callback Functions
 *
 *
 */

public class GameControl {

	static int consumedBeer = 0;
	static int mistressCounter = 0;
	public static boolean drunkState = false;
	static int drunkTime = 175;
	static int currentDrunkTime = 0;
	static boolean inJail = false;
	//movement vector
	public static Vector2 movement = new Vector2(0, 0);
	
	/**
	 * needs to be called every frame to update game logic
	 */
	public static void update(){
		//update offset
		GameObject.updateOffset(movement);
		handleDrunkState();
		
	}
	

	public static void updateDrunkStatus(StatusBar drunkBar){
		drunkBar.updateScale( 1.0f/(float)drunkTime * (float)currentDrunkTime);
	}
	
	public static void movePlayer(float x,float y){
		
		if (!inJail && !drunkState){
		float deltaX = Math.abs( x - MyRenderer.screenWidth / 2.0f);
		float deltaY = Math.abs( y - MyRenderer.screenHeight / 2.0f);
		
		if(deltaX >= deltaY) {
			if(x < MyRenderer.screenWidth / 2.0f) {
				//move left
			
				movement.x = -MyRenderer.SPEED;
				movement.y = 0.0f;
			}
			else if(x > MyRenderer.screenWidth / 2.0f) {
				//move right
				movement.x = MyRenderer.SPEED;
				movement.y = 0.0f;
			}
		}
		else {
			//event starts at top left
			if(y < MyRenderer.screenHeight / 2.0f) {
				//move up
				movement.x = 0.0f;
				movement.y = MyRenderer.SPEED;
			}
			else if(y > MyRenderer.screenHeight / 2.0f) {
				//move up
				movement.x = 0.0f;
				movement.y = -MyRenderer.SPEED;
			}
		}} else if(!inJail && drunkState){
			float deltaX = Math.abs( x - MyRenderer.screenWidth / 2.0f);
			float deltaY = Math.abs( y - MyRenderer.screenHeight / 2.0f);
			
			if(deltaX >= deltaY) {
				if(x < MyRenderer.screenWidth / 2.0f) {
					//move left
				
					movement.x = MyRenderer.SPEED;
					movement.y = 0.0f;
				}
				else if(x > MyRenderer.screenWidth / 2.0f) {
					//move right
					movement.x =  -MyRenderer.SPEED;
					movement.y = 0.0f;
				}
			}
			else {
				//event starts at top left
				if(y < MyRenderer.screenHeight / 2.0f) {
					//move up
					movement.x = 0.0f;
					movement.y = -MyRenderer.SPEED;
				}
				else if(y > MyRenderer.screenHeight / 2.0f) {
					//move up
					movement.x = 0.0f;
					movement.y = MyRenderer.SPEED;
				}
			}}
			
		
		
		
		
	
		
	}
	
	
	
	
	public static void consumeBeer(){
		consumedBeer++;
	}
	
	/**
	 * Callback function that handles the drunk state of the Player.
	 * Called every frame within the update loop.
	 */
	
	private static void handleDrunkState(){
		if (consumedBeer >= 2){
			consumedBeer = 0;
			// Set player to drunk state
			currentDrunkTime = drunkTime;
			drunkState = true;
			
		}
		if(drunkState){

			if(currentDrunkTime > 0){
				currentDrunkTime--;
				
				/*
				if(player.rotation > 360)
				player.rotation = 0;
				else
				player.rotation+=3;
				*/
				
			}
			else{
				
				drunkState = false;

				if(inJail){
					//checkJailState when getting clean again dry again
					inJail = false;
					
				}
			}
		}
		
			
		
	}
	
	/**
	 * Function that is called by the Collision detection routine when the player encounters a cop.
	 * 
	 * @param cop 
	 */
	
	public static void encounterCop(CopObject cop){
	//	cop.isActive = false;
		//TODO: CREATE VARIABLE FOR ESCAPING COP LIKE ADRENALINE SHOTS TO PIC UP WITH A TIMER.
		if (drunkState){
			if (!inJail){
				
				//TODO: GENERATE "JAIL-FAIL" message
				inJail = true;
			}
		}
			
	}
	
	/**
	 * Function that is called by the Collision detection routine when the player encounters a prostitute.
	 * 
	 * @param mistress
	 */
	
	
	public static void encounterMistress(MistressObject mistress){
		mistress.isActive = false;
		mistressCounter++;
		
	}
	
	
}
