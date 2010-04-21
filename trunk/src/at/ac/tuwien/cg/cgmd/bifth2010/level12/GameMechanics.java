package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public class GameMechanics {
	private int mMoney = 0;
	private boolean mGameRunning = true;
	private int mSecondsToNextRound = Definitions.GAME_ROUND_WAIT_TIME * 1000; //für ms
	private long mRoundStartedTime = -1;
	
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
		System.out.println("Adding Money, amount: "+amount+" sum: "+mMoney);
		return mMoney += amount;
	}
	
	public int removeMoney( int amount){
		System.out.println("Removing Money, amount: "+amount+" sum: "+mMoney);
		return mMoney -=amount;
	}
	
	
	public boolean running(){
		return mGameRunning;
	}
	
	public void pause(){
		mGameRunning = false;
	}
	
	public void unpause(){
		mGameRunning = true;
	}
	
	public void setRoundStartedTime(){
		mRoundStartedTime = System.currentTimeMillis();
	}
	
	public int  getRemainingWaitTime(){
		long dt =  System.currentTimeMillis() - this.mRoundStartedTime;
		return (int)( (mSecondsToNextRound - dt)*0.001 );
	}
	
}
