package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.FFModel;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

/**
 * Represents a house in the game
 * @author MaMa
 *
 */
public class ForceField extends LevelElement{

	private float mForceFieldRadius = 10f;
	private float mHeight;
	private Vector2 mTexSpeed = new Vector2(1.0f,-1.0f);
	private float mForceFieldSpeed;
	

	public ForceField(float radius, Vector3 pos, float height, float speed)
	{
		super(new FFModel(radius, height, 4), R.drawable.l17_forcefield, pos, 0f);
		mForceFieldRadius = radius;
		mHeight = height;
		mForceFieldSpeed = speed;
	}
	
	public void update(float elapsedSeconds)
	{
		mRotation += elapsedSeconds * mForceFieldSpeed;
		//mModel.setTexOffset(Vector2.add(mModel.getTexOffset(), Vector2.mult(mTexSpeed, elapsedSeconds)));
	}
	
	/**
	 * Getter for the forcefield radius
	 * @return the radius of the forcefield
	 */
	public float getForceFieldRadius() {
		return mForceFieldRadius;
	}

	/**
	 * Setter for the radius of the forcefield
	 * @param forceFieldRadius the new forcefield radius
	 */
	public void setForceFieldRadius(float forceFieldRadius) {
		this.mForceFieldRadius = forceFieldRadius;
	}

	/**
	 * Getter for the height of the forcefield
	 * @return the height of the forcefield
	 */
	public float getHeight() {
		return mHeight;
	}

	/**
	 * Setter for the hight of the forcefield
	 * @param height the height of the forcefield
	 */
	public void setHeight(float height) {
		this.mHeight = height;
	}
}
