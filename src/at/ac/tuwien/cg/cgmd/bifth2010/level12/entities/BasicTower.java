package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;



public class BasicTower extends Tower {
	
	
	public BasicTower(float xc, float yc, float rad ) {
		super( xc, yc, rad );
	}
	
	public BasicTower( float radius ){
		super( radius );
	}
	
	
	public void initProjectiles( float speed, short dmg, float interval ){
		mProjectiles = new BasicProjectile[ Definitions.BASIC_PROJECTILE_POOL ];
		mShootingInterval = interval;
		for( int i = 0; i < mProjectiles.length; i++){
			mProjectiles[i] = new BasicProjectile( speed, dmg );
		}
	}
	
	public void draw(GL10 gl){
		TextureManager.getSingletonObject().setTexture(R.drawable.l12_icon);
		super.draw(gl);
	}
}
