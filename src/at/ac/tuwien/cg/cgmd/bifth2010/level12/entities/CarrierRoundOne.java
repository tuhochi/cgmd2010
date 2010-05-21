package at.ac.tuwien.cg.cgmd.bifth2010.level12.entities;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level12.Definitions;

public class CarrierRoundOne extends MoneyCarrier {
	
	public CarrierRoundOne(){
		mHp = Definitions.FIRST_ROUND_ENEMIE_HP;
		mStrength = Definitions.FIRST_ROUND_ENEMIE_STRENGTH;
		mRadius = (short)Math.floor((float)Definitions.FIELD_SEGMENT_LENGTH / 100  * Definitions.FIRST_ROUND_ENEMIE_RADIUS) ;
		mType = 1;
		mMoney = Definitions.FIRST_ROUND_ENEMIE_MONEY;
		mSpeed = Definitions.FIRST_ROUND_ENEMIE_SPEED;
		mColor[0] = 1.0f;
		mColor[1] = 1.0f;
		mColor[2] = 1.0f;
		mColor[3] = 1.0f;
		mTexture = R.drawable.l12_enemie_lvl0;
		mType = 1;
	}

}
