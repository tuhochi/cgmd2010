package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.AdvancedTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.BasicTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundFour;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundOne;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundThree;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundTwo;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.FreezeTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.HyperTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.MoneyCarrier;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.Tower;

public class GameWorld {
	private static GameWorld mSingleton = null; /** Gamefield singleton */
	
	private Vector< MoneyCarrier > mEnemies = null; /** Enemies on the field */
	private Gamefield mGamefield = null; /** the gamefield */ 
	
	private BasicTower[] mBasicTower = null; /** basic tower pool */
	private AdvancedTower[] mAdvancedTower = null; /** advanced tower pool */
	private HyperTower[] mHyperTower = null; /** hyper tower pool */
	private FreezeTower[] mFreezeTower = null; /** freeze tower pool */
	
	private int mXPos, mYPos; /** forwarded picking coordinate */
	private static int mWidth = -1; /** width of the gamefield */
	private static int mHeight = -1; /**height of the gamefield */
	static int res[]; 
	        
	boolean remove;
	
	private int carrierOneChance = 0;
	private int carrierTwoChance = 0;
	private int carrierThreeChance = 0;
	private int carrierFourChance = 0;
	
	//private static int mCTDsound = R.raw.l12_ctd;
	
	/** constructor, initializes the gamefield, the towerpools and one enemy of each enemy to for adding the textures */
	private GameWorld(){
		initGameField();
		initTower();
		//for initializing textures:
		MoneyCarrier c= new CarrierRoundOne();
		c = new CarrierRoundTwo();
		c = new CarrierRoundThree();
		c = new CarrierRoundFour();

		//SoundHandler.getSingleton().addResource(mCTDsound);
	}
	
	/** playing the CTD-sound */
	public void playCTDSound(){
		//SoundHandler.getSingleton().play(mCTDsound);
	}
	
	/** setting the display size == size of gamefield */
	public static void setDisplay( int height, int width){
		mWidth = width;
		mHeight = height;
	}
	
	/** returning the resolution */
	public static int[] getRes(){
		res = new int[2];
		res[0] = mWidth;
		res[1] = mHeight;
		return res;
	}
	
	/** returns the gameworld singleton, and creates one first is there is none */
	public static GameWorld getSingleton(){
		if( mSingleton == null && mHeight > 0) mSingleton = new GameWorld();
		return mSingleton;
	}
	
	/** (re)creates the opengl VBOs, calls the initVBOs method of each enemie on the field and each tower */
	public void initVBOs(){
		//if( mEnemies != null )System.out.println("On Resume - Enemies count: "+mEnemies.size());
		if( mEnemies != null ) {
			synchronized( mEnemies ){
				for( int i = 0; i < mEnemies.size(); i++) mEnemies.get(i).initVBOs();
			}	
		}
		if( mBasicTower != null){
			for( int i = 0; i < mBasicTower.length; i++) {
				if( mBasicTower[i].getActiveState())mBasicTower[i].initVBOs();
			}
		}
		if( mAdvancedTower != null ){
			for( int i = 0; i < mAdvancedTower.length; i++){
				if( mAdvancedTower[i].getActiveState())mAdvancedTower[i].initVBOs();
			}
		}
		if( mHyperTower != null ){
			for( int i = 0; i < mHyperTower.length; i++){
				if( mHyperTower[i].getActiveState())mHyperTower[i].initVBOs();
			}
		}
		if( mFreezeTower != null ){
			for( int i = 0; i < mFreezeTower.length; i++){
				if( mFreezeTower[i].getActiveState())mFreezeTower[i].initVBOs();
			}
		}
		if( mGamefield != null) mGamefield.onResume();
	}
	
	/** 
	 * initializes the gamefield with the defined segment count in the x-direction and the defined lane count (y-directional segments)
	 */
	public void initGameField(){
		int ySegCount = Definitions.FIELD_HEIGHT_SEGMENTS;
		float segLength = mHeight / ySegCount;
		Definitions.FIELD_SEGMENT_LENGTH = (int)Math.ceil(segLength);
		int xSegCount = (int) Math.ceil( mWidth / segLength );
		if( mGamefield == null) mGamefield = new Gamefield( xSegCount, ySegCount, segLength );
	}
	
