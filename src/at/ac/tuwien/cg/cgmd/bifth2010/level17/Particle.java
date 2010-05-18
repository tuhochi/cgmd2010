package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.Date;
import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

/**
 * A class that represents a single particle
 * @author Johannes Scharl
 *
 */
public class Particle {
	private float mAge;
	private float  mLifeSpan;
	private Vector3 mRelativePosition;
	private Vector3 mDirection;
	private float mRotation;
	private float mRotationSpeed;
	private Vector3 mRotationAxis;
	
	/**
	 * Constructs a new particle object with random parameters
	 * @param r the random object used to create random values
	 */
	public Particle(Random r)
	{
		mAge = 0f;
		mLifeSpan = r.nextFloat() * 3f;
		mRelativePosition = new Vector3(0f, -0.5f, 0f);
		mDirection = Vector3.mult(getRandomNormalizedVector(true, r), 0.02f);
		mRotation = r.nextFloat();
		mRotationSpeed = r.nextFloat() * 0.02f;
		mRotationAxis = getRandomNormalizedVector(false, r);
	}
	
	/**
	 * Creates a random, normalized Vector3
	 * @param makeYzero whether the y component of the vector should be zero
	 * @param r the Random object used to create random values
	 * @return the random vector
	 */
	private Vector3 getRandomNormalizedVector(boolean makeYzero, Random r)
	{
		Vector3 ret = new Vector3();
		ret.x = r.nextFloat();
		ret.x -= 0.5f;
		ret.z = r.nextFloat();
		ret.z -= 0.5f;
		if (!makeYzero)
		{
			ret.y = r.nextFloat();
			ret.y -= 0.5f;
		}
		ret.normalize();
		return ret;
	}
	
	/**
	 * Updates age, position and rotation of a particle
	 * @param elapsedSeconds
	 */
	public void update(float elapsedSeconds)
	{
		mAge += elapsedSeconds;
		Vector3 delta = Vector3.mult(mDirection, elapsedSeconds);
		mRelativePosition = Vector3.add(mRelativePosition, delta);
		mRotation += mRotationSpeed * elapsedSeconds;
	}
	
	/**
	 * Returns if the particle is still active
	 * @return If the particle is still active
	 */
	public boolean isActive()
	{
		return (mAge < mLifeSpan);
	}
	
	/**
	 * Draws a particle at the current position with current rotation
	 * @param referencePosition the absolute position of the particle system
	 * @param renderable the renderable that should be drawn
	 */
	public void draw(Vector3 referencePosition, Renderable renderable)
	{
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(Vector3.add(referencePosition, mRelativePosition));
		gl.glRotatef(-mRotation - 90.0f, mRotationAxis.x, mRotationAxis.y, mRotationAxis.z);
		renderable.draw(gl);
		gl.glPopMatrix();
	}
}
