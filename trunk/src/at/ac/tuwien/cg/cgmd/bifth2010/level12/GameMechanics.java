package at.ac.tuwien.cg.cgmd.bifth2010.level12;

/** class which handler various game mechanic elements */
public class GameMechanics {
	private int mMoney = 0; /** money "won" by the player */
	private int mBurnedMoney = 0;  /** money "lost" by the player */
	private short mIron = Definitions.STARTING_IRON; /** Iron possesed */
	private boolean mGameRunning = true; /** is the game running or paused? */
	private int mSecondsToNextRound = (int)Math.floor( Definitions.GAME_START_TIME / 1000); /** wait time between the rounds */
	private long mLastCountdownCheck = -1; /** time when the countdown was last checked */
	private short mRound = 0; /** round number */
	private long mRoundStartedTime = System.currentTimeMillis(); /** time the current round started */
	private int mSelectedTower = 0; /** tower currently selected through the UI */
	private long mLastColDetDone = -1; /** time when the last collision detection is done */
	private LevelActivity mGameContext = null; /** the app context (level activity( */
	private short mSpawndenemies = 0; /** number of enemies spawned */
	private short mKilledenemies = 0; /** number of enemies killed */
	private short mBasicTowerBuilt = 0; /** number of basic tower built */
	private short mAdvancedTowerBuilt = 0; /** number of advanced tower built */
	private short mHyperTowerBuilt = 0; /** number of hyper tower built */
	private short mFreezeTowerBuilt = 0; /** number of freeze tower built */
	private short mTowerDestroyed = 0; /** number of towers destroyed */
	
	
	private static GameMechanics mSingleton = null;
	
	public void addBasicTowerBuilt(){
		this.mBasicTowerBuilt++;
	}
	
	public short getBasicTowerBuilt(){
		return this.mBasicTowerBuilt;
	}
	
	public void addAdvancedTowerBuilt(){
		this.mAdvancedTowerBuilt++;
	}
	
	public short getAdvancedTowerBuilt(){
		return this.mAdvancedTowerBuilt;
	}
	
	public void addHyperTowerBuilt(){
		this.mHyperTowerBuilt++;
	}
	
	public short getHyperTowerBuild(){
		return this.mHyperTowerBuilt;
	}
	
	public void addFreezeTowerBuilt(){
		this.mFreezeTowerBuilt++;
	}
	
	public short getFreezeTowerBuilt(){
		return this.mFreezeTowerBuilt;
	}
	
	public void addTowerDestroyed(){
		this.mTowerDestroyed++;
	}
	
	public short getTowerDestroyed(){
		return this.mTowerDestroyed;
	}
	
	private GameMechanics( int startMoney ){
		mMoney = startMoney;	
	}
	
	/** sets the app context (the Level Acitvity ) */
	public void setGameContext( LevelActivity gc ){
		mGameContext = gc;
	}
	
	public int calculateTotalMoney(){
		int money = Definitions.FIRST_ROUND_ENEMIE_MONEY * (Definitions.FIRST_ROUND_ENEMIE_NUMBER + Definitions.SECOND_ROUND_ENEMIE_NUMBER + Definitions.THIRD_ROUND_ENEMIE_NUMBER + Definitions.FOURTH_ROUND_ENEMIE_NUMBER + Definitions.FIFTH_ROUND_ENEMIE_NUMBER + Definitions.SIXTH_ROUND_ENEMIE_NUMBER);
		mMoney = money;
		return money;
	}
	public static GameMechanics getSingleton(){
		if( mSingleton == null){
			mSingleton = new GameMechanics( Definitions.STARTING_MONEY );
		}
		return mSingleton;
	}
	
	public int addMoney( int amount ){
		return mMoney += amount;
	}
	
	public int removeMoney( int amount){
		return mBurnedMoney +=amount;
	}
	
	public boolean running(){
		return mGameRunning;
	}
	
	public void pause(){
		mGameRunning = false;
		//mTimeGamePaused = System.currentTimeMillis();
		mLastCountdownCheck = System.currentTimeMillis();
	}
	
	public void unpause(){
		mGameRunning = true;
		//mTimeGamePaused = -1;
		mLastCountdownCheck = System.currentTimeMillis();
	}
	
	
	public void setRoundStartedTime(){
		mLastCountdownCheck = System.currentTimeMillis();
		mSecondsToNextRound = Definitions.GAME_ROUND_WAIT_TIME / 1000;
		mRoundStartedTime = System.currentTimeMillis();
	}
	
	/**
	 * delivers the remaining countdown time
	 * @return int the reaming seconds
	 */
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
	
	/** sets the values for the next round started */
	public void nextRound(){
		mSecondsToNextRound = (int)Math.floor( Definitions.GAME_ROUND_WAIT_TIME / 1000);
		mRound++;
	}


	public int getMoney() {	
		return calculateTotalMoney();
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
		
		if( mGameContext != null ) {
			mGameContext.showFinishDialog();
		}
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

	/**
	 * how many basic towers can be built with iron possesd
	 * @return int amount of towers
	 */
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
