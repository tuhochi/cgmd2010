package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.BasicTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundFour;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundOne;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundThree;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.CarrierRoundTwo;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.MoneyCarrier;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class GLView extends GLSurfaceView implements Renderer {
	
	public TextureManager texMan;
	private Activity mContext;
	private Vector< MoneyCarrier > mEnemies = null;
	
	//private int mEnemieCount = 0;
	private Gamefield mGamefield = null;
	private int mBasicTowerCounter = 0;
	private BasicTower[] mBasicTower = null; //tower types, wo gezeichnet in der towerklasse
	private int mAdvancedTowerCount = 0;

	private int mTowerTypeSelectedByPlayer = Definitions.BASIC_TOWER;
	private float mXPos, mYPos; //picking
	private int mWidth, mHeight; //viewport
	//private float mPassedTime = 0;
	private long mLastCollDetDone = 0;
	
	
	public GLView(Activity context, int w, int h) {
		super(context);
		System.out.println("GLVIEW CONSTRUCTOR");
		this.setRenderer( this );
		mContext = context;
		mWidth = w;
		mHeight = h;
	}
	
	@Override
	public void onResume(){
		if( mEnemies != null )System.out.println("On Resume - Enemies count: "+mEnemies.size());
		if( mEnemies != null ) for( int i = 0; i < mEnemies.size(); i++) mEnemies.get(i).initVBOs();
		if( mBasicTower != null) for( int i = 0; i < mBasicTower.length; i++) mBasicTower[i].initVBOs();
		if( mGamefield != null) mGamefield.onResume();
		super.onResume();
	}

	@Override
	public void onPause(){
		if( mEnemies != null )System.out.println("On Pause - Enemies count: "+mEnemies.size());
		super.onPause();
	}
	
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		//texMan.setTexture(R.drawable.l12_grass);
		mGamefield.draw(gl);	
		System.out.println("mBasicTowerLength: "+mBasicTower.length+" mEnemies: "+mEnemies.size());
		for ( int i = 0; i < mBasicTower.length; i++){
			if(mBasicTower[i].getActiveState()) mBasicTower[i].draw(gl); 
			System.out.println("mBasicTower "+i+" is active at: "+mBasicTower[i].getX()+" "+mBasicTower[i].getY());
		}
		for ( int i = 0; i < mEnemies.size(); i++){
				boolean remove = false;
				if(mEnemies.get(i).getActiveState()) mEnemies.get(i).draw(gl);
				if( mEnemies.get(i).getX() <= 1.0f) {
					GameMechanics.getGameMecanics().addMoney( mEnemies.get(i).getMoney() );
					mEnemies.get(i).deactivate();
					remove = true;
				}
				if( mEnemies.get(i).getHP() <= 0 ){
					mEnemies.get(i).deactivate();
					remove = true;
				}
				if( remove ) mEnemies.remove(i);
			}
		if( System.currentTimeMillis() - mLastCollDetDone > Definitions.COLLISION_DETECTION_TIMEOUT ) calcCollisions();
		if( GameMechanics.getGameMecanics().getRoundNumber() >= 0) {
			if( System.currentTimeMillis() - GameMechanics.getGameMecanics().getRoundStartedTime() > Definitions.GAME_ROUND_WAIT_TIME ){
				initEnemies();
				GameMechanics.getGameMecanics().setRoundStartedTime();
				GameMechanics.getGameMecanics().nextRound();
			}
		}
		if( (GameMechanics.getGameMecanics().getRoundNumber() < 0) && (mEnemies.size() == 0) ){
			mContext.finish();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		System.out.println("ON SURFACE CHANGED!");
		 gl.glMatrixMode(GL10.GL_PROJECTION);    
	     gl.glOrthof(0.0f, width, 0.0f, height, -1.0f, 10.0f);
	     gl.glViewport(0, 0, width, height);   
	     gl.glMatrixMode(GL10.GL_MODELVIEW);  
	     // enable the differentiation of which side may be visible 
	     //gl.glEnable(GL10.GL_CULL_FACE);
	     // which is the front? the one which is drawn counter clockwise
	     gl.glFrontFace(GL10.GL_CCW);
	     // which one should NOT be drawn
	    // gl.glCullFace(GL10.GL_BACK);   
	     gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	     gl.glEnableClientState(GL10.GL_COLOR_ARRAY);   
	     gl.glShadeModel(GL10.GL_SMOOTH);
	     gl.glClearColor(0.0f, 0.49321f, 0.49321f, 1.0f); 
	     gl.glClearDepthf(1.0f);
	     gl.glEnable(GL10.GL_DEPTH_TEST);
	     gl.glEnable(GL10.GL_LINE_SMOOTH);
	     gl.glDepthFunc(GL10.GL_LEQUAL);
	     gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
	     gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);	
	     gl.glDisable(GL10.GL_DITHER);
	}
	

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		System.out.println("ON SURFACE CREATED!");	
		texMan = TextureManager.getSingletonObject();
		texMan.initialize(gl, mContext);
		texMan.add(R.drawable.l12_grass1);
		texMan.add(R.drawable.l12_grass2);
		texMan.add(R.drawable.l12_basic_tower);
		texMan.add(R.drawable.l12_advanced_tower);
		texMan.add(R.drawable.l12_hyper_tower);
		texMan.add(R.drawable.l12_advanced_tower);
		texMan.add(R.drawable.l12_basic_projectile);
		texMan.add(R.drawable.l12_enemie_lvl0);
		texMan.add(R.drawable.l12_enemie_lvl1);
		texMan.add(R.drawable.l12_enemie_lvl2);
		texMan.add(R.drawable.l12_enemie_lvl3);
		texMan.add(R.drawable.l12_icon);	
		texMan.loadTextures();	
		initGameField( mWidth, mHeight);
		initTower();
		for( int i = 0; i < mBasicTower.length; i++ ) mBasicTower[i].setViewPortLength( mWidth );
		if( mEnemies == null ) mEnemies = new Vector< MoneyCarrier >();
	}
	
	public void initGameField( int width, int height ){
		int xSegCount = Definitions.FIELD_WIDTH_SEGMENTS;
		int ySegCount = Definitions.FIELD_HEIGHT_SEGMENTS;
		float segLength = height / ySegCount;
		if( mGamefield == null) mGamefield = new Gamefield( xSegCount, ySegCount, segLength );
	}
	
	
	public void setXYpos(float xpos, float ypos) {
		if( mGamefield.getOccupied( xpos, ypos )) return;
		float[] correctXYpos = mGamefield.correctXYpos( xpos, ypos);
		mXPos = correctXYpos[0];
		mYPos = correctXYpos[1];
			switch ( mTowerTypeSelectedByPlayer ){
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
		short roundnr = GameMechanics.getGameMecanics().getRoundNumber();
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
					mEnemies.add( carrier );
				}
				break;
			case(1):
				for( int i = 0; i < Definitions.SECOND_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundTwo();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
					carrier.activate();
					mEnemies.add( carrier );
				}
				break;
			case(2):
				for( int i = 0; i < Definitions.THIRD_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundThree();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
					carrier.activate();
					mEnemies.add( carrier );
				}
				break;
			case(3):
				for( int i = 0; i < Definitions.FOURTH_ROUND_ENEMIE_NUMBER; i++){
					MoneyCarrier carrier = new CarrierRoundFour();
					float lane = rand.nextInt((int)mHeight);
					float[] correctXYpos = mGamefield.correctXYpos( mWidth, lane);
					carrier.setXY( correctXYpos[0], correctXYpos[1] );
					carrier.activate();
					mEnemies.add( carrier );
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
		//if( mBasicTower.length == 0){System.out.println("TowerCount = 0 "); return;}
		//if( mEnemieCount == 0) {System.out.println("EnemieCount = 0 "); return;}
		//simple stupid way
		for( int i = 0; i < mBasicTower.length; i++){
			if( mBasicTower[i].getActiveState()){	
				for( int j = 0; j < mEnemies.size() ; j++){
					if( mEnemies.get(j).getActiveState() && mEnemies.get(j).getY() == mBasicTower[i].getY() ){
						mBasicTower[i].collideX( mEnemies.get(j) );
					}
				}
			}
		}
	}
	
	//***************************
	//TODO: Life-Cycle Implementierung: speichern von spieldaten, wiederherstellen der spieldaten
	//
	
}
