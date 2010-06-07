package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.SoundHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.TextureManager;

public class CarrierRoundTwo extends MoneyCarrier {

	public CarrierRoundTwo(){
		mHp = Definitions.SECOND_ROUND_ENEMIE_HP;
		mStrength = Definitions.SECOND_ROUND_ENEMIE_STRENGTH;
		mRadius = (short)Math.floor( (float)Definitions.FIELD_SEGMENT_LENGTH / 100 *Definitions.SECOND_ROUND_ENEMIE_RADIUS );
		mType = 1;
		mMoney = Definitions.SECOND_ROUND_ENEMIE_MONEY;
		mSpeed = Definitions.SECOND_ROUND_ENEMIE_SPEED;
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture = R.drawable.l12_enemie_lvl1;
		TextureManager.getSingletonObject().add(mTexture);
		TextureManager.getSingletonObject().add(mDyingTextur1);
		TextureManager.getSingletonObject().add(mDyingTextur2);
		TextureManager.getSingletonObject().add(mDyingTextur3);
		TextureManager.getSingletonObject().add(mDyingTextur4);
		mSound = R.raw.l12_enemie2_dying;
		mType = 2;
		mIronToDrop = Definitions.SECOND_ROUND_ENEMIE_IRON;
	}
	
}
