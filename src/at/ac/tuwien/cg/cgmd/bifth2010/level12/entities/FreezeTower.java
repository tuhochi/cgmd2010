package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

public class FreezeTower extends Tower {

	public FreezeTower( ){
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 *  Definitions.FREEZE_TOWER_RADIUS );
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_bunny4;
		TextureManager.getSingletonObject().add(mTexture);
		TextureManager.getSingletonObject().add(mDyingTextur1);
		TextureManager.getSingletonObject().add(mDyingTextur2);
		TextureManager.getSingletonObject().add(mDyingTextur3);
		TextureManager.getSingletonObject().add(mDyingTextur4);
		mShootingTextur1 = R.drawable.l12_bunny4_shooting1;
		mShootingTextur2 = R.drawable.l12_bunny4_shooting2;
		mShootingTextur3 = R.drawable.l12_bunny4_shooting3;
		TextureManager.getSingletonObject().add(mShootingTextur1);
		TextureManager.getSingletonObject().add(mShootingTextur2);
		TextureManager.getSingletonObject().add(mShootingTextur3);
		mSound = R.raw.l12_freeze_tower_shooting_sound;
		mPrice = Definitions.FREEZE_TOWER_IRON_NEED;
		initProjectiles();
	}
		
		
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new FreezeProjectile[ Definitions.FREEZE_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new FreezeProjectile();	
			}
		}
	}

}
