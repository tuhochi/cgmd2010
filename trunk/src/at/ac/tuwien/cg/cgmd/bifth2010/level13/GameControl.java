package at.ac.tuwien.cg.cgmd.bifth2010.level13;



import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.SoundManager.SoundFX;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.CopObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.MistressObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.PlayerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.StatusBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.TextureSingletons;

/**
 *
 *
 * @author Sebastian/arthur
 *
 * Handles game logic related stuff.
 *
 * The functions of this class are called from within the renderer.
 *
 *
 *
 */

public class GameControl {

		public static int MIN_DRUNK_LEVEL = 3; 
		public static int MAX_DRUNK_LEVEL = 6;
		
        public int consumedBeer = 0;
        private int mistressCounter = 0;
        private boolean ratArsedState = false;
        private boolean drunkState = false;
        private static final int DRUNKTIME = 200;
        private int currentDrunkTime = 0;
        private static final int BUSTTIME = 50;
        private static final int SEXTIME = 50;
        private static final int INVINCIBLETIME = 60;
        private boolean invincibleState = false;
        private int currentInvincibleTime = 0;
        private boolean sexState = false;
        private int currentSexTime = 0;
        private int currentBustTime = 0;
        private boolean inJail = false;
        private int money = 0;
        //movement vector
        private Vector2 movement = new Vector2(0, 0);
        //old movement vector
        private Vector2 oldMovement = new Vector2(0, 0);
       
        private static GameControl instance;
       
        public static GameControl getInstance() {
                if(instance == null) {
                        instance = new GameControl();
                }
                return instance;
        }
       
        //reset
        public static void reset() {
                instance = null;
        }
       
        /**
         * needs to be called every frame to update game logic
         */
        public void update(PlayerObject player){
                //update offset
                GameObject.updateOffset(movement);
                handleDrunkState();
                handleJailState();
                handleInvincibleState();
                handleSexState(player);
        }
       

        public void updateDrunkStatus(StatusBar drunkBar){
                drunkBar.updateScale( 1.0f/(float)DRUNKTIME * (float)currentDrunkTime);
        }
       
        public void updateJailStatus(StatusBar jailBar){
                if(inJail)
                        jailBar.updateScale(1.0f/(float)BUSTTIME * (float)currentBustTime);
                else
                        jailBar.updateScale(0.0f);
        }
       
        public void movePlayer(float x,float y){
                //remember old movement
                oldMovement = movement.clone();
               
                if (!inJail && !sexState){
                float deltaX = Math.abs( x - MyRenderer.screenWidth / 2.0f);
                float deltaY = Math.abs( y - MyRenderer.screenHeight / 2.0f);
       
                Log.d("df", "moving player");
                Log.d("df", "x: " +x + "y: " + y + " deltax: " + deltaX + " deltay: " + deltaY);
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
                }}
               
        }
       
       
       
        public void consumeBeer(){
                SoundManager.playSound(SoundFX.BURP);
                consumedBeer++;
                money--;
        }
       
        
        
        
       
        
        /**
         * Function that handles the drunk state of the Player.
         * Called every frame within the update loop.
         */
       
        private void handleDrunkState(){
        	  if (consumedBeer >= MIN_DRUNK_LEVEL && !drunkState){
                  // Set player to drunk state
                  drunkState = true;
                 
          }
        	

                
                if (consumedBeer >= MAX_DRUNK_LEVEL && !ratArsedState){
                	  ratArsedState = true;
                	  currentDrunkTime = DRUNKTIME;
                      SoundManager.playSound(SoundFX.DRUNK);
                      consumedBeer = 0;
                }	
                
                if (ratArsedState){
                	if(currentDrunkTime > 0  ){
                        currentDrunkTime--;

                	}
                	else{
                		MyRenderer.rotation = 0;
                        drunkState = false;
                        ratArsedState = false;
                       
               
                }
                }
                	
                


        }
       
        
        private void handleJailState(){
        	if(inJail){
        		currentBustTime--;
        		if(currentBustTime == 0){
        			invincibleState = true;
        			currentInvincibleTime = INVINCIBLETIME;
        			inJail = false;
        		}
        	}

        }
        
        private void handleInvincibleState() {
        	if(invincibleState) {
        		currentInvincibleTime--;
        		if(currentInvincibleTime == 0) {
        			invincibleState = false;
        		}
        	}
        }
       
        private void handleSexState(PlayerObject player) {
        	if(sexState) {
        		player.setTexture(TextureSingletons.getTexture("censored"));
        		if(currentSexTime > 0) {
        			currentSexTime--;
        		}
        		else {
        			sexState = false;
        		}
        	}
        	else {
        		player.setTexture(TextureSingletons.getTexture(PlayerObject.class.getSimpleName()));
        	}
        }
       
        /**
         * Function that is called by the Collision detection routine when the player encounters a cop.
         *
         * @param cop
         */
       
        public void encounterCop(CopObject cop){
                if ((ratArsedState && !inJail && !invincibleState) || (sexState && !inJail && !invincibleState)){
                                SoundManager.playSound(SoundFX.POLICE);
                                currentBustTime = BUSTTIME;
                                inJail = true;
                                consumedBeer = 0;
                                //stop player movement
                               // movement = new Vector2(0, 0);
                                //oldMovement = new Vector2(0, 0);
               
                }
                       
        }
       
        /**
         * Function that is called by the Collision detection routine when the player encounters a prostitute.
         *
         * @param mistress
         */
       
       
        public void encounterMistress(MistressObject mistress){
        	if(!sexState && !inJail && !invincibleState) {
                mistress.isActive = false;
                mistressCounter++;
                money = money - 10;
                SoundManager.playSound(SoundFX.ORGASM);
                currentSexTime = SEXTIME;
                sexState = true;
                //movement = new Vector2(0,0);
                //oldMovement = new Vector2(0, 0);
        	}
                
               
        }


        public int getMoney() {
                return money;
        }

        public boolean isDrunkState() {
                return drunkState;
        }
        
        public boolean isRatArsedState(){
        	return ratArsedState;
        }
        
        public Vector2 getMovement() {
                return movement;
        }

        public void setMovement(Vector2 movement) {
                this.movement = movement;
        }

        public Vector2 getOldMovement() {
                return oldMovement;
        }

        public void setOldMovement(Vector2 oldMovement) {
                this.oldMovement = oldMovement;
        }

		public boolean isSexState() {
			return sexState;
		}

		public boolean isInJail() {
			return inJail;
		}

		public boolean isInvincibleState() {
			return invincibleState;
		}
       
       
}
