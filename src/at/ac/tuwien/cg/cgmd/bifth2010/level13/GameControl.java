package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.SoundManager.SoundFX;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BackgroundObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BeerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.BeerStatusBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.CopObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.DrunkBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.GameObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.JailBar;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.MistressObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.PlayerObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects.TextureSingletons;

/**
 *
 *
 * @author group13
 *
 * handles game logic related stuff
 *
 */

public class GameControl implements IPersistence {

	/** movement speed of player */
	private static final int SPEED = 4;

	/** amount of beers required for drunk state */
	private static final int MIN_DRUNK_LEVEL = 2; 

	/** amount of beers required for rat-arsed state */
	public static final int MAX_DRUNK_LEVEL = 4;

	/** duration of rat-arsed state */
	private static final int DRUNKTIME = 80;

	/** duration of jail state */
	private static final int BUSTTIME = 50;

	/** duration of sex state */
	private static final int SEXTIME = 50;

	/** duration of invincible state */
	private static final int INVINCIBLETIME = 60;

	/** starting position of player */
	private static final Vector2 STARTTILE = new Vector2(3, 1);

	/** map code for beer objects */
	private static final int BEERCODE = 2;

	/** number of beer objects in map */
	private static final int BEERAMOUNT = 24;

	/** map code for cop objects */
	private static final int COPCODE = 3;

	/** number of cop objects in map */
	private static final int COPAMOUNT = 10;

	/** map code for mistress objects */
	private static final int MISTRESSCODE = 4;

	/** number of mistress objects in map */
	private static final int MISTRESSAMOUNT = 4;

	/** price of a beer */
	private static final int BEERPRICE = 3;

	/** price of a mistress */
	private static final int MISTRESSPRICE = 7;

