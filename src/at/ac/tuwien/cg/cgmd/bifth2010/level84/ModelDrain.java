package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Model representing a "drain".
 * @author Gerald, Georg
 */
public class ModelDrain extends Model {

	/** Drain type: CLOSED */
	public static final int CLOSED = 0;
	/** Drain type: ROUND */
	public static final int ROUND = 1;
	/** Drain type: OCT */
	public static final int OCT = 2;
	/** Drain type: RECT */
	public static final int RECT = 3;
	/** Drain type: DIAMOND */
	public static final int DIAMOND = 4;
	
	/** Type of the drain */
	private int type;
	
	/** Width of the drain **/
	private float width = 3.1f;
	/** Texture for closed drain **/
	private int textureClosed = R.drawable.l84_drain_closed;
	/** Texture for drain with round hole **/
	private int textureRound = R.drawable.l84_drain_round;
	/** Texture for drain with diamond hole **/
	private int textureDiamond = R.drawable.l84_drain_diamond;
	/** Texture for drain with rectangle hole **/
	private int textureRect = R.drawable.l84_drain_rect;
	/** Texture for drain with octagon hole **/
	private int textureOct = R.drawable.l84_drain_oct;
	
	/** Orientation (angle in degrees) of the drain **/
	private float orientation = 0;
	
	/** Horizontal position of the drain**/
	private float pos;
	
	
	/**
	 * Creates a new drain.
	 * @param drainType style of the drain (closed, round, rect, ...)
	 * @param pos horizontal position 
	 * @param orientation 
	 */
	public ModelDrain(int drainType, float pos, float orientation)
	{
		this.type = drainType;
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
	 * Sets the drain texture.
	 * @param type texture is set dependent on the drain type
	 */
	public void setTexture(int type) { 
		switch (type) {
			case CLOSED:	this.textureResource = textureClosed; break;
			case ROUND:		this.textureResource = textureRound; break;
			case DIAMOND:	this.textureResource = textureDiamond; break;
			case RECT:		this.textureResource = textureRect; break;
			case OCT:		this.textureResource = textureOct; break;
		}
	}
	
	/**
	 * Sets the orientation of the drain.
	 * @param angle drain orientation in degrees
	 */
	public void setOrientationAngle(float angle) {
		this.orientation = angle;
	}
	
	/**
	 * Returns the drain's orientation.
	 * @return orientation of the drain in degrees
	 */
	public float getOrientationAngle() {
		return this.orientation;
	}	
	
	/** Sets position of the drain.
	 * @param pos horizontal position
	 */
	public void setPosition(float pos) {
		this.pos = pos;
	}
	
	/**
	 * Returns the drain's horizontal position.
	 * @return horizontal position
	 */
	public float getPosition() {
		return this.pos;
	}
	
	/**
	 * Returns the drain's style (closed, round, ...).
	 * @return the drain's style
	 */
	public int getStyle() {
		return this.type;
	}
}
