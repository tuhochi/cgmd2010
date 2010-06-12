package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public class GameMechanics {
	private int mMoney = 0;
	private int mBurnedMoney = 0;
	private short mIron = Definitions.STARTING_IRON;
	private boolean mGameRunning = true;
	private int mSecondsToNextRound = (int)Math.floor( Definitions.GAME_ROUND_WAIT_TIME / 1000);
	private long mLastCountdownCheck = -1;
	private short mRound = 0;
	private long mRoundStartedTime = System.currentTimeMillis();
	private long mTimeGamePaused = -1;
	private int mSelectedTower = 0;
	private long mLastColDetDone = -1;
	private LevelActivity mGameContext = null;
	private int mSpawndenemies = 0;
	private int mKilledenemies = 0;
	
	
	private static GameMechanics mSingleton = null;
	
	private GameMechanics( int startMoney ){
		mMoney = startMoney;	
	}
	
	public void setGameContext( LevelActivity gc ){
		mGameContext = gc;
	}
	
	
	public static GameMechanics getSingleton(){
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
		return mBurnedMoney +=amount;
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
		System.out.println("Game Unpaused at "+System.currentTimeMillis()+" Game Paused at: "+mTimeGamePaused);
		mGameRunning = true;
		mTimeGamePaused = -1;
		mLastCountdownCheck = System.currentTimeMillis();
	}
	
	public void setRoundStartedTime(){
		mLastCountdownCheck = System.currentTimeMillis();
		mSecondsToNextRound = Definitions.GAME_ROUND_WAIT_TIME / 1000;
		mRoundStartedTime = System.currentTimeMillis();
	}
	
	public int  getRemainingWaitTime(){
		if( (System.currentTimeMillis() - mLastCountdownCheck > 1000 ) && mGameRunning  ){
			mSecondsToNextRound--;
			mLastCountdownCheck = System.currentTimeMillis();
		}
		return mSecondsToNextRound;
	}


	public short getRoundNumber() {
		return mRound;
	}
	
	public void nextRound(){
		mRound++;
		if( mRound > Definitions.MAX_ROUND_NUMBER) mRound = Definitions.MAX_ROUND_NUMBER;
		mLastCountdownCheck = System.currentTimeMillis();
		mSecondsToNextRound = (int)Math.floor( Definitions.GAME_ROUND_WAIT_TIME / 1000);
	}


	public int getMoney() {	
		return mMoney;
	}
	
	public int getBurnedMoney() {	
		return mBurnedMoney;
	}
	public long getRoundStartedTime(){
		return mRoundStartedTime;
	}


	public int getSelectedTower() {
		return mSelectedTower;
	}
	
	public long getLastCollDetDoneTime(){
		return mLastColDetDone;
	}


	public void setCollDetTime() {
		mLastColDetDone = System.currentTimeMillis();
	}
	
	public void finishGame(){
		if( mGameContext != null )mGameContext.finish();
		else System.out.println("Could not end game, no pointer to Context");
	}

	public static void destroySingleton() {
		mSingleton = null;
	}

	public void setBasicTowerSelected() {
		mSelectedTower = Definitions.BASIC_TOWER;
	}
	
	public void setAdvancedTowerSelected() {
		mSelectedTower = Definitions.ADVANCED_TOWER;
	}
	
	public void setHyperTowerSelected() {
		mSelectedTower = Definitions.HYPER_TOWER;
	}
	
	public void setFreezeTowerSelected() {
		mSelectedTower = Definitions.FREEZE_TOWER;
	}

	public void addIron(int mIronDropped) {
		mIron += mIronDropped;
	}

	public short getIron() {
		return mIron;
	}

	public void subIron(short price) {
		mIron -= price;
	}

	public CharSequence getPossibleBasicTowerCount() {
		Integer count = (int)Math.floor( Definitions.BASIC_TOWER_IRON_NEED / mIron );
		return count.toString();
	}
	
	public CharSequence getPossibleAdvancedTowerCount() {
		Integer count = (int)Math.floor( Definitions.ADVANCED_TOWER_IRON_NEED / mIron );
		return count.toString();
	}
	
	public CharSequence getPossibleHyperTowerCount() {
		Integer count = (int)Math.floor( Definitions.HYPER_TOWER_IRON_NEED / mIron );
		return count.toString();
	}
	
	public CharSequence getPossibleFreezeTowerCount() {
		Integer count = (int)Math.floor( Definitions.FREEZE_TOWER_IRON_NEED / mIron );
		return count.toString();
	}

	public int getKilledEnemies() {
		return mKilledenemies;
	}

	public int getSpawnedEnemies() {
		return mSpawndenemies;
	}
	
	public void addSpawnedEnemie(){
		mSpawndenemies++;
	}
	
	public void addKilledEnemie(){
		mKilledenemies++;
	}

}
