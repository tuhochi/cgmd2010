package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public class GameMechanics {
	private int mMoney = 0;
	
	private static GameMechanics mSingleton = null;
	
	private GameMechanics( int startMoney ){
			mMoney = startMoney;	
	}
	
	
	public static GameMechanics getGameMecanics(){
		if( mSingleton == null){
			mSingleton = new GameMechanics( Definitions.STARTING_MONEY );
		}
		return mSingleton;
	}
	
	public int addMoney( int amount ){
		return mMoney += amount;
	}
	
	public int removeMoney( int amount){
		return mMoney -=amount;
	}
	
}
