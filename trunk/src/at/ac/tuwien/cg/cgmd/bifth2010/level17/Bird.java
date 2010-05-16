package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

/**
 * Represents a Bird in the game
 * @author MaMa
 *
 */
public class Bird extends LevelElement{
	
	private float mRadius = 3.0f;
	
	/**
	 * Create the bird with defined settings
	 * @param model The model of the bird
	 * @param pos The initial position of the bird
	 * @param rotation The rotation of the bird
	 */
	public Bird(Renderable model, Vector3 pos, float rotation)
	{
		super(model, R.drawable.l17_vogel, pos, rotation);
	}
	
	/**
	 * Update the bird
	 * @param elapsedSeconds The elapsed time since the last call to update
	 */
	public void update(float elapsedSeconds) {
		float x = (float)Math.cos(mRotation * Math.PI / 180.0f);
		float z = (float)Math.sin(mRotation * Math.PI / 180.0f);

		mPosition = Vector3.add(mPosition, Vector3.mult(new Vector3(x,0,z), elapsedSeconds * 5.0f));
	}
	
	/**
	 * Intersect the bird with a sphere
	 * @param pos Position of the sphere
	 * @param radius Radius of the sphere
	 * @return True if there is an intersection false when not
	 */
	public boolean intersect(Vector3 pos, float radius) {
		float dist = Vector3.diff(mPosition, pos).length();
		if(dist < radius + mRadius) {
			return true;
		}
		return false;
	}
	
	/**
	 * Getter for the size of the bird
	 * @return Returns the radius of the bird
	 */
	public float getRadius() {
		return mRadius;
	}

	/**
	 * Setter for the size of the bird
	 * @param radius The new radius of the bird
	 */
	public void setRadius(float radius) {
		this.mRadius = radius;
	}
}
