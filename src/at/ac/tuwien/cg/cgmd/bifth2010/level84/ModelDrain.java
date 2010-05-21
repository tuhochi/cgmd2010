package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Model representing a "Drain".
 * @author Gerald, Georg
  */

public class ModelDrain extends Model {

	/** width of the drain **/
	private float width = 3.2f;
	/** texture for closed drain **/
	private int textureClosed = R.drawable.l84_drain_closed;
	/** texture for drain with round hole **/
	private int textureRound = R.drawable.l84_drain_round;
	/** texture for drain with diamond hole **/
	private int textureDiamond = R.drawable.l84_drain_diamond;
	/** texture for drain with rectangle hole **/
	private int textureRect = R.drawable.l84_drain_rect;
	/** texture for drain with octagon hole **/
	private int textureOct = R.drawable.l84_drain_oct;
	
	private int drainType = 0;
	
	/** orientation (angle) of the drain **/
	private float orientation = 0;
	
	/** (horizontal) position of the drain**/
	private float pos;
	
	
	/**
	 * creates a new drain 
	 * @param drainType style of the drain (closed, hole type, ...)
	 * @param pos (horizontal) position 
	 * @param orientation 
	 */
	public ModelDrain(int drainType, float pos, float orientation)
	{
		this.drainType = drainType;
		this.setTexture(drainType);
		this.setPosition(pos);
		this.orientation = orientation;
		
		//Adjust the width of Model's quad.
		vertices[0] = vertices[6] = -width/2.0f;
		vertices[3] = vertices[9] = width/2.0f;
		
		//Adjust the height of Model's quad.
		vertices[1] = vertices[4] = -width/2.0f;
		vertices[7] = vertices[10] = width/2.0f;
		
		fillBuffers();
	}
	
	/**
	 * set the drain texture
	 * @param type texture is set dependent on the drain type
	 */
	public void setTexture(int type)
	{
		switch (type)
		{
			case 0:	this.textureResource = textureClosed; break;
			case 1:	this.textureResource = textureRound; break;
			case 2:	this.textureResource = textureDiamond; break;
			case 3:	this.textureResource = textureRect; break;
			case 4:	this.textureResource = textureOct; break;
		}
	}
	
	/**
	 * set the orientation of the drain
	 * @param angle drain orientation
	 */
	public void setOrientationAngle(float angle) {
		this.orientation = angle;
	}
	
	/**
	 * @return orientation of the drain
	 */
	public float getOrientationAngle() {
		return this.orientation;
	}	
	
	/** set position of the drain
	 * @param pos (horizontal) position
	 */
	public void setPosition(float pos) {
		this.pos = pos;
	}
	
	/**
	 * @return (horizontal) position
	 */
	public float getPosition() {
		return this.pos;
	}
	
	public int getDrainStyle()
	{
		return this.drainType;
	}
	
}