	/** level map 
	 * 
	 * 0 = path
	 * 1 = wall / solid tile 
	 */
	private int map[][] = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 }, 
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, 
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 },
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1 },
			{ 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	/** amount of beer the player has drunken */
	private int consumedBeer;

	/** flag if player is in rat-arsed state (totally drunken) */
	private boolean ratArsedState;

	/** flag if player is in drunk state (light drunken) */
	private boolean drunkState;

	/** flag if player is in sex state (when player encounters a mistress) */
	private boolean sexState;

	/** flag if player is invincible (after player comes out of jail) */
	private boolean invincibleState;

	/** flag if player is in jail (after player is busted by cops) */
	private boolean jailState;

	/** current time of player in drunk state */
	private int currentDrunkTime;

	/** current time of player in invincible state */
	private int currentInvincibleTime;

	/** current time of player in sex state */
	private int currentSexTime;

	/** current time of player in jail state */
	private int currentBustTime;

	/** money spent by player */
	private int money;

	/** new movement of player */
	private Vector2 movement;

	/** old movement of player */
	private Vector2 oldMovement;

	/** all game objects (except status bars) */
	private List<GameObject> gameObjects;

	/** the player object */
	private PlayerObject player;

	/** status bar to show remaining time in rat-arsed state */
	private DrunkBar drunkStatusBar;

	/** status bar to show remaining time in jail */
	private JailBar jailStatusBar;

	/** status bar for drunken beer */
	private BeerStatusBar beerStatusBar;

	/** persistent properties saved at previous game */
	private Bundle savedInstanceState;

	/** singleton object of this class */
	private static GameControl instance;


	/**
	 * @see IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		//populate members with saved values
		this.consumedBeer = savedInstanceState.getInt("l13_gameControl_consumedBeer");
		this.ratArsedState = savedInstanceState.getBoolean("l13_gameControl_ratArsedState");
		this.drunkState = savedInstanceState.getBoolean("l13_gameControl_drunkState");
		this.jailState = savedInstanceState.getBoolean("l13_gameControl_jailState");
		this.currentDrunkTime = savedInstanceState.getInt("l13_gameControl_currentDrunkTime");
		this.currentBustTime = savedInstanceState.getInt("l13_gameControl_currentBustTime");
		this.money = savedInstanceState.getInt("l13_gameControl_money");

		for(int i = 0; i < map.length; i++) {
			this.map[i] = savedInstanceState.getIntArray("l13_gameControl_map" + i);
		}
		//save bundle for restoring game-objects (must be done later)
		this.savedInstanceState = savedInstanceState;
	}


	/**
	 * @see IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		//if game objects were not yet restored, just save previous values
		if(gameObjects.isEmpty()) {
			outState.putAll(savedInstanceState);
		}
		else {
			//write all required values for a restart into bundle
			outState.putInt("l13_gameControl_consumedBeer", consumedBeer);
			outState.putBoolean("l13_gameControl_ratArsedState", ratArsedState);
			outState.putBoolean("l13_gameControl_drunkState", drunkState);
			outState.putBoolean("l13_gameControl_jailState", jailState);
			outState.putInt("l13_gameControl_currentDrunkTime", currentDrunkTime);
			outState.putInt("l13_gameControl_currentBustTime", currentBustTime);
			outState.putInt("l13_gameControl_money", money);

			for(int i = 0; i < map.length; i++) {
				outState.putIntArray("l13_gameControl_map" + i, map[i]);
			}

			for(GameObject gameObject : gameObjects) {
				gameObject.save(outState);
			}
		}
	}

	/**
	 * resets the singleton object
	 */
	public static void reset() {
		instance = null;
	}


	/**
	 * default constructor initializes members
	 */
	public GameControl() {
		//init members
		this.consumedBeer = 0;
		this.ratArsedState = false;
		this.drunkState = false;
		this.sexState = false;
		this.invincibleState = false;
		this.jailState = false;
		this.currentDrunkTime = 0;
		this.currentInvincibleTime = 0;
		this.currentSexTime = 0;
		this.currentBustTime = 0;
		this.money = 0;
		this.movement = new Vector2(0, 0);
		this.oldMovement = new Vector2(0, 0);
		this.gameObjects = new ArrayList<GameObject>();
	}

	/**
	 * creates/returns a singleton object of this class
	 * 
	 * @return singleton object of this class
	 */
	public static GameControl getInstance() {
		if(instance == null) {
			instance = new GameControl();
		}
		return instance;
	}


	private void populateMap(int code, int amount) {
		Random random = new Random();
		for(int i = 0; i < amount;) {
			int x = random.nextInt(map.length);
			int y = random.nextInt(map[x].length);
			if(map[x][y] == 0) {
				map[x][y] = code;
				i++;
			}
		}
	}
	/**
	 * creates all game objects
	 */
	public void createGameObjects() {

		if(savedInstanceState == null) {
			populateMap(BEERCODE, BEERAMOUNT);
			populateMap(COPCODE, COPAMOUNT);
			populateMap(MISTRESSCODE, MISTRESSAMOUNT);
		}

		//background object
		gameObjects.add(new BackgroundObject());

		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				//create beer object
				if(map[i][j] == BEERCODE) {
					gameObjects.add(new BeerObject(j, Math.abs(i - map.length + 1), i + "." + j));
				}
				//create cop object
				else if(map[i][j] == COPCODE){
					gameObjects.add(new CopObject(j, Math.abs(i - map.length+1), i + "." + j));
				}
				//create mistress object
				else if(map[i][j] == MISTRESSCODE){
					gameObjects.add(new MistressObject(j, Math.abs(i - map.length+1), i + "." + j));
				}
			}
		}

		//create player object
		player = new PlayerObject();
		gameObjects.add(player);
		//set starting position of player
		GameObject.setStartTile(STARTTILE);

		//create status bars
		drunkStatusBar = new DrunkBar(200, 50);
		drunkStatusBar.setPosition(new Vector2(0,50));
		jailStatusBar = new JailBar(200, 50);
		jailStatusBar.setPosition(new Vector2(0, 50));
		beerStatusBar = new BeerStatusBar();


		//restore persistent values of game objects
		if(savedInstanceState != null) {
			for(GameObject gameObject : gameObjects) {
				gameObject.restore(savedInstanceState);
			}
		}

	}


	/**
	 * needs to be called every frame to update game logic
	 */
	public void update(){
		//update offset due to player movement
		GameObject.updateOffset();

		//update possible states
		handleDrunkState();
		handleJailState();
		handleInvincibleState();
		handleSexState();

		//update status bars
		updateDrunkStatusBar();
		updateJailStatusBar();

		//update game objects
		for(GameObject gameObject : gameObjects) {
			if (gameObject.isActive()){
				gameObject.update();
			}
		}

		//update game time
		GameTimer.getInstance().update();
	}


	/**
	 * updates the drunk status-bar (correct scale)
	 */
	private void updateDrunkStatusBar(){
		drunkStatusBar.updateScale( 1.0f/(float)DRUNKTIME * (float)currentDrunkTime);
	}


	/**
	 * updates the jail status-bar (correct scale)
	 */
	private void updateJailStatusBar(){
		if(jailState) {
			jailStatusBar.updateScale(1.0f/(float)BUSTTIME * (float)currentBustTime);
		}
		else {
			jailStatusBar.updateScale(0.0f);
		}
	}


	/**
	 * moves the player according to user-input
	 * @param x x-value of screen touch
	 * @param y y-value of screen touch
	 */
	public void movePlayer(float x,float y){
		//remember old movement
		oldMovement = movement.clone();

		//only move if player is not in jail and does not have sex
		if(!jailState && !sexState){
			//calculate distance in x/y-direction between center of screen and screen touch
			float deltaX = Math.abs( x - MyRenderer.getScreenWidth() / 2.0f);
			float deltaY = Math.abs( y - MyRenderer.getScreenHeight() / 2.0f);

			//move in the direction which has a greater delta
			if(deltaX >= deltaY) {
				//move left
				if(x < MyRenderer.getScreenWidth() / 2.0f) {
					movement.x = -SPEED;
					movement.y = 0;
				}
				//move right
				else if(x > MyRenderer.getScreenWidth() / 2.0f) {
					movement.x = SPEED;
					movement.y = 0;
				}
			}
			else {
				//move down
				if(y < MyRenderer.getScreenHeight() / 2.0f) {
					movement.x = 0;
					movement.y = SPEED;
				}
				//move up
				else if(y > MyRenderer.getScreenHeight() / 2.0f) {
					movement.x = 0;
					movement.y = -SPEED;
				}
			}
		}
	}


	/**
	 * method that handles the drunk state of the player.
	 * called every frame within the update loop.
	 */

	private void handleDrunkState(){
		//player transitions in drunk state if he has drunken a specified amount of beer (min_drunk_level)
		if(consumedBeer >= MIN_DRUNK_LEVEL && !drunkState){
			drunkState = true;
		}

		//player transitions in rat-arsed state if he has drunken a specified amount of beer (max_drunk_level)
		if(consumedBeer == MAX_DRUNK_LEVEL && !ratArsedState){
			ratArsedState = true;
			//set duration to be in state
			currentDrunkTime = DRUNKTIME;
			//player drunk sound
			SoundManager.getInstance().playSound(SoundFX.DRUNK);
		}	

		if(ratArsedState){
			//update remaining time in rat-arsed state
			if(currentDrunkTime > 0  ){
				currentDrunkTime--;

			}
			//leave rat-arsed/drunk state after remaining time
			else {
				drunkState = false;
				ratArsedState = false;

				//update spent money
				money -= consumedBeer * BEERPRICE;
				//reset beer counter
				consumedBeer = 0;

			}
		}
	}


	/**
	 * handles jail state
	 * called every frame
	 */
	private void handleJailState(){
		if(jailState){
			//update remaining time in state
			currentBustTime--;
			//leave state and go into invincible state
			if(currentBustTime == 0){
				invincibleState = true;
				currentInvincibleTime = INVINCIBLETIME;
				jailState = false;
			}
		}
	}


	/**
	 * handles the invincible state
	 * called every frame
	 */
	private void handleInvincibleState() {
		if(invincibleState) {
			//update remaining time in state
			currentInvincibleTime--;
			//leave state if time is up
			if(currentInvincibleTime == 0) {
				invincibleState = false;
			}
		}
	}


	/**
	 * handles the sex state
	 * called every frame
	 */
	private void handleSexState() {
		if(sexState) {
			//set censored texture in this state
			player.setTexture(TextureSingletons.getTexture("censored"));

			//update remaining time in this state
			if(currentSexTime > 0) {
				currentSexTime--;
			}

			//leave state if time is up
			else {
				sexState = false;
				//update spent money
				if(!jailState) {
					money -= MISTRESSPRICE;
				}
			}
		}
		//set normal player texture if not in this state
		else {
			player.setTexture(TextureSingletons.getTexture(PlayerObject.class.getSimpleName()));
		}
	}


	/**
	 * method that is called by the collision detection routine when the player encounters a cop.
	 *
	 * @param cop cop object which player encounters
	 */

	public void encounterCop(CopObject cop){
		//only encounter cop if player is drunken or has sex (and is not in jail or is invincible)
		if(((ratArsedState || drunkState) && !jailState && !invincibleState) || (sexState && !jailState && !invincibleState)){
			//stop other sounds
			SoundManager.getInstance().stopSounds();
			//player police sound
			SoundManager.getInstance().playSound(SoundFX.POLICE);

			//set duration of jail state
			currentBustTime = BUSTTIME;
			//transition player into jail state
			jailState = true;
			//reset beer counter
			consumedBeer = 0;
			ratArsedState = false;
			drunkState = false;
			currentDrunkTime = 0;
			sexState = false;
		}
	}


	/**
	 * method that is called by the collision detection routine when the player encounters a prostitute.
	 *
	 * @param mistress the mistress object the player encounters
	 */
	public void encounterMistress(MistressObject mistress){
		//only encounter mistress if player has no sex/is not in jail/is not invincible
		if(!sexState && !jailState && !invincibleState) {
			//remove mistress from rendering
			mistress.setActive(false);
			//play orgasm sound
			SoundManager.getInstance().playSound(SoundFX.ORGASM);
			//transition into sex state and update sex-state duration
			currentSexTime = SEXTIME;
			sexState = true;
		}
	}


	/**
	 * called when the player encounters a beer object
	 */
	public void encounterBeer(BeerObject beerObject){
		//dont drink beer when invincible or already totally drunken
		if(invincibleState || ratArsedState) {
			return;
		}
		//play beer sound
		SoundManager.getInstance().playSound(SoundFX.BURP);
		//increase beer counter
		consumedBeer++;

		//set beer inactive
		beerObject.setActive(false);
	}


	/**
	 * getter for money-member variable
	 * 
	 * @return money-member variable
	 */
	public int getMoney() {
		return money;
	}


	/**
	 * getter for drunkstate-member variable
	 * 
	 * @return drunkstate-member variable
	 */
	public boolean isDrunkState() {
		return drunkState;
	}


	/**
	 * getter for ratarsedstate-member variable
	 * 
	 * @return ratarsedstate-member variable
	 */
	public boolean isRatArsedState(){
		return ratArsedState;
	}


	/**
	 * getter for movement-member variable
	 * 
	 * @return movement-member variable
	 */
	public Vector2 getMovement() {
		return movement;
	}


	/**
	 * getter for oldmovement-member variable
	 * 
	 * @return oldmovement-member variable
	 */
	public Vector2 getOldMovement() {
		return oldMovement;
	}


	/**
	 * setter for oldmovement-member variable
	 * 
	 * @param oldMovement value member variable should be set to
	 */
	public void setOldMovement(Vector2 oldMovement) {
		this.oldMovement = oldMovement;
	}


	/**
	 * getter for sexstate-member variable
	 * 
	 * @return sexstate-member variable
	 */
	public boolean isSexState() {
		return sexState;
	}


	/**
	 * getter for jailstate-member variable
	 * 
	 * @return jailstate-member variable
	 */
	public boolean isJailState() {
		return jailState;
	}


	/**
	 * getter for invinciblestate-member variable
	 * 
	 * @return invinciblestate member-variable
	 */
	public boolean isInvincibleState() {
		return invincibleState;
	}


	/**
	 * getter for consumedbeer member-variable
	 * 
	 * @return consumedbeer member-variable
	 */
	public int getConsumedBeer() {
		return consumedBeer;
	}


	/**
	 * getter for gameobjects member-variable
	 * 
	 * @return gameobjects member-variable
	 */
	public List<GameObject> getGameObjects() {
		return gameObjects;
	}


	/**
	 * getter for player member-variable
	 * 
	 * @return player member-variable
	 */
	public PlayerObject getPlayer() {
		return player;
	}


	/**
	 * getter for drunkstatusbar member-variable
	 * 
	 * @return drunkstatusbar member-variable
	 */
	public DrunkBar getDrunkStatusBar() {
		return drunkStatusBar;
	}


	/**
	 * getter for jailstatusbar member-variable
	 * 
	 * @return jailstatusbar member-variable
	 */
	public JailBar getJailStatusBar() {
		return jailStatusBar;
	}


	/**
	 * getter for beerstatusbar member-variable
	 * 
	 * @return beerstatusbar member-variable
	 */
	public BeerStatusBar getBeerStatusBar() {
		return beerStatusBar;
	}


	/**
	 * getter for map member-variable
	 * 
	 * @return map member-variable
	 */
	public int[][] getMap() {
		return map;
	}
}