	/**
	 * corrects the forwarded x-, y-coordinates from the picking to the center of the field in which the coordinates lie, and sets the next unused tower from the tower pool. 
	 * Type of the tower which gets setted is chosen through the GameUI
	 * @param xpos x-picking-coordinate
	 * @param ypos y-picking-coordinate
	 */
	public void setXYpos(int xpos, int ypos) {
		if( mGamefield.getOccupied( xpos, ypos )) return;
		int[] correctXYpos = mGamefield.correctXYpos( xpos, ypos);
		mXPos = correctXYpos[0];
		mYPos = correctXYpos[1];
		boolean last = false;
			switch ( GameMechanics.getSingleton().getSelectedTower() ){
				case Definitions.BASIC_TOWER:
					if( mBasicTower[0].getPrice() <= GameMechanics.getSingleton().getIron()){
						for( int i = 0; i < Definitions.BASIC_TOWER_POOL && !last; i++){
							if( mBasicTower[i].getActiveState() == false){
								mBasicTower[i].setXY(mXPos, mYPos);
								last = true;
								mGamefield.setFieldOccupied(mXPos, mYPos);
								GameMechanics.getSingleton().subIron( mBasicTower[i].getPrice());
								GameMechanics.getSingleton().addBasicTowerBuilt();
								break;
							}
						if ( i == mBasicTower.length -1 ) last = true;
						}
					}
					break;
				case Definitions.ADVANCED_TOWER:
					if( mAdvancedTower[0].getPrice() <= GameMechanics.getSingleton().getIron()){
						for( int i = 0; i < Definitions.ADVANCED_TOWER_POOL && !last; i++){
							if( mAdvancedTower[i].getActiveState() == false){
								mAdvancedTower[i].setXY(mXPos, mYPos);
								last = true;
								mGamefield.setFieldOccupied(mXPos, mYPos);
								GameMechanics.getSingleton().subIron(mAdvancedTower[i].getPrice());
								GameMechanics.getSingleton().addAdvancedTowerBuilt();
								break;
							}
							if ( i == mAdvancedTower.length -1 ) last = true;
						}
					}
					break;
				case Definitions.HYPER_TOWER:
					if( mHyperTower[0].getPrice() <= GameMechanics.getSingleton().getIron()){
						for( int i = 0; i < Definitions.HYPER_TOWER_POOL && !last; i++){
							if( mHyperTower[i].getActiveState() == false){
								mHyperTower[i].setXY(mXPos, mYPos);
								last = true;
								mGamefield.setFieldOccupied(mXPos, mYPos);
								GameMechanics.getSingleton().subIron(mHyperTower[i].getPrice());
								GameMechanics.getSingleton().addHyperTowerBuilt();
								break;
							}
							if ( i == mHyperTower.length -1 ) last = true;
						}
					}
					break;
				case Definitions.FREEZE_TOWER:
					if( mFreezeTower[0].getPrice() <= GameMechanics.getSingleton().getIron()){
						for( int i = 0; i < Definitions.FREEZE_TOWER_POOL && !last; i++){
							if( mFreezeTower[i].getActiveState() == false){
								mFreezeTower[i].setXY(mXPos, mYPos);
								last = true;
								mGamefield.setFieldOccupied(mXPos, mYPos);
								GameMechanics.getSingleton().subIron(mFreezeTower[i].getPrice());
								GameMechanics.getSingleton().addFreezeTowerBuilt();
								break;
							}
							if ( i == mFreezeTower.length -1 ) last = true;
						}
					}
					break;
				default:
					System.out.println("Selected TowerType not found!");
					break;
			}
	}
	
