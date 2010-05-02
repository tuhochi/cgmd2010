package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;



public class BasicTower extends Tower {
	
	public BasicTower( ){
		mRadius = Definitions.BASIC_TOWER_RADIUS;
		mColor[0] = 0.5f;
		mColor[1] = 0.5f;
		mColor[2] = 0.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_icon;
		ByteBuffer tbb = ByteBuffer.allocateDirect(mTexturePoints.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		mTextureBuffer = tbb.asFloatBuffer();
		mTextureBuffer.put(mTexturePoints);
		mTextureBuffer.position(0);
		mShootingInterval = Definitions.BASIC_TOWER_SHOOTING_INTERVALL;
		initProjectiles();
		mTexture = R.drawable.l12_basic_tower;
	}
	
	
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new BasicProjectile[ Definitions.BASIC_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new BasicProjectile();	
			}
		}
	}
	
}
