package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.BasicTower;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.MoneyCarrier;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.BasicProjectile;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.Projectile;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.entities.Tower;

public class GLView extends GLSurfaceView implements Renderer {
	private Gamefield mGamefield = null;
	
	//private static final int CARRIER_SPAWN_INTERVALL_01 = 3; //wave 1 spawn intervall
	private static final int[] mCarrierWave = new int[Definitions.CARRIER_POOL];
	private MoneyCarrier[] mEnemies = new MoneyCarrier[ Definitions.CARRIER_POOL ];
	private int mEnemieCount = 0;
	
	private int mBasicTowerCounter = 0;
	private BasicTower[] mBasicTower = new BasicTower[ Definitions.BASIC_TOWER_POOL ]; //tower types, wo gezeichnet in der towerklasse
	private int mAdvancedTowerCount = 0;
	

	//private boolean mNewTower = false;
	private int mTowerTypeSelectedByPlayer = Definitions.BASIC_TOWER;
	private float mXPos, mYPos; //picking
	private float mWidth, mHeight; //viewport
	private float mStartTime;
	private float mPassedTime;
	private double mLastCollDetDone = 0;
	
	
	//xml werte zwecks frameworkportierung mal als klassenvariablen definiert.
	private int[] fieldsegments = {8,5};
	private int radius = 3;
	private int speed = 3333;
	private int dmg = 9;
	private float shootingInterval = 3;
	
	public GLView(Context context, float w, float h) {
		super(context);
		this.setRenderer( this );
		initTower();
		initProjectiles();
		mStartTime = System.currentTimeMillis();
		mWidth = w;
		mHeight = h;
	}
	
	
	
	

	@Override
	public void onDrawFrame(GL10 gl) {
		mPassedTime = mStartTime - System.currentTimeMillis();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -50.0f);
		mGamefield.draw(gl);	
		for ( int i = 0; i < mBasicTower.length; i++) if(mBasicTower[i].getActiveState()) mBasicTower[i].draw(gl);	
		
		//if(mPassedTime == mCarrierWave[0]) //if abfrage wird so nie true sein = passed time auf int casten!
		for ( int i = 0; i < mEnemies.length; i++)if(mEnemies[i].getActiveState()) mEnemies[i].draw(gl);
		
		//if( System.currentTimeMillis() - mLastCollDetDone > Definitions.COLLISION_DETECTION_TIMEOUT )
		//calcCollisions();
	}
	
	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, width, 0.0f, height, -1.0f, 10.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//TODO: eventuell spiellogik initialisierung von surface changed methode unabhängig machen
		//GameField:
		initGameField( width, height );
		mWidth = width;
		mHeight = height;
		for( int i = 0; i < mBasicTower.length; i++ ) mBasicTower[i].setViewPortLength( width );
		initEnemies();	
		for ( int i = 0; i < mEnemies.length; i++){mEnemies[i].activate();	mEnemieCount++;} 
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
        gl.glMatrixMode(GL10.GL_PROJECTION);
        
        gl.glOrthof(0.0f, mWidth, 0.0f, mHeight, -1.0f, 100.0f);
        gl.glViewport(0, 0, (int) mWidth, (int) mHeight);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
        // enable the differentiation of which side may be visible 
        gl.glEnable(GL10.GL_CULL_FACE);
        // which is the front? the one which is drawn counter clockwise
        gl.glFrontFace(GL10.GL_CCW);
        // which one should NOT be drawn
        gl.glCullFace(GL10.GL_BACK);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_LINE_SMOOTH);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	
		gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);	
		
		initGameField( (int)mWidth, (int)mHeight );
		for( int i = 0; i < mBasicTower.length; i++ ) mBasicTower[i].setViewPortLength( (int)mWidth );
		initEnemies();	
		for ( int i = 0; i < mEnemies.length; i++){mEnemies[i].activate();	mEnemieCount++;} 
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
							mBasicTower[i].setActiveState(true);
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
	
	
	
	public void initGameField( int width, int height ){
		//int[] fieldsegments = {8,5};//this.getResources().getIntArray(R.array.Field);
		int xSegCount = fieldsegments[0];
		int ySegCount = fieldsegments[1];
		float segLength = height / ySegCount;
		mGamefield = new Gamefield( xSegCount, ySegCount, segLength );
	}
	
	
	
	
	public void initTower(){
		//BasicTower init
		//int radius = (this.getResources().getIntArray(R.array.BasicTowerRadius))[0];
		for ( int i = 0; i < mBasicTower.length; i++) mBasicTower[i] = new BasicTower( radius );
	}
	
	
	
	public void initProjectiles(  ){
		//Basic Projectile for Basic Tower
		//int speed = this.getResources().getIntArray(R.array.BasicProjectileSpeed)[0];
		float speedf = speed/100;
		//int dmg = this.getResources().getIntArray(R.array.BasicProjectileDmg)[0];
		//float shootingInterval = this.getResources().getIntArray(R.array.BasicTowerShootingInterval)[0] / 100;
		for( int i = 0; i < mBasicTower.length; i++) mBasicTower[i].initProjectiles(speedf, dmg, shootingInterval);
	}
	
	
	public void initEnemies(){
		Random rand = new Random();
		float lane;
		float[] correctXYpos;
		for(int i=0; i < mEnemies.length; i++){
			mEnemies[i] = new MoneyCarrier();
			lane = rand.nextInt((int)mHeight);
			correctXYpos = mGamefield.correctXYpos( 10, lane);
			mEnemies[i].init(mWidth, correctXYpos[1], 2);
			
			mCarrierWave[i] = rand.nextInt(30); //
		}
	}
	
	
	public void calcCollisions(){
		//Log.d("draw", "kkkkkkkkkkkkk");
		//System.out.println("Doing Collision Detection!");
		mLastCollDetDone = System.currentTimeMillis();
		if( mBasicTower.length == 0){Log.d("draw", "ohoh!!"); return;}
		if( mEnemieCount == 0) {Log.d("enemieconter", "ohoh!!"); return;}
		//simple stupid way
		//Log.d("draw", "yeeeeeeeah");
		for( int i = 0; i < mBasicTower.length; i++){
			if( mBasicTower[i].getActiveState()){
				
				Projectile p = mBasicTower[i].getProjectile();
				//Log.d("draw", "" + p.getX());
				MoneyCarrier m = null;
				for( int j = 0; j < mEnemies.length; j++){
					//System.out.println("enemy: " + mEnemies[j].getY() + "projectile: " + p.getY());
					if( mEnemies[j].getActiveState() && (int)mEnemies[j].getY() == (int)p.getY() ){
						//System.out.println("Doing Collision Detection!");
						if((int)p.getX() == (int)mEnemies[j].getX()){
							Log.d("draw", "HITTTTT!!!!!" + p.getX() + " --- " + mEnemies[j].getX());
							mEnemies[j].deactivate(); //TODO: deactivate nur zu debug zwecken. später HP abziehen und bei HP = 0 deactivate()
							mEnemieCount--;
						}
						
						/*
						if( m == null) m = mEnemies[j];
						else{
							if( mEnemies[j].getX() < m.getX() ) m = mEnemies[j]; //only first enemie gets hit
						}
						if( m != null ){
							float colpkt = p.collideX( m );
							p.setCollisionPointX( colpkt );
							m.setCollisionPointX( colpkt );
						}
						*/
					}
				}
				
			}
		}	
	}
	

}
