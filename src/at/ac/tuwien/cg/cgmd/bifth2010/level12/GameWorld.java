package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Random;
import java.util.Vector;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.BasicTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundFour;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundOne;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundThree;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundTwo;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.MoneyCarrier;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.Tower;

public class GameWorld {
	private static GameWorld mSingleton = null;
	
	private Vector< MoneyCarrier > mEnemies = null;
	private Gamefield mGamefield = null;
	
	private int mBasicTowerCounter = 0;
	private BasicTower[] mBasicTower = null; //tower types, wo gezeichnet in der towerklasse
	private int mAdvancedTowerCount = 0;
	
	private float mXPos, mYPos; //picking	
	private static int mWidth = -1;
	private static int mHeight = -1; //viewport
	private long mLastCollDetDone = 0;
	
	private static LevelActivity mGameContext = null;
	
	
	private GameWorld(){
		initGameField();
		initTower();
	}
	
	public static void setDisplay( int height, int width){
		mWidth = width;
		mHeight = height;
	}
	
	public static void setGameContext( LevelActivity gc ){
		mGameContext = gc;
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
		int xSegCount = Definitions.FIELD_WIDTH_SEGMENTS;
		int ySegCount = Definitions.FIELD_HEIGHT_SEGMENTS;
		float segLength = mHeight / ySegCount;
		if( mGamefield == null) mGamefield = new Gamefield( xSegCount, ySegCount, segLength );
	}
	
	public void setXYpos(float xpos, float ypos) {
		if( mGamefield.getOccupied( xpos, ypos )) return;
		float[] correctXYpos = mGamefield.correctXYpos( xpos, ypos);
		mXPos = correctXYpos[0];
		mYPos = correctXYpos[1];
			switch ( GameMechanics.getSingleton().getSelectedTower() ){
				case Definitions.BASIC_TOWER:
					boolean last = false;
					for( int i = mBasicTowerCounter; i < Definitions.BASIC_TOWER_POOL && !last; i++){
						if( mBasicTower[i].getActiveState() == false){
							mBasicTower[i].setXY(mXPos, mYPos);
							mBasicTowerCounter++;
							System.out.println("Setting tower at x/y: "+mXPos+"/"+mYPos);
							last = true;
							mGamefield.setFieldOccupied(mXPos, mYPos);
							break;
						}
						if ( i == mBasicTower.length -1 ) last = true;
					}
					//mNewTower = false;
					break;
				case Definitions.ADVANCED_TOWER:
					//TODO
					break;
				default:
					System.out.println("Selected TowerType not found!");
					break;
			}
	}
	
	
	public void initEnemies(){
		short roundnr = GameMechanics.getSingleton().getRoundNumber();
		if( mEnemies == null ) mEnemies = new Vector< MoneyCarrier >();
		System.out.println("INIT ENEMIES! roundnr: "+roundnr+" #Enemies: "+mEnemies.size());
		Random rand = new Random();
		switch (roundnr) {
			case (0):
				for( int i = 0; i < Definitions.FIRST_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundOne();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(1):
				for( int i = 0; i < Definitions.SECOND_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundTwo();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(2):
				for( int i = 0; i < Definitions.THIRD_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundThree();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
					carrier.activate();
					synchronized( mEnemies ){
						mEnemies.add( carrier );
					}
				}
				break;
			case(3):
				for( int i = 0; i < Definitions.FOURTH_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundFour();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
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
			System.out.println("mBASICTOWER == NULL");
			mBasicTower = new BasicTower[ Definitions.BASIC_TOWER_POOL ];
			for ( int i = 0; i < mBasicTower.length; i++) mBasicTower[i] = new BasicTower();
		}
	}
	
	
	public void calcCollisions(){
		mLastCollDetDone = System.currentTimeMillis();
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
	}
	
	public Gamefield getGamefield(){
		return mGamefield;
	}

	
	public Vector<Tower> getTower(){
		Vector<Tower> ret = new Vector<Tower>();	
		for ( int i = 0; i < mBasicTower.length; i++){
			if( mBasicTower[i].getActiveState()) ret.add( mBasicTower[i] ); 
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
