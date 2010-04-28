package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;


public class BasicProjectile extends Projectile {
	
	public BasicProjectile(){
		mSpeed = Definitions.BASIC_TOWER_PROJECTILE_SPEED;
		mDmg = Definitions.BASIC_TOWER_PROJECTILE_DAMAGE;
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.5f;
		mActive = false;
		mTexture = R.drawable.l12_icon;
	}
}
