package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;

public class HyperProjectile extends Projectile {

	public HyperProjectile(){
		mSpeed = Definitions.HYPER_PROJECTILE_SPEED;
		mDmg = Definitions.HYPER_PROJECTILE_DAMAGE;
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mActive = false;
		mTexture = R.drawable.l12_hyper_projectile;
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 * Definitions.HYPER_PROJECTILE_RADIUS);
	}
	
}
