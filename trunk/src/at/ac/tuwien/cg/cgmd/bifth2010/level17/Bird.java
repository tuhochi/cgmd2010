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
public class Bird {
	
	private Vector3 mPosition;
	private float mRotation;
	private float mRadius = 3.0f;
	private Renderable mModel;
	
	/**
	 * Empty Constructor
	 */
	public Bird() {}
	
	/**
	 * Create the bird with defined settings
	 * @param pos The initial position of the bird
	 * @param radius The size of the bird
	 * @param model The model of the bird
	 * @param rotation The rotation of the bird
	 */
	public Bird(Vector3 pos, float radius, Renderable model, float rotation)
	{
		mPosition = pos;
		mRadius = radius;
		mModel = model;
		mRotation = rotation;
	}
	
	/**
	 * Render the bird
	 */
	public void draw() {
		GLManager.getInstance().getTextures().setTexture(R.drawable.l17_vogel);
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		gl.glRotatef(-mRotation - 90.0f, 0, 1.0f, 0);
		mModel.draw(gl);
		gl.glPopMatrix();
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
	 * Getter for the bird position
	 * @return Postion of the bird
	 */
	public Vector3 getPosition() {
		return mPosition;
	}
	
	/**
	 * Setter for the position
	 * @param position The position to set
	 */
	public void setPosition(Vector3 position) {
		this.mPosition = position;
	}

	/**
	 * Getter for the rotation
	 * @return The rotation of the bird
	 */
	public float getRotation() {
		return mRotation;
	}

	/**
	 * Setter for the rotation
	 * @param rotation The new rotation of the bird
	 */
	public void setRotation(float rotation) {
		this.mRotation = rotation;
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
	
	/**
	 * Getter for the model of the bird
	 * @return The model of the bird
	 */
	public Renderable getModel() {
		return mModel;
	}

	/**
	 * Setter for the model of the bird
	 * @param model The new model of the bird
	 */
	public void setModel(Renderable model) {
		this.mModel = model;
	}
}
