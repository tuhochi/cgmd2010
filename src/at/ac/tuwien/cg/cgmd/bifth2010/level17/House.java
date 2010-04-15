package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

/**
 * Represents a house in the game
 * @author MaMa
 *
 */
public class House {

	private int mHouseSize = 0;
	private Vector3 mPosition;
	private Vector3 mSize = new Vector3();
	private Renderable mModel;
	
	/**
	 * Intersect the House with a sphere
	 * @param pos the position of the sphere
	 * @param radius the radius of the sphere
	 * @return True if there is an intersection false when not
	 */
	public boolean intersect(Vector3 pos, float radius) {
		if(Math.abs(pos.x - mPosition.x) < radius + mSize.x / 2.0f &&
		   Math.abs(pos.y - mPosition.y) < radius + mSize.y / 2.0f &&
		   Math.abs(pos.z - mPosition.z) < radius + mSize.z / 2.0f) {
			return true;
		}
		return false;
	}
	
	/**
	 * Draw the house
	 */
	public void draw()
	{
		GLManager.getInstance().getTextures().setTexture(R.drawable.l17_crate);
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		mModel.draw(gl);
		gl.glPopMatrix();
	}
	
	/**
	 * Getter for the house size
	 * @return Returns the house blocksize
	 */
	public int getHouseSize() {
		return mHouseSize;
	}
	
	/**
	 * Setter for the House size
	 * @param houseSize The new blocksize
	 * @param size The new physical size
	 */
	public void setHouseSize(int houseSize, Vector3 size) {
		this.mHouseSize = houseSize;
		mSize = size;
	}
	
	/**
	 * Getter for the house position
	 * @return Returns the position of the house
	 */
	public Vector3 getPosition() {
		return mPosition;
	}
	
	/**
	 * Setter for the house position
	 * @param position The new position of the house
	 */
	public void setPosition(Vector3 position) {
		this.mPosition = position;
	}

	/**
	 * Getter for the physical house size
	 * @return Returns the physical house size
	 */
	public Vector3 getSize() {
		return mSize;
	}

	/**
	 * Setter for the physical house size
	 * @param size The new physical house size
	 */
	public void setSize(Vector3 size) {
		this.mSize = size;
	}

	/**
	 * Getter for the model of the house
	 * @return Returns the model of the house
	 */
	public Renderable getModel() {
		return mModel;
	}

	/**
	 * Setter for the house model
	 * @param model The new model for the house
	 */
	public void setModel(Renderable model) {
		this.mModel = model;
	}
}
