package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Random;
import java.util.Vector;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.AdvancedTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.BasicTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundFour;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundOne;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundThree;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundTwo;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.HyperTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.MoneyCarrier;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.Tower;

public class GameWorld {
	private static GameWorld mSingleton = null;
	
	private Vector< MoneyCarrier > mEnemies = null;
	private Gamefield mGamefield = null;
	
	private int mBasicTowerCounter = 0;
	private BasicTower[] mBasicTower = null; //tower types, wo gezeichnet in der towerklasse
	private int mAdvancedTowerCounter = 0;
	private AdvancedTower[] mAdvancedTower = null;
	private int mHyperTowerCounter = 0;
	private HyperTower[] mHyperTower = null;
	
	private int mXPos, mYPos; //picking	
	private static int mWidth = -1;
	private static int mHeight = -1; //viewport
	//private long mLastCollDetDone = 0;
	
	//private static LevelActivity mGameContext = null;
	
	
	private GameWorld(){
		initGameField();
		initTower();
	}
	
	public static void setDisplay( int height, int width){
		mWidth = width;
		mHeight = height;
	}
	
	public static GameWorld getSingleton(){
		if( mSingleton == null && mHeight > 0) mSingleton = new GameWorld();
		return mSingleton;
	}
	
	
	public void initVBOs(){
		if( mEnemies != null )System.out.println("On Resume - Enemies count: "+mEnemies.size());
		if( mEnemies != null ) {
			synchronized( mEnemies ){
				for( int i = 0; i < mEnemies.size(); i++) mEnemies.get(i).initVBOs();
			}	
		}
		if( mBasicTower != null) for( int i = 0; i < mBasicTower.length; i++) mBasicTower[i].initVBOs();
		if( mGamefield != null) mGamefield.onResume();
	}
	
	public void initGameField(){
		int ySegCount = Definitions.FIELD_HEIGHT_SEGMENTS;
		float segLength = mHeight / ySegCount;
		Definitions.FIELD_SEGMENT_LENGTH = (int)Math.ceil(segLength);
		int xSegCount = (int) Math.ceil( mWidth / segLength );
		if( mGamefield == null) mGamefield = new Gamefield( xSegCount, ySegCount, segLength );
	}
	
