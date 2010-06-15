package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * skeletal class for the enemies
 * responsible for drawing, moving, creating and destroying
 * @see GLObject
 */
public abstract class MoneyCarrier extends GLObject {
	protected short mRadius = -1; /** radius of the quad to draw */
	private int mMovePos = 0; /** position to move from the initial position where enemy got spawned */
	private long mLastFrametime = -1; /** time when the last frame has been drawn */
	private int mStartPos; /** initial position where this enemy got spawned */
	protected short mHp = 10; /** hitpoints of the enemy */
	protected int mTexture = R.drawable.l12_enemie_lvl0;  /** normal enemy texture */
	protected int mDyingTextur1 = R.drawable.l12_enemie_dying1; /** enemy dying texture used in dying animation texture cycle */
	protected int mDyingTextur2 = R.drawable.l12_enemie_dying2;
	protected int mDyingTextur3 = R.drawable.l12_enemie_dying3;
	protected int mDyingTextur4 = R.drawable.l12_enemie_dying4;
	protected int mSound = R.raw.l00_gold01; /** sound sample played when this enemy dies */
	protected short mSlowed = 0; /** enemy got slowed this much percent by freeze tower */
	protected short mMoney = 10; /** money got added when the enemy reaches the house */
	protected short mSpeed = 5; /** speed on which the enemy moves */
	protected int mIronToDrop = Definitions.FIRST_ROUND_ENEMIE_IRON; /** how much iron drops when enemy dies */
	protected long mStartDyingTime = -1; /** starting time for the dying animation texture cycle */
	boolean mReadyToRemove = false; /** is ready to remove */
	public boolean mIsExploding = false;
	
	/** activates (spawns) the enemy and initializes the values used */
	public void activate(){
		super.setActiveState(true);
		mLastFrametime = System.currentTimeMillis();
		mReadyToRemove = false;
		mStartDyingTime = -1;
		mSlowed = 0;
		//GameMechanics.getSingleton().addMoney( mMoney );
	}

	/** deactivads (removes the enemy from the gamefield), setting default values */
	public void deactivate(){
		super.setActiveState(false);
		mMovePos = 0;
		mLastFrametime = -1;
		mReadyToRemove = true;
	}
	
	/** sets a position on the gamefield of an enemy, x-coordinate is always on a fixed lane, calls the method for creating the opengl VBOs */
	public void setXY(int xCentr, int yCentr ){
		mStartPos = xCentr;	
		mX = xCentr;
		mY = yCentr;
		initVBOs();
	}
	
	/** creates the opengl VBOs, adds the dying soundsamples to the soundpool */
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
		
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(mTexturePoints);
		mTextureBuffer.position(0);
	}
	
	/** 
	 * moves the enemy relativ to the last frame's position further to the left( - x direction ), of starts/moves on in the dying animation texture cycle 
	 */
	@Override
	public void draw(GL10 gl){	
		long ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		if( GameMechanics.getSingleton().running() == false ) dt = 0;
		mLastFrametime = ms;
		int distance = 0;
		if( mStartDyingTime != -1 )distance = 0;
		else if( mSlowed == 0) distance = (int)(mSpeed * dt);
		else distance = (int)(mSpeed * dt * 0.5);//( mSpeed * 0.01 * (100 - mSlowed) * dt);
		mMovePos -= distance;
		//calculate actual position
		mX = (int)(mStartPos + mMovePos);
		gl.glPushMatrix();
		gl.glTranslatef(mMovePos, 0.0f, 0.0f);
		
		if( mStartDyingTime == -1) TextureManager.getSingletonObject().setTexture( mTexture );
		else{
			long dyt = System.currentTimeMillis() - mStartDyingTime;
			//System.out.println("DYT: "+dyt+" cycle time: "+Definitions.DIE_ANIMTE_CYCLE_TIME);
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
		gl.glPopMatrix();
	}
	
	/** 
	 * enemy got hit by projectile, removes enemies HP
	 * @param dmg damage done by the projectile
	 * @param slow slowing done by the projectile
	 */
	public void hit( short dmg, short slow ){
		mHp -= dmg;
		if( mSlowed == 0 && slow != 0 ){
			mSlowed = slow;
			setFrozenColor();
			mHp = (short)(mHp*0.4f);
		}
	}

	/**
	 * delivers the money this enemy is carrying
	 * @return int the money
	 */
	public int getMoney() {
		return mMoney;
	}

	/**
	 * delivers the enemies HP
	 * @return int the HP
	 */
	public int getHP() {
		return mHp;
	}	
	
	/**
	 * delivers the furthest left (smallest) x-coordinate of the enemy quad 
	 * @return int the enemies x-pos on the left 
	 */
	@Override
	public int getX(){
		return (int)(mX - mRadius);
	}
	
	/** starts the dying animation texture cycle */
	public void die(){
		if( mStartDyingTime == -1 ){
			SoundHandler.getSingleton().play(mSound);
			GameMechanics.getSingleton().addIron(mIronToDrop);
			mStartDyingTime = System.currentTimeMillis();
			mIsExploding = true;
		}
	}

	/** enemy is dead so ready to remove from the gamefield */
	public boolean toRemove() {
		return mReadyToRemove;
	}
	
	/** changes the color of the enemy to a blue tint */
	public void setFrozenColor(){
		mColor[0] = 0.0f;
		mColor[1] = 0.0f;
		mColor[2] = 1.0f;
		float[] colors = { mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f,
				mColor[0], mColor[1], mColor[2], 1.0f};
		ByteBuffer cbb = ByteBuffer.allocateDirect( colors.length * 4 );
		cbb.order( ByteOrder.nativeOrder() );
		mColorBuffer = cbb.asFloatBuffer();
		mColorBuffer.put( colors );
		mColorBuffer.position( 0 );
	}

	/**
	 * delivers the furthest left (smallest) x-coordinate of the enemy quad 
	 * @return int the enemies x-pos on the left 
	 */
	public int getFrontX() {
		return getX();
	}
	
	/**
	 * delivers the furthest right (highest) x-coordinate of the enemy quad 
	 * @return int the enemies x-pos on the right 
	 */
	public int getBackX(){
		return mX + mRadius;
	}
}