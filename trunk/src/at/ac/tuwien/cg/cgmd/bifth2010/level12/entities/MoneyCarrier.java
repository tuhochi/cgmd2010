package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


//TODO: wenn getroffen setActivestate false, und werte für hp dmg usw resetten
public class MoneyCarrier extends GLObject {
	private float mRadius = -1;
	private float mMovePos = 0.0f;
	private double mLastFrametime, ms = -1;
	private float mStartPos, dt, distance = -1;

	private short mHp = 1;
	private short mStrength = 1; //how much damage it can do
	private int mType = 0; //zombie type
	
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
			mHp = 100;
			mSpeed = 5;
			mStrength = 5;
			mColor[0] = 0.0f;
			mColor[1] = 0.0f;
			mColor[2] = 0.0f;
		}
		else if(type == 1){
			mRadius = 5; 
			mHp = 100;
			mSpeed = 5;
			mStrength = 5;
			mColor[0] = 1.0f;
			mColor[1] = 0.0f;
			mColor[2] = 0.0f;
		}
		else if(type == 2){
			mRadius = 5;
			mHp = 100;
			mSpeed = 10;
			mStrength = 5;
			mColor[0] = 1.0f;
			mColor[1] = 1.0f;
			mColor[2] = 0.0f;
		}
		
		mX = xCentr;
		mY = yCentr;
		float[] vertices = {
				(mX - mRadius),	(mY - mRadius), 1.0f,
				(mX + mRadius),	(mY - mRadius), 1.0f,
				(mX + mRadius),	(mY + mRadius), 1.0f,
				(mX - mRadius),	(mY + mRadius), 1.0f
		};
		ByteBuffer v = ByteBuffer.allocateDirect( vertices.length * 4 );
		v.order( ByteOrder.nativeOrder() );
		mVerticesBuffer = v.asFloatBuffer();
		mVerticesBuffer.put( vertices );
		mVerticesBuffer.position(0);
		
		short[] indices = {
				0,	1,	2,
				0,	2,	3
		};
		System.out.println("Vertices.length: "+vertices.length+" Indices.length: "+indices.length);
		ByteBuffer i = ByteBuffer.allocateDirect( indices.length * 2 );
		i.order( ByteOrder.nativeOrder() );
		mIndicesBuffer = i.asShortBuffer();
		mIndicesBuffer.put( indices );
		mIndicesBuffer.position(0);	
		mIndicesCounter = indices.length;
	}
	
	@Override
	public void draw(GL10 gl){
		ms = System.currentTimeMillis();
		dt = (float)((ms - mLastFrametime) * 0.001);
		mLastFrametime = ms;
		distance = (float) mSpeed * dt;
		mMovePos -= distance;
		gl.glPushMatrix();
		gl.glTranslatef(mMovePos, 0.0f, 0.0f);
		super.draw(gl);
		gl.glPopMatrix();
		
		//calculate actual position
		mX = mStartPos + mMovePos;
		//Log.d("CARRIER", "x = " + mX);
		
	}
	
	
	public void setDamageAtCollisionPoint( short dmg ){
		mDamageAtCollisionPoint = dmg;
	}
	
}