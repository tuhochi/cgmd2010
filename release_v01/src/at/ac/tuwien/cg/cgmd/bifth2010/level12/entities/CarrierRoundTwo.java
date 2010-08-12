package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

/**
 * one enemy
 * @see MoneyCarrier
 */
public class CarrierRoundTwo extends MoneyCarrier {

	/** constructor setting values and adding the needed textures */
	public CarrierRoundTwo(){
		mHp = Definitions.SECOND_ROUND_ENEMIE_HP;
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 *Definitions.SECOND_ROUND_ENEMIE_RADIUS );
		mMoney = Definitions.SECOND_ROUND_ENEMIE_MONEY;
		mSpeed = Definitions.SECOND_ROUND_ENEMIE_SPEED;
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture = R.drawable.l12_enemie_lvl1;
		mTexture1 = R.drawable.l12_enemie_lvl1_1;
		mTexture2 = R.drawable.l12_enemie_lvl1_2;
		mDyingTextur1 = R.drawable.l12_enemie_lvl1_die;
		mDyingTextur2 = R.drawable.l12_enemie_lvl1_die1;
		mDyingTextur3 = R.drawable.l12_enemie_lvl1_die2;
		TextureManager.getSingletonObject().add(mTexture);
		TextureManager.getSingletonObject().add(mTexture1);
		TextureManager.getSingletonObject().add(mTexture2);
		TextureManager.getSingletonObject().add(mDyingTextur1);
		TextureManager.getSingletonObject().add(mDyingTextur2);
		TextureManager.getSingletonObject().add(mDyingTextur3);
		mIronToDrop = Definitions.SECOND_ROUND_ENEMIE_IRON;
		SoundHandler.getSingleton().addResource(mSound);
	}
	
}
