package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

/**
 * Better Tower 
 *@see Tower
 *@see AdvancedProjectile
 */
public class AdvancedTower extends Tower {

	/** Constructor of the AdvancedTower, initializes the values like adding needed texture/soundsample, calls the method for initalizing the projectiles */
	public AdvancedTower( ){
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 *  Definitions.ADVANCED_TOWER_RADIUS );
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_bunny3;
		TextureManager.getSingletonObject().add(mTexture);
		TextureManager.getSingletonObject().add(mDyingTextur1);
		TextureManager.getSingletonObject().add(mDyingTextur2);
		TextureManager.getSingletonObject().add(mDyingTextur3);
		TextureManager.getSingletonObject().add(mDyingTextur4);
		mShootingTextur1 = R.drawable.l12_bunny3_shooting1;
		mShootingTextur2 = R.drawable.l12_bunny3_shooting2;
		mShootingTextur3 = R.drawable.l12_bunny3_shooting3;
		TextureManager.getSingletonObject().add(mShootingTextur1);
		TextureManager.getSingletonObject().add(mShootingTextur2);
		TextureManager.getSingletonObject().add(mShootingTextur3);
		mSound = R.raw.l12_advanced_tower_shooting_sound;
		mPrice = Definitions.ADVANCED_TOWER_IRON_NEED;
		mShootingInterval = Definitions.ADVANCED_TOWER_SHOOTING_INTERVALL;
		initProjectiles();
	}
	
	/** initializes an amount of advanced projectiles */	
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new AdvancedProjectile[ Definitions.ADVANCED_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new AdvancedProjectile();	
			}
		}
	}
}
