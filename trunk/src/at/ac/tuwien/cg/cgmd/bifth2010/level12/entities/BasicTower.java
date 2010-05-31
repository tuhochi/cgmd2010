package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;



public class BasicTower extends Tower {
	
	public BasicTower( ){
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 *  Definitions.BASIC_TOWER_RADIUS );
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		//mTexture =  R.drawable.l12_basic_tower;
		mTexture =  R.drawable.l12_bunny1;
		mPrice = Definitions.BASIC_TOWER_IRON_NEED;
		mSound = R.raw.l12_basic_tower_shooting_sound;
		mSoundSampleID = SoundHandler.getSingleton().addSound(mSound);
		initProjectiles();	
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
