package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

public class Player {

	private Vector3 mPosition;
	@SuppressWarnings("unused")
	private float mRadius;
	@SuppressWarnings("unused")
	private int mLives;
	@SuppressWarnings("unused")
	private int mMoney;

	public Player (Vector3 position, float radius, int lives, int money)
	{
		mPosition = position;
		mRadius = radius;
		mLives = lives;
		mMoney = money;
	}
	
	public void SetPosition(Vector3 position)
	{
		mPosition = position;
	}
	
	public Vector3 GetPosition()
	{
		return mPosition;
	}
}
