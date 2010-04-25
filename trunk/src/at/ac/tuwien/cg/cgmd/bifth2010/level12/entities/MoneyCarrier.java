package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.GameMechanics;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


//TODO: wenn getroffen setActivestate false, und werte für hp dmg usw resetten
public class MoneyCarrier extends GLObject {
	private float mRadius = -1;
	private float mMovePos = 0.0f;
	private long mLastFrametime, ms = -1;
	private float mStartPos;
	private short mHp = 1;
	private short mStrength = 1; //how much damage it can do
	private int mType = 0; //zombie type
	int tex;
	
	private int mMoney = 10;
	
	private short mDamageAtCollisionPoint = -1;
	
	
	public MoneyCarrier(){
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		//mActive = false;
	}
	
	public void activate(){
		super.setActiveState(true);
	}

	public void deactivate(){
		super.setActiveState(false);
	}
	
	public void init(float xCentr, float yCentr, int type){
		//super.setActiveState(true);
		mLastFrametime = System.currentTimeMillis();//TODO: später in activate() methode verschieben
		mStartPos = xCentr;
		
		if(type == 0){
			mRadius = 5;  //TODO: only placeholder values
			mHp = 10;
			mSpeed = 5;
			mStrength = 5;
			mColor[0] = 0.0f;
			mColor[1] = 0.0f;
			mColor[2] = 0.0f;
			
			tex = R.drawable.l12_icon;
		}
		else if(type == 1){
			mRadius = 5; 
			mHp = 10;
			mSpeed = 5;
			mStrength = 5;
			mColor[0] = 1.0f;
			mColor[1] = 0.0f;
			mColor[2] = 0.0f;
			
			tex = R.drawable.l12_icon;
		}
		else if(type == 2){
			mRadius = 5;
			mHp = 10;
			mSpeed = 10;
			mStrength = 5;
			mColor[0] = 1.0f;
			mColor[1] = 1.0f;
			mColor[2] = 0.0f;
			
			tex = R.drawable.l12_icon;
		}
		
		mX = xCentr;
		mY = yCentr;
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
		System.out.println("Vertices.length: "+vertices.length+" Indices.length: "+indices.length);
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
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(texture);
		mTextureBuffer.position(0);
	}
	
	@Override
	public void draw(GL10 gl){
		
		TextureManager.getSingletonObject().add(R.drawable.l12_icon);
		ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		mLastFrametime = ms;
		double distance = mSpeed * dt;
		mMovePos -= distance;
		//calculate actual position
		mX = mStartPos + mMovePos;
		
		if( mX <= 1.0f) {
			this.deactivate();
			GameMechanics.getGameMecanics().addMoney( mMoney );
		}
		gl.glPushMatrix();
		gl.glTranslatef(mMovePos, 0.0f, 0.0f);
		
		TextureManager.getSingletonObject().setTexture(tex);
		super.draw(gl);
		gl.glPopMatrix();
	}
	
	
	public void hit( short dmg ){
		mHp -= dmg;
		if( mHp <= 0 ) this.deactivate();
		System.out.println("Carrier got hit, damage done: "+dmg+" raminint hp: "+mHp);
	}
	
	public float getX(){
		/*ms = System.currentTimeMillis();
		double dt = (ms - mLastFrametime) * 0.001;
		mLastFrametime = ms;
		double distance = mSpeed * dt;
		mMovePos -= distance;
		//calculate actual position
		mX = mStartPos + mMovePos;
		*/
		return mX;
	}	
}