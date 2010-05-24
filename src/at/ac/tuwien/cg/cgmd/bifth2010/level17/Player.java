package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.Date;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

/**
 * A class representing the player
 * @author MaMa
 *
 */
public class Player {

	private Vector3 mPosition;
	private float mRadius = 1.5f;
	private int mLives = 0;
	private int mMoney = 0;
	private long mLastHouseStreak = 0;
	
	private PlayerStateListener mListener;

	/**
	 * Creates a new player
	 * @param position The initial position of the player
	 * @param radius The size of the player
	 * @param lives The initial lives of the player
	 * @param money The initial money of the player
	 */
	public Player (Vector3 position, float radius, int lives, int money)
	{
		mPosition = position;
		mRadius = radius;
		mLives = lives;
		mMoney = money;
	}
	
	/**
	 * Is called when the player hits a house
	 */
	public void hitHouse() {
		mLives--;
		mListener.playerHPChanged(mLives);
	}
	
	/**
	 * is called when the player hits a bird
	 */
	public void hitBird() {
		mMoney += 10;
		mListener.playerMoneyChanged(mMoney, true);
	}
	
	/**
	 * is called when the player is very near to a house
	 */
	public void streakHouse(){
        Date date = new Date();
        long currentTime = date.getTime();
        if (currentTime - mLastHouseStreak > 300)
        {
			mMoney += 3;
			mListener.playerMoneyChanged(mMoney, false);
	        mLastHouseStreak = currentTime;
        }
	}
	
	/**
	 * Adds a new Listener for player updates
	 * @param listener The new listener
	 */
    public void addPlayerStateListener(PlayerStateListener listener) {
        mListener = listener;
    }
	
    /**
     * Setter for the player position
     * @param position The new player position
     */
	public void setPosition(Vector3 position)
	{
		mPosition = position;
	}
	
	/**
	 * Getter for the player position
	 * @return Returns the actual player position
	 */
	public Vector3 getPosition()
	{
		return mPosition;
	}

	/**
	 * Getter for the size of the player
	 * @return Returns the size of the player
	 */
	public float getRadius() {
		return mRadius;
	}

	/**
	 * Setter for the size of the player
	 * @param radius The new size of the player 
	 */
	public void setRadius(float radius) {
		this.mRadius = radius;
	}

	public int getLives() {
		return mLives;
	}

	public void setLives(int lives) {
		this.mLives = lives;
	}

	public int getMoney() {
		return mMoney;
	}

	public void setMoney(int money) {
		this.mMoney = money;
	}
}