	/**
	 * Initializes enemies at each beginning round. In every round each enemy has a procentual chance to be spawned and set on a random lane (y-coordinate) and a random x-position random off screen.
	 */
	public void initEnemies(){
		short roundnr = GameMechanics.getSingleton().getRoundNumber();
		if( mEnemies == null ) mEnemies = new Vector< MoneyCarrier >();
		Random rand = new Random();
		if( roundnr > Definitions.MAX_ROUND_NUMBER) return;
		switch (roundnr) {
			case (0):
				for( int i = 0; i < Definitions.FIRST_ROUND_ENEMIE_NUMBER; i++){
					carrierOneChance = 100;
					carrierTwoChance = 0;
					carrierThreeChance = 0;
					MoneyCarrier carrier;
					if( carrierOneChance >= carrierTwoChance && carrierOneChance >= carrierThreeChance ) carrier = new CarrierRoundOne();
					else if( carrierTwoChance > carrierOneChance && carrierTwoChance > carrierThreeChance) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierOneChance && carrierThreeChance > carrierTwoChance) carrier = new CarrierRoundThree();
					else carrier = new CarrierRoundOne();
					int lane = rand.nextInt(mHeight);
					int xplus = 40;//rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			case(1):
				for( int i = 0; i < Definitions.SECOND_ROUND_ENEMIE_NUMBER; i++){
					if(i < Definitions.SECOND_ROUND_ENEMIE_NUMBER-1){
						carrierOneChance = 1;
						carrierTwoChance = 0;
						carrierThreeChance = 0;
					}
					else {
						carrierOneChance = 0;
						carrierTwoChance = 1;
						carrierThreeChance = 0;
					}
					MoneyCarrier carrier;
					if( carrierOneChance >= carrierTwoChance && carrierOneChance >= carrierThreeChance ) carrier = new CarrierRoundOne();
					else if( carrierTwoChance > carrierOneChance && carrierTwoChance > carrierThreeChance) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierOneChance && carrierThreeChance > carrierTwoChance) carrier = new CarrierRoundThree();
					else carrier = new CarrierRoundOne();
					int lane = rand.nextInt(mHeight);
					int xplus = 60;//rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			case(2):
				for( int i = 0; i < Definitions.THIRD_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = 1;
					int carrierThreeChance = 0;
					int carrierFourChance = 0;
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundThree();
					int lane = rand.nextInt(mHeight);
					int xplus = 50;
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			case(3):
				for( int i = 0; i < Definitions.FOURTH_ROUND_ENEMIE_NUMBER; i++){
					if(i < Definitions.FOURTH_ROUND_ENEMIE_NUMBER-2){
						carrierTwoChance = 1;
						carrierThreeChance = 0;
						carrierFourChance = 0;
					}
					else {
						carrierTwoChance = 0;
						carrierThreeChance = 1;
						carrierFourChance = 0;
					}
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = 50;
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			case(4):
				for( int i = 0; i < Definitions.FIFTH_ROUND_ENEMIE_NUMBER; i++){
					if(i < Definitions.FIFTH_ROUND_ENEMIE_NUMBER-1){
						carrierTwoChance = 0;
						carrierThreeChance = 1;
						carrierFourChance = 0;
					}
					else {
						carrierTwoChance = 0;
						carrierThreeChance = 0;
						carrierFourChance = 1;
					}
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = 50;
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			case(5):
				for( int i = 0; i < Definitions.SIXTH_ROUND_ENEMIE_NUMBER; i++){
					 carrierTwoChance = 0;
					 carrierThreeChance = 0;
					 carrierFourChance = 1;
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = 100;
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			case(6):
				for( int i = 0; i < Definitions.SEVENTH_ROUND_ENEMIE_NUMBER; i++){
					 carrierTwoChance = 0;
					 carrierThreeChance = 0;
					 carrierFourChance = 1;
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = 50;
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + i*xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
					GameMechanics.getSingleton().addSpawnedEnemie();
				}
				break;
			default:
				System.out.println("Default selected in roundnummer case, should not get here! roundnummer: "+roundnr);
				break;
		}	
		
	}
	
	/**
	 * Creates a pool of towers at the beginning of the game.
	 */
	public void initTower(){
		//BasicTower init
		if( mBasicTower == null){
			mBasicTower = new BasicTower[ Definitions.BASIC_TOWER_POOL ];
			for ( int i = 0; i < mBasicTower.length; i++) mBasicTower[i] = new BasicTower();
		}
		if( mAdvancedTower == null){
			mAdvancedTower = new AdvancedTower[ Definitions.ADVANCED_TOWER_POOL ];
			for ( int i = 0; i < mAdvancedTower.length; i++) mAdvancedTower[i] = new AdvancedTower();
		}
		if( mHyperTower == null){
			mHyperTower = new HyperTower[ Definitions.HYPER_TOWER_POOL ];
			for ( int i = 0; i < mHyperTower.length; i++) mHyperTower[i] = new HyperTower();
		}
		if( mFreezeTower == null){
			mFreezeTower = new FreezeTower[ Definitions.FREEZE_TOWER_POOL ];
			for ( int i = 0; i < mFreezeTower.length; i++) mFreezeTower[i] = new FreezeTower();
		}
	}
	
	/**
	 * On every call the collision detection is done by stepping through each tower on the gamefield, 
	 * checking if there is an enemy on the lane (y-coordinate is the same as the tower's), and if more than one which is the nearest enemy. 
	 * Then the found enemy is collided by the tower's projectile in the tower's collideX method. If the enemy is in the tower's quad the 
	 * tower starts the dying animation cycle. 
	 */
	public void calcCollisions(){
		if( mEnemies == null ) return;
		//simple stupid way
		for( int i = 0; i < mBasicTower.length; i++){
			if( mBasicTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mBasicTower[i].getY() && mEnemies.get(j).getBackX() >= mBasicTower[i].getFrontX() ){
							mBasicTower[i].collideX( mEnemies.get(j) );
							if( mEnemies.get(j).getFrontX() <= mBasicTower[i].getFrontX() && mEnemies.get(j).getBackX() >= mBasicTower[i].getBackX() ){
								mBasicTower[i].die();
								}
							}
						}
					}
				}
			if( mBasicTower[i].toRemove() ){
				mBasicTower[i].reset();
				mGamefield.setFieldUnOccupied(mBasicTower[i].getX(), mBasicTower[i].getY());
			}
		}
		for( int i = 0; i < mAdvancedTower.length; i++){
			if( mAdvancedTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mAdvancedTower[i].getY() && mEnemies.get(j).getBackX() >= mAdvancedTower[i].getFrontX() ){
							mAdvancedTower[i].collideX( mEnemies.get(j) );
							if( mEnemies.get(j).getFrontX() <= mAdvancedTower[i].getFrontX() && mEnemies.get(j).getBackX() >= mAdvancedTower[i].getBackX() ){
								mAdvancedTower[i].die();
								}
							}
						}
					}
				}
			if( mAdvancedTower[i].toRemove() ){
				mAdvancedTower[i].reset();
				mGamefield.setFieldUnOccupied(mAdvancedTower[i].getX(), mAdvancedTower[i].getY());
			}
		}
		for( int i = 0; i < mHyperTower.length; i++){
			if( mHyperTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mHyperTower[i].getY() && mEnemies.get(j).getBackX() >= mHyperTower[i].getFrontX() ){
							mHyperTower[i].collideX( mEnemies.get(j) );
							if( mEnemies.get(j).getFrontX() <= mHyperTower[i].getFrontX() && mEnemies.get(j).getBackX() >= mHyperTower[i].getBackX() ){
								mHyperTower[i].die();
								}
							}
						}
					}
				}
			if( mHyperTower[i].toRemove() ){
				mHyperTower[i].reset();
				mGamefield.setFieldUnOccupied(mHyperTower[i].getX(), mHyperTower[i].getY());
			}
		}
		for( int i = 0; i < mFreezeTower.length; i++){
			if( mFreezeTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mFreezeTower[i].getY() && mEnemies.get(j).getBackX() >= mFreezeTower[i].getFrontX() ){
							mFreezeTower[i].collideX( mEnemies.get(j) );
							if( mEnemies.get(j).getFrontX() <= mFreezeTower[i].getFrontX() && mEnemies.get(j).getBackX() >= mFreezeTower[i].getBackX() ){
								mFreezeTower[i].die();
								}
							}
						}
					}
				}
			if( mFreezeTower[i].toRemove() ){
				mFreezeTower[i].reset();
				mGamefield.setFieldUnOccupied(mFreezeTower[i].getX(), mFreezeTower[i].getY());
			}
		}
	}
	
	
	/**
	 * returns the gamefield
	 * @return GameField the gamefield
	 */
	public Gamefield getGamefield(){
		return mGamefield;
	}

	/**
	 * returns all towers on the field
	 * @return vector<Tower> the towers
	 */
	public Vector<Tower> getTower(){
		Vector<Tower> ret = new Vector<Tower>();	
		for ( int i = 0; i < mBasicTower.length; i++){
			if( mBasicTower[i].getActiveState()) ret.add( mBasicTower[i] ); 
		}
		for ( int i = 0; i < mAdvancedTower.length; i++){
			if( mAdvancedTower[i].getActiveState()) ret.add( mAdvancedTower[i] ); 
		}
		for ( int i = 0; i < mHyperTower.length; i++){
			if( mHyperTower[i].getActiveState()) ret.add( mHyperTower[i] ); 
		}
		for ( int i = 0; i < mFreezeTower.length; i++){
			if( mFreezeTower[i].getActiveState()) ret.add( mFreezeTower[i] ); 
		}
		return ret;
	}
	
	
	
	/** 
	 * For every enemy on the field, at first it is checked if the enemy has reached the house, if so money is added in the gamemechanics, and the enemy is removed.
	 * next the enemy is checked if it hast 0 (or lower) hitpoints, if so the enemy starts the dying animation texture cycle. If this cycle has finished, the enemy gets removes from the field.
	 * else the draw method is called.
	 */
 	public void drawEnemies(GL10 gl){
        if( mEnemies == null ) return;
        synchronized( mEnemies ){
            for ( int i = 0; i < mEnemies.size(); i++){
                remove = false;
                mEnemies.get(i).draw(gl);
                if( mEnemies.get(i).getX() <= 1.0f) {
                    //GameMechanics.getSingleton().addMoney( mEnemies.get(i).getMoney() );
                    mEnemies.get(i).deactivate();
                    mEnemies.remove(i);
                    i--;
                }
                else if( mEnemies.get(i).getHP() <= 0 ){
                    mEnemies.get(i).die();
                    if ( mEnemies.get(i).toRemove() ){
                    	GameMechanics.getSingleton().removeMoney( mEnemies.get(i).getMoney() );
                       	mEnemies.get(i).deactivate();
                    	mEnemies.remove(i);
                    	GameMechanics.getSingleton().addKilledEnemie();
                    	i--;
                    }
                }
            }
        }
}

 	/** calls the draw method of each tower	 */
   public void drawTowers(GL10 gl){
        for ( int i = 0; i < mBasicTower.length; i++){
            if( mBasicTower[i].getActiveState()){
            	mBasicTower[i].draw(gl);
            }
        }
        for ( int i = 0; i < mAdvancedTower.length; i++){
            if( mAdvancedTower[i].getActiveState() ){
            	mAdvancedTower[i].draw(gl);
            }
        }
        for ( int i = 0; i < mHyperTower.length; i++){
            if( mHyperTower[i].getActiveState()){
            	mHyperTower[i].draw(gl);
            }
        }
        for ( int i = 0; i < mFreezeTower.length; i++){
            if( mFreezeTower[i].getActiveState()){
            	mFreezeTower[i].draw(gl);
            }
        }
    }
   
   /**
    * delivers the number of enemies on the gamefield
    * @return int the size of the enemy vector
    */
   public int getEnemiesSize(){
	   return mEnemies.size();
   }

	
   /** destroyes the gameworld singleton */
	public static void destroySingleton() {
		mSingleton = null;	
	}
}
