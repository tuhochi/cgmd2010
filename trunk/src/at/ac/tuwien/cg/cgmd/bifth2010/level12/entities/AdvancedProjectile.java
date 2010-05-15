package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;

public class AdvancedProjectile extends Projectile {
		
		public AdvancedProjectile(){
			mSpeed = Definitions.ADVANCED_TOWER_PROJECTILE_SPEED;
			mDmg = Definitions.ADVANCED_TOWER_PROJECTILE_DAMAGE;
			mColor[0] = 1.0f;
			mColor[1] = 1.0f;
			mColor[2] = 1.0f;
			mColor[3] = 1.0f;
			mActive = false;
			mTexture = R.drawable.l12_advanced_projectile;
			mRadius = 7;
	}
	
}
