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
public class ForceField {

	private float mForceFieldRadius = 10f;
	private Vector3 mPosition;
	private float mHeight;
	private FFModel mModel;
	private Vector2 mTexSpeed = new Vector2(1.0f,-1.0f);
	

	public ForceField(float radius, Vector3 pos, float height, Vector2 texSpeed)
	{
		mForceFieldRadius = radius;
		mPosition = pos;
		mHeight = height;
		mTexSpeed = texSpeed;
		mModel = new FFModel(mForceFieldRadius, height, 4);
	}
	
	/**
	 * Draw the forcefield
	 */
	public void draw()
	{
		GLManager.getInstance().getTextures().setTexture(R.drawable.l17_forcefield);
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		mModel.draw(gl);
		gl.glPopMatrix();
	}
	
	public void update(float elapsedSeconds)
	{
		mModel.setTexOffset(Vector2.add(mModel.getTexOffset(), Vector2.mult(mTexSpeed, elapsedSeconds)));
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
	 * Getter for the forcefield position
	 * @return Returns the position of the forcefield
	 */
	public Vector3 getPosition() {
		return mPosition;
	}
	
	/**
	 * Setter for the forcefield position
	 * @param position The new position of the forcefield
	 */
	public void setPosition(Vector3 position) {
		this.mPosition = position;
	}

	/**
	 * Getter fpr the hight of the forcefield
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

	/**
	 * Getter for the model of the forcefield
	 * @return Returns the model of the forcefield
	 */
	public Renderable getModel() {
		return mModel;
	}

	/**
	 * Setter for the house forcefield
	 * @param model The new model for the forcefield
	 */
	public void setModel(FFModel model) {
		this.mModel = model;
	}
}
