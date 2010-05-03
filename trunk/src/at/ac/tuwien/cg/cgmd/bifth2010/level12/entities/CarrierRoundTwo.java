package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;

public class CarrierRoundTwo extends MoneyCarrier {

	public CarrierRoundTwo(){
		mHp = Definitions.SECOND_ROUND_ENEMIE_HP;
		mStrength = Definitions.SECOND_ROUND_ENEMIE_STRENGTH;
		mRadius = Definitions.SECOND_ROUND_ENEMIE_RADIUS;
		mType = 1;
		mMoney = Definitions.SECOND_ROUND_ENEMIE_MONEY;
		mSpeed = Definitions.SECOND_ROUND_ENEMIE_SPEED;
		mColor[0] = 1.0f;
		mColor[1] = 0.0f;
		mColor[2] = 0.0f;
		mColor[3] = 1.0f;
		mTexture = R.drawable.l12_enemie_lvl1;
		mType = 2;
	}
	
}
