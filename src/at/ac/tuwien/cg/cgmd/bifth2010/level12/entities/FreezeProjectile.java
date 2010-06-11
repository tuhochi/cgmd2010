package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

public class FreezeProjectile extends Projectile {

			
			public FreezeProjectile(){
				mSpeed = Definitions.FREEZE_PROJECTILE_SPEED;
				mDmg = Definitions.FREEZE_PROJECTILE_DAMAGE;
				mSlowing = Definitions.FREEZE_PROJECTILE_SLOWING;
				mColor[0] = 1.0f;
				mColor[1] = 1.0f;
				mColor[2] = 1.0f;
				mColor[3] = 1.0f;
				mActive = false;
				mTexture = R.drawable.l12_freeze_projectile;
				TextureManager.getSingletonObject().add(mTexture);
				TextureManager.getSingletonObject().add(mDyingTextur1);
				TextureManager.getSingletonObject().add(mDyingTextur2);
				TextureManager.getSingletonObject().add(mDyingTextur3);
				TextureManager.getSingletonObject().add(mDyingTextur4);
				mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 * Definitions.FREEZE_PROJECTILE_RADIUS );
		}
		
}
