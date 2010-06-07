package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Projectile extends GLObject{
	
	private long mLastFrametime = -1;
	private int mXTranslate = 0;
	protected short mRadius = 4;
	protected short mSpeed = 5;
	protected short mDmg = 10;
	protected int mTexture = -1;
	long ms;
	double dt;
	int distance;
	
	public float getSpeed(){
		return mSpeed;
	}
	
	public void setXY( int x, int y){
		mX = x;
		mY = y;
		initVBOs();
	}
	
	
	public void initVBOs(){	
		mLastFrametime = System.currentTimeMillis();
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
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(mTexturePoints);
		mTextureBuffer.position(0);
	}
	
	@Override
	public void draw( GL10 gl ){
		TextureManager.getSingletonObject().setTexture( mTexture );
		ms = System.currentTimeMillis();
		dt = (ms - mLastFrametime) * 0.001;
		//pause
		if( GameMechanics.getSingleton().running() == false ) dt = 0;		
		mLastFrametime = ms;
		distance = (int)(this.getSpeed() * dt);
		mXTranslate += distance;		
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef( mXTranslate, 0.0f, 0.0f);
		super.draw(gl);
		gl.glPopMatrix();	
	}
	
	@Override
	public int getX(){
		return mXTranslate + mX + mRadius; //returns the bullet real position because it gets translated and not moved
	}


	public short getDamage() {
		return mDmg;
	}
	
	public void reset(){
		mXTranslate = 0;
		this.setActiveState(false);
	}
	
}
