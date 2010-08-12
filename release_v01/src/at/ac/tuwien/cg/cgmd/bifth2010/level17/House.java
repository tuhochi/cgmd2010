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
public class House extends LevelElement{

	private int mHouseSize = 0;
	private Vector3 mSize = new Vector3();
	
	/**
	 * Create the house with defined settings
	 * @param model The model of the house
	 * @param pos The initial position of the house
	 */
	public House(Renderable model, int textureID, Vector3 pos)
	{
		super(model, textureID, pos, 0f);
	}
	
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
}
