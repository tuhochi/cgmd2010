package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public class GameMechanics {
	private int mMoney = 0;
	private boolean mGameRunning = true;
	private int mSecondsToNextRound = Definitions.GAME_ROUND_WAIT_TIME; //für ms
	private long mLastCountdownCheck = -1;
	private short mRound = 0;
	private int mRemainingCountdownTime = -1;
	private long mTimeGamePaused = -1;
	
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
		System.out.println("GameMechanics: Game Paused!");
		mGameRunning = false;
		mTimeGamePaused = System.currentTimeMillis();
		mLastCountdownCheck = System.currentTimeMillis();
	}
	
	public void unpause(){
		mGameRunning = true;
		mTimeGamePaused = -1;
		mLastCountdownCheck = System.currentTimeMillis();
	}
	
	public void setRoundStartedTime(){
		mLastCountdownCheck = System.currentTimeMillis();
		mSecondsToNextRound = Definitions.GAME_ROUND_WAIT_TIME;
	}
	
	public int  getRemainingWaitTime(){
		if( (System.currentTimeMillis() - mLastCountdownCheck > 1000 ) && mGameRunning  ) mSecondsToNextRound--;
		return mSecondsToNextRound;
	}


	public short getRoundNumber() {
		return mRound;
	}
	
	public void nextRound(){
		mRound++;
		if( mRound > Definitions.MAX_ROUND_NUMBER) mRound = -1;
		mLastCountdownCheck = System.currentTimeMillis();
		mSecondsToNextRound = Definitions.GAME_ROUND_WAIT_TIME;
	}
	
	public void resetRound(){
		
	}


	public int getMoney() {	
		return mMoney;
	}

}
