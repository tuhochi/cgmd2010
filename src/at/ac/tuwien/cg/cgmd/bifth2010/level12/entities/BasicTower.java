package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;



public class BasicTower extends Tower {
	
	
	public BasicTower(float xc, float yc, float rad ) {
		super( xc, yc, rad );
	}
	
	public BasicTower( float radius ){
		super( radius );
	}
	
	
	public void initProjectiles( float speed, int dmg, float interval ){
		mProjectiles = new BasicProjectile[ Definitions.BASIC_PROJECTILE_POOL ];
		mShootingInterval = interval;
		for( int i = 0; i < mProjectiles.length; i++){
			mProjectiles[i] = new BasicProjectile( speed, dmg );
			mProjectiles[i].setActiveState(false);
		}
	}
}
