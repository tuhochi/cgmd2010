package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

/** 
 * strongest tower, no slowing effect
 * @see HyperProjectile
 * @see Tower
 */
public class HyperTower extends Tower {

	/** Constructor of the HyperTower, initializes the values like adding needed texture/soundsample, calls the method for initalizing the projectiles */
	public HyperTower( ){
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 * Definitions.HYPER_TOWER_RADIUS );
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_bunny2;
		TextureManager.getSingletonObject().add(mTexture);
		TextureManager.getSingletonObject().add(mDyingTextur1);
		TextureManager.getSingletonObject().add(mDyingTextur2);
		TextureManager.getSingletonObject().add(mDyingTextur3);
		TextureManager.getSingletonObject().add(mDyingTextur4);
		mShootingTextur1 = R.drawable.l12_bunny2_shooting1;
		mShootingTextur2 = R.drawable.l12_bunny2_shooting2;
		mShootingTextur3 = R.drawable.l12_bunny2_shooting3;
		TextureManager.getSingletonObject().add(mShootingTextur1);
		TextureManager.getSingletonObject().add(mShootingTextur2);
		TextureManager.getSingletonObject().add(mShootingTextur3);
		mPrice = Definitions.HYPER_TOWER_IRON_NEED;
		mSound = R.raw.l12_hyper_tower_shooting_sound;
		SoundHandler.getSingleton().addResource(mSound);
		mShootingInterval = Definitions.HYPER_TOWER_SHOOTING_INTERVALL;
		initProjectiles();
	}
	
	/** initializes an amount of hyper projectiles */	
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new HyperProjectile[ Definitions.HYPER_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new HyperProjectile();	
			}
		}
	}
}