	public void setXYpos(int xpos, int ypos) {
		if( mGamefield.getOccupied( xpos, ypos )) return;
		int[] correctXYpos = mGamefield.correctXYpos( xpos, ypos);
		mXPos = correctXYpos[0];
		mYPos = correctXYpos[1];
		boolean last = false;
			switch ( GameMechanics.getSingleton().getSelectedTower() ){
				case Definitions.BASIC_TOWER:
					for( int i = mBasicTowerCounter; i < Definitions.BASIC_TOWER_POOL && !last; i++){
						if( mBasicTower[i].getActiveState() == false){
							mBasicTower[i].setXY(mXPos, mYPos);
							mBasicTowerCounter++;
							last = true;
							mGamefield.setFieldOccupied(mXPos, mYPos);
							break;
						}
						if ( i == mBasicTower.length -1 ) last = true;
					}
					break;
				case Definitions.ADVANCED_TOWER:
					for( int i = mAdvancedTowerCounter; i < Definitions.ADVANCED_TOWER_POOL && !last; i++){
						if( mAdvancedTower[i].getActiveState() == false){
							mAdvancedTower[i].setXY(mXPos, mYPos);
							mAdvancedTowerCounter++;
							last = true;
							mGamefield.setFieldOccupied(mXPos, mYPos);
							break;
						}
						if ( i == mAdvancedTower.length -1 ) last = true;
					}
					break;
				case Definitions.HYPER_TOWER:
					for( int i = mHyperTowerCounter; i < Definitions.HYPER_TOWER_POOL && !last; i++){
						if( mHyperTower[i].getActiveState() == false){
							mHyperTower[i].setXY(mXPos, mYPos);
							mHyperTowerCounter++;
							last = true;
							mGamefield.setFieldOccupied(mXPos, mYPos);
							break;
						}
						if ( i == mHyperTower.length -1 ) last = true;
					}
					break;
				default:
					System.out.println("Selected TowerType not found!");
					break;
			}
	}
	
	
	public void initEnemies(){
		short roundnr = GameMechanics.getSingleton().getRoundNumber();
		if( mEnemies == null ) mEnemies = new Vector< MoneyCarrier >();
		Random rand = new Random();
		switch (roundnr) {
			case (0):
				for( int i = 0; i < Definitions.FIRST_ROUND_ENEMIE_NUMBER; i++){
					int carrierOneChance = rand.nextInt( 50 );
					int carrierTwoChance = rand.nextInt( 25 );
					int carrierThreeChance = rand.nextInt( 15 );
					MoneyCarrier carrier;
					if( carrierOneChance >= carrierTwoChance && carrierOneChance >= carrierThreeChance ) carrier = new CarrierRoundOne();
					else if( carrierTwoChance > carrierOneChance && carrierTwoChance > carrierThreeChance) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierOneChance && carrierThreeChance > carrierTwoChance) carrier = new CarrierRoundThree();
					else carrier = new CarrierRoundOne();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(1):
				for( int i = 0; i < Definitions.SECOND_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = rand.nextInt( 50 );
					int carrierThreeChance = rand.nextInt( 25 );
					int carrierFourChance = rand.nextInt( 15 );
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundTwo();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(2):
				for( int i = 0; i < Definitions.THIRD_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = rand.nextInt( 25 );
					int carrierThreeChance = rand.nextInt( 50 );
					int carrierFourChance = rand.nextInt( 25 );
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundThree();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(3):
				for( int i = 0; i < Definitions.FOURTH_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = rand.nextInt( 15 );
					int carrierThreeChance = rand.nextInt( 25 );
					int carrierFourChance = rand.nextInt( 50 );
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(4):
				for( int i = 0; i < Definitions.FIFTH_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = rand.nextInt( 5 );
					int carrierThreeChance = rand.nextInt( 25 );
					int carrierFourChance = rand.nextInt( 70 );
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(5):
				for( int i = 0; i < Definitions.SIXTH_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = rand.nextInt( 5 );
					int carrierThreeChance = rand.nextInt( 10 );
					int carrierFourChance = rand.nextInt( 85 );
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(6):
				for( int i = 0; i < Definitions.SEVENTH_ROUND_ENEMIE_NUMBER; i++){
					int carrierTwoChance = rand.nextInt( 5 );
					int carrierThreeChance = rand.nextInt( 5 );
					int carrierFourChance = rand.nextInt( 90 );
					MoneyCarrier carrier;
					if( carrierTwoChance >= carrierThreeChance && carrierTwoChance >= carrierFourChance ) carrier = new CarrierRoundTwo();
					else if( carrierThreeChance > carrierTwoChance && carrierThreeChance > carrierFourChance) carrier = new CarrierRoundThree();
					else if( carrierFourChance > carrierTwoChance && carrierFourChance > carrierThreeChance) carrier = new CarrierRoundFour();
					else carrier = new CarrierRoundFour();
					int lane = rand.nextInt(mHeight);
					int xplus = rand.nextInt(100);
					int[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0] + xplus, correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			default:
				System.out.println("Default selected in roundnummer case, should not get here! roundnummer: "+roundnr);
				break;
		}	
		
	}
		
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
	}
	
	
	public void calcCollisions(){
		//mLastCollDetDone = System.currentTimeMillis();
		if( mEnemies == null ) return;
		//simple stupid way
		for( int i = 0; i < mBasicTower.length; i++){
			if( mBasicTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mBasicTower[i].getY() ){
							mBasicTower[i].collideX( mEnemies.get(j) );
						}
					}
				}
			}
		}
		for( int i = 0; i < mAdvancedTower.length; i++){
			if( mAdvancedTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mAdvancedTower[i].getY() ){
							mAdvancedTower[i].collideX( mEnemies.get(j) );
						}
					}
				}
			}
		}
		for( int i = 0; i < mBasicTower.length; i++){
			if( mHyperTower[i].getActiveState()){
				synchronized( mEnemies ){
					for( int j = 0; j < mEnemies.size() ; j++){
						if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mHyperTower[i].getY() ){
							mHyperTower[i].collideX( mEnemies.get(j) );
						}
					}
				}
			}
		}
	}
	
	public Gamefield getGamefield(){
		return mGamefield;
	}

	
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
		return ret;
	}
	
	
	public Vector<MoneyCarrier> getEnemies(){
		if( mEnemies == null ) return null;
		synchronized( mEnemies ){
			for ( int i = 0; i < mEnemies.size(); i++){
				boolean remove = false;
				if( mEnemies.get(i).getX() <= 1.0f) {
					GameMechanics.getSingleton().addMoney( mEnemies.get(i).getMoney() );
					mEnemies.get(i).deactivate();
					remove = true;
				}
				if( mEnemies.get(i).getHP() <= 0 ){
					mEnemies.get(i).deactivate();
					remove = true;
				}
				if( remove ) mEnemies.remove(i);
			}
		}
		return mEnemies;
	}

	public static void destroySingleton() {
		mSingleton = null;	
	}
}
