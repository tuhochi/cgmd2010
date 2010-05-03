package at.ac.tuwien.cg.cgmd.bifth2010.level13;



import at.ac.tuwien.cg.cgmd.bifth2010.level13.SoundManager.SoundFX;
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
 * The functions of this class are called from within the renderer.
 * 
 *
 *
 */

public class GameControl {

	private static int consumedBeer = 0;
	private static int mistressCounter = 0;
	public static boolean drunkState = false;
	private static final int DRUNKTIME = 150;
	private static int currentDrunkTime = 0;
	private static final int BUSTTIME = 50;
	private static int currentBustTime = 0;
	private static boolean inJail = false;
	private static SoundManager sound;
	private static boolean musicRunning = false;
	public static int money = 0;
	//movement vector
	public static Vector2 movement = new Vector2(0, 0);
	//old movement vector
	public static Vector2 oldMovement = new Vector2(0, 0);
	
	
	//reset variables
	public static void init() {
		consumedBeer = 0;
		mistressCounter = 0;
		drunkState = false;
		currentDrunkTime = 0;
		currentBustTime = 0;
		inJail = false;
		musicRunning = false;
		money = 0;
		//movement vector
		movement = new Vector2(0, 0);
		//old movement vector
		oldMovement = new Vector2(0, 0);
		GameTimer.getInstance().reset();
	}
	/**
	 * needs to be called every frame to update game logic
	 */
	public static void update(){
		//update offset
	
		GameObject.updateOffset(movement);
		handleDrunkState();
		handleJailState();
		
		//update game time
		GameTimer.getInstance().update();
	}
	

	public static void updateDrunkStatus(StatusBar drunkBar){
		drunkBar.updateScale( 1.0f/(float)DRUNKTIME * (float)currentDrunkTime);
	}
	
	public static void updateJailStatus(StatusBar jailBar){
		if(inJail)
			jailBar.updateScale(1.0f/(float)BUSTTIME * (float)currentBustTime);
		else
			jailBar.updateScale(0.0f);
	}
	
	public static void movePlayer(float x,float y){
		//remember old movement
		oldMovement = movement.clone();
		
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
		SoundManager.playSound(SoundFX.BURP);
		consumedBeer++;
		money--;
	}
	
	/**
	 * Function that handles the drunk state of the Player.
	 * Called every frame within the update loop.
	 */
	
	private static void handleDrunkState(){
		if (consumedBeer >= 5){
			consumedBeer = 0;
			// Set player to drunk state
			currentDrunkTime = DRUNKTIME;
			SoundManager.playSound(SoundFX.DRUNK);
			if (musicRunning == false){
				SoundManager.playSound(SoundFX.MUSIC);
				
				musicRunning = true;
			}
			
			
			drunkState = true;
			
		}
		if(drunkState){
			
			if(currentDrunkTime > 0){
				currentDrunkTime--;
				
			//	GameObject.drunkenRotation += 10;
				
				/*
				if(player.rotation > 360)
				player.rotation = 0;
				else
				player.rotation+=3;
				*/
				
			}
			else{
				SoundManager.pauseMusic();
				musicRunning = false;
				GameObject.drunkenRotation =0;
				drunkState = false;

			
			}
		}
		
			
		
	}
	
	private static void handleJailState(){
		if(inJail){
			if(currentBustTime > 0){
				currentBustTime--;
			}else{

			//checkJailState when getting clean again dry again
			inJail = false;
			
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
		if (drunkState & !inJail){
				SoundManager.playSound(SoundFX.POLICE);
				currentBustTime = BUSTTIME;
				inJail = true;
		
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
		money = money - 10;
		SoundManager.playSound(SoundFX.ORGASM);
		
	}
	
	
}
