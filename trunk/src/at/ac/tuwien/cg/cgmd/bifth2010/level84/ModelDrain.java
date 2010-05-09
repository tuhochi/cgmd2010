package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Model representing a "Drain".
 * @author Gerald, Georg
  */

public class ModelDrain extends Model {

	/** width of the drain **/
	private float width = 2.0f;
	/** texture for closed drain **/
	private int texture_drain0 = R.drawable.l84_drain_closed;
	/** texture for drain with round hole **/
	private int texture_drain1 = R.drawable.l84_drain_round;
	/** texture for drain with diamond hole **/
	private int texture_drain2 = R.drawable.l84_drain_diamond;
	/** texture for drain with rectangle hole **/
	private int texture_drain3 = R.drawable.l84_drain_rect;
	/** texture for drain with octagon hole **/
	private int texture_drain4 = R.drawable.l84_drain_oct;
	
	private int drainStyle = 0;
	
	/** orientation (angle) of the drain **/
	private float orientation = 0;
	
	/** (horizontal) position of the drain**/
	private float pos;
	
	
	/**
	 * creates a new drain 
	 * @param drainstyle style of the drain (closed, hole type, ...)
	 * @param pos (horizontal) position 
	 * @param orientation 
	 */
	public ModelDrain(int drainstyle, float pos, float orientation)
	{
		this.drainStyle = drainstyle;
		this.setTexture(drainstyle);
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
	 * @param drainstyle texture is set dependent on the drain shape
	 */
	public void setTexture(int drainstyle)
	{
		switch (drainstyle)
		{
			case 0:	this.textureResource = texture_drain0; break;
			case 1:	this.textureResource = texture_drain1; break;
			case 2:	this.textureResource = texture_drain2; break;
			case 3:	this.textureResource = texture_drain3; break;
			case 4:	this.textureResource = texture_drain4; break;
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
		return this.drainStyle;
	}
	
}
