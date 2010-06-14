package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import java.lang.System;


/**
 * Tower skelet, responsible for drawing the tower and shooting the projectiles.
 * @see GLObject
 */
public abstract class Tower extends GLObject {
	protected short mRadius = -1; /** radius of the quad to draw */
	protected int mShootingInterval = 3000; /** intervall spawning projectiles in ms */
	protected long mTimeLastProjectileShot = -1; 
	protected boolean mHasShot = false;
	protected Projectile[] mProjectiles = null;
	protected int mScreenWidth = 800;
	protected int mSound = R.raw.l12_basic_tower_shooting_sound; /** Soundsample to be played when the tower shoots */
	protected int mTexture = -1; /** texture to draw ib tge quad */
	protected int mDyingTextur1 = R.drawable.l12_enemie_dying1; /** texture to draw when the tower is destroyed */
	protected int mDyingTextur2 = R.drawable.l12_enemie_dying2;
	protected int mDyingTextur3 = R.drawable.l12_enemie_dying3;
	protected int mDyingTextur4 = R.drawable.l12_enemie_dying4;
	protected int mShootingTextur1 = R.drawable.l12_bunny1_shooting1;/** texture to draw when the tower shoots a projectile */
	protected int mShootingTextur2 = R.drawable.l12_bunny1_shooting2;
	protected int mShootingTextur3 = R.drawable.l12_bunny1_shooting3;
	protected long mStartShootingTime = -1; /** time projectile is created */
	protected short mPrice = Definitions.BASIC_TOWER_IRON_NEED; /** price of the tower */
	protected long mStartDyingTime = -1; /** starting time for dying animation texture cycle */
	protected boolean mReadyToRemove = false; /** dying animation has finished, tower ready to remove from the gameworld */
	
	/**
	 * Sets the tower to a specified place on the gamefield and calls the method for VBO initialization
	 * @param xCentr x-coordinate centre of one field quad of the gamefield
	 * @param yCentr y-coordinate centre of one field quad of the gamefield
	 */
	public void setXY( int xCentr, int yCentr ){
		mX = xCentr;
		mY = yCentr;
		this.setActiveState(true);
		mTimeLastProjectileShot = System.currentTimeMillis();
		
		initVBOs();
	}
	
