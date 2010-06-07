package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

public class HyperTower extends Tower {

	public HyperTower( ){
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 * Definitions.HYPER_TOWER_RADIUS );
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_bunny2;
		TextureManager.getSingletonObject().add(mTexture);
		mPrice = Definitions.HYPER_TOWER_IRON_NEED;
		mSound = R.raw.l12_hyper_tower_shooting_sound;
		initProjectiles();
	}
	
	
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new HyperProjectile[ Definitions.HYPER_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new HyperProjectile();	
			}
		}
	}
}
