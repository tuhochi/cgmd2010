package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;

public class AdvancedTower extends Tower {

	public AdvancedTower( ){
		mRadius = Definitions.ADVANCED_TOWER_RADIUS;
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture =  R.drawable.l12_advanced_tower;
		initProjectiles();
	}
	
	
	public void initProjectiles(){
		if( mProjectiles == null){
			mProjectiles = new AdvancedProjectile[ Definitions.ADVANCED_PROJECTILE_POOL ];
			for( int i = 0; i < mProjectiles.length; i++){
				mProjectiles[i] = new AdvancedProjectile();	
			}
		}
	}
}
