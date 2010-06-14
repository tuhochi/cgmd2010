package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

/**
 * Basic Tower
 *@see Tower
 *@see BasicProjectile
 */
public class BasicTower extends Tower {
	
	/** Constructor of the BasicTower, initializes the values like adding needed texture/soundsample, calls the method for initalizing the projectiles */
	public BasicTower( ){
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 *  Definitions.BASIC_TOWER_RADIUS );
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_bunny1;
		TextureManager.getSingletonObject().add(mTexture);
		TextureManager.getSingletonObject().add(mDyingTextur1);
		TextureManager.getSingletonObject().add(mDyingTextur2);
		TextureManager.getSingletonObject().add(mDyingTextur3);
		TextureManager.getSingletonObject().add(mDyingTextur4);
		mShootingTextur1 = R.drawable.l12_bunny1_shooting1;
		mShootingTextur2 = R.drawable.l12_bunny1_shooting2;
		mShootingTextur3 = R.drawable.l12_bunny1_shooting3;
		TextureManager.getSingletonObject().add(mShootingTextur1);
		TextureManager.getSingletonObject().add(mShootingTextur2);
		TextureManager.getSingletonObject().add(mShootingTextur3);
		mPrice = Definitions.BASIC_TOWER_IRON_NEED;
		mSound = R.raw.l12_basic_tower_shooting_sound;
		mShootingInterval = Definitions.BASIC_TOWER_SHOOTING_INTERVALL;
		initProjectiles();	
	}
	
	/** initializes an amount of basic projectiles */	
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new BasicProjectile[ Definitions.BASIC_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new BasicProjectile();	
			}
		}
	}
	
}
