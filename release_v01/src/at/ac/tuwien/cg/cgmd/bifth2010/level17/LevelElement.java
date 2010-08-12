package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

/**
 * Super class for all Elements in the level (Houses, Birds, Forcefield, etc...)
 * @author Johannes Scharl
 *
 */
public class LevelElement {
	protected int mTextureID;
	protected Vector3 mPosition;
	protected float mRotation;
	protected Renderable mModel;
	protected Vector3 mRotationAxis = new Vector3(0f, 1f, 0f);
	
	/**
	 * Create the element with defined settings
	 * @param pos The initial position of the element
	 * @param model The model of the element
	 * @param rotation The rotation of the element
	 */
	public LevelElement(Renderable model, int textureID, Vector3 pos, float rotation)
	{
		mTextureID = textureID;
		mPosition = pos;
		mModel = model;
		mRotation = rotation;
	}

	
	/**
	 * Sets the rotation axis the rotation is calculated around
	 * @param axis The new rotation axis
	 */
	protected void setRotationAxis(Vector3 axis)
	{
		mRotationAxis = axis;
	}
	
	/**
	 * Render the element
	 */
	public void draw() {
		GLManager.getInstance().getTextures().setTexture(mTextureID);
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		if (mRotation != 0f)
			gl.glRotatef(-mRotation - 90.0f, mRotationAxis.x, mRotationAxis.y, mRotationAxis.z);
		mModel.draw(gl);
		gl.glPopMatrix();
	}
	
	/**
	 * Getter for the position
	 * @return Position 
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
	 * @return The rotation
	 */
	public float getRotation() {
		return mRotation;
	}

	/**
	 * Setter for the rotation
	 * @param rotation The new rotation
	 */
	public void setRotation(float rotation) {
		this.mRotation = rotation;
	}
	
	
	/**
	 * Getter for the model
	 * @return The model
	 */
	public Renderable getModel() {
		return mModel;
	}

	/**
	 * Setter for the model
	 * @param model The new model
	 */
	public void setModel(Renderable model) {
		this.mModel = model;
	}
}
