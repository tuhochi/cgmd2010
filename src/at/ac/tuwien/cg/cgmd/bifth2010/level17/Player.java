package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

public class Player {

	private Vector3 mPosition;
	private float mRadius = 1.5f;
	private int mLives = 0;
	@SuppressWarnings("unused")
	private int mMoney;
	
	private PlayerStateListener mListener;

	public Player (Vector3 position, float radius, int lives, int money)
	{
		mPosition = position;
		mRadius = radius;
		mLives = lives;
		mMoney = money;
	}
	
	public void hit() {
		mLives++;
		mListener.playerHPChanged(mLives);
	}
	
    public void addPlayerStateListener(PlayerStateListener listener) {
        mListener = listener;
    }
	
	public void setPosition(Vector3 position)
	{
		mPosition = position;
	}
	
	public Vector3 getPosition()
	{
		return mPosition;
	}

	public float getRadius() {
		return mRadius;
	}

	public void setRadius(float radius) {
		this.mRadius = radius;
	}
}