	/**
	 * Creates the OpenGL VBOs
	 */
	public void initVBOs(){
		SoundHandler.getSingleton().addResource(mSound);
		float[] vertices = {
				(mX - mRadius),	(mY - mRadius), 1.0f,
				(mX + mRadius),	(mY - mRadius), 1.0f,
				(mX - mRadius),	(mY + mRadius), 1.0f,
				(mX + mRadius),	(mY + mRadius), 1.0f
		};
		ByteBuffer v = ByteBuffer.allocateDirect( vertices.length * 4 );
		v.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = v.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position(0);
		
		short[] indices = {
				0,1,3,
				0,3,2,
		};
		ByteBuffer i = ByteBuffer.allocateDirect( indices.length * 2 );
		i.order( ByteOrder.nativeOrder() );
		mIndicesBuffer = i.asShortBuffer();
		mIndicesBuffer.put( indices );
		mIndicesBuffer.position(0);	
		mIndicesCounter = indices.length;
		
		float[] colors = { mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3],
				mColor[0], mColor[1], mColor[2], mColor[3]};
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		
		mTimeLastProjectileShot = System.currentTimeMillis();
		for( int c = 0; c < mProjectiles.length; c++){
			mProjectiles[c].initVBOs();
		}
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(mTexturePoints);
		mTextureBuffer.position(0);
	}
	
	/**
	 * sets the length of the gamefield 
	 * @param width length of the gamefield
	 */
	public void setViewPortLength(int width) {
		mScreenWidth = width;
	}
	
	/**
	 * checks if the tower is ready to shoot, if so it spawns a projectile. draws the tower depending on the tower state (shooting, being destroyed)
	 * removes projectiles > screenwidth
	 */
	@Override
	public void draw( GL10 gl ){	
		//pause
		if( GameMechanics.getSingleton().running() == false) mTimeLastProjectileShot = System.currentTimeMillis();
		if( this.getActiveState() == false ) return;
		long mDT =(System.currentTimeMillis() - mTimeLastProjectileShot );
		if( mDT >= mShootingInterval && mStartDyingTime == -1 && !mHasShot ){
			if( mProjectiles != null ){
				boolean found = false;
				for ( int  i = 0; i < mProjectiles.length && !found; i++){
					if( mProjectiles[i].getActiveState() == false ){
						found = true;
						mProjectiles[i].setActiveState( true );
						mProjectiles[i].setXY( this.getFrontX(), mY);
						mTimeLastProjectileShot = System.currentTimeMillis();
						SoundHandler.getSingleton().play(mSound);
						mStartShootingTime = System.currentTimeMillis();
						mHasShot = true;
						break;
					}
				}
			}
		}
		for( int i = 0; i < mProjectiles.length; i++){
			//all active sollen gezeichnet werden
			if(mProjectiles[i].getActiveState() == true) {
				mProjectiles[i].draw(gl);
				//reset projectile when it leaves the screen
				if(  mProjectiles[i].getX() >= mScreenWidth ){
					mProjectiles[i].reset();
				}
				if( mProjectiles[i].toRemove() ) mProjectiles[i].reset();
			}
		}
		if( mStartDyingTime == -1 && mStartShootingTime == -1 )TextureManager.getSingletonObject().setTexture( mTexture );
		else if( mStartShootingTime != -1 ){
			long dyt = System.currentTimeMillis() - mStartShootingTime;
			if( dyt > (0 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mShootingTextur1 );
			if( dyt > (1 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mShootingTextur2 );
			if( dyt > (2 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mShootingTextur3 );
			if( dyt > (3 * Definitions.ANIMTE_CYCLE_TIME) ) {
				TextureManager.getSingletonObject().setTexture( mTexture );
				mStartShootingTime = -1;
			}
		}
		else if( mStartDyingTime != -1 ){
			long dyt = System.currentTimeMillis() - mStartDyingTime;
			if( dyt > (0 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur1 );
			if( dyt > (1 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur2 );
			if( dyt > (2 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur3 );
			if( dyt > (3 * Definitions.ANIMTE_CYCLE_TIME) )  TextureManager.getSingletonObject().setTexture( mDyingTextur4 );
			if( dyt > (4 * Definitions.ANIMTE_CYCLE_TIME) ) {
				TextureManager.getSingletonObject().setTexture( mDyingTextur4 );
				mReadyToRemove = true;
			}
		}
		
		super.draw(gl);
	}
	
	/**
	 *  called from the collision detection, collides projectiles >= enemy position with the given enemy
	 * @param carrier the enemy to collide with
	 */
	public void collideX( MoneyCarrier carrier ){
		for( int i = 0; i < mProjectiles.length; i++){
			if( mProjectiles[i].getActiveState() && (mProjectiles[i].getX() >= carrier.getX()) && mProjectiles[i].isExploding() == false ){
				carrier.hit( mProjectiles[i].getDamage(), mProjectiles[i].getSlow() );
				mProjectiles[i].reset();//die();
				mReadyToRemove = true;
				
			}
		}
	}
	
	
	/**
	 * resets the tower to initial state, which means it is not drawn and spawns no projectiles
	 */
	public void reset(){
		this.setActiveState(false);
		mReadyToRemove = false;
		mHasShot = false;
		this.mTimeLastProjectileShot = System.currentTimeMillis();
		mStartDyingTime = -1;
	}
	
	/**
	 * delivers the highest x-coordinate of the tower quad
	 * @return int the x-coordinate
	 */
	public int getFrontX(){
		return mX + mRadius;
	}
	
	/**
	 * delivers the lowest x-coordinate of the tower quad
	 * @return int the x-coordinate
	 */
	public int getBackX(){
		return mX - mRadius;
	}
	
	/**
	 * delivers the price of the tower
	 * @return short the price
	 */
	public short getPrice(){
		return mPrice;
	}
	
	/**
	 * if an enemie is inside the tower quad the tower dies (starts the dying animation texture cycle)
	 */
	public void die(){
		if( mStartDyingTime == -1 ){
			mStartDyingTime = System.currentTimeMillis();
			GameMechanics.getSingleton().addTowerDestroyed();
		}
	}
	
	/**
	 * is the tower ready to remove? either through finished shooting or through being destroyed from an enemy
	 * @return boolean read to remove or not
	 */
	public boolean toRemove() {
		return mReadyToRemove;
	}
}


