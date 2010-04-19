package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

/**
 * The ParallaxSprite class is a special Sprite that has the ability to pan
 * along the X and Y axis of the screen to achieve a parallax effect without
 * the need for sophisticated 3D effects. This fits well with the comic style
 * of MireRabbit.
 * 
 * More information about the effect:
 *   http://en.wikipedia.org/wiki/Parallax
 * 
 * @author thp
 */
public class ParallaxSprite extends Sprite {
	private float screenWidth;
	private float screenHeight;
	private float distance;
	private float yOffset;

	/**
	 * 
	 * @param texturePart A part of an existing texture used to draw this sprite
	 * @param screenWidth The width of the screen, used for parallax-style panning
	 * @param screenHeight The height of the screen - again, used for parallax-style panning
	 */
	public ParallaxSprite(TexturePart texturePart, float screenWidth, float screenHeight) {
		super(texturePart);
		setCenter(0, getHeight());
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.distance = getWidth()/screenWidth;
		this.yOffset = 0.f;
		update(screenWidth/2, screenHeight/2);
	}
	
	/**
	 * Update the position of this sprite based on a given object's position.
	 * In the case of MireRabbit, this should be the rabbit's position.
	 * 
	 * @param x The x-coordinate of the focused object
	 * @param y The y-coordinate of the focused object
	 */
	public void update(float x, float y) {
		float xpos = (x/screenWidth);
		float ypos = (y/screenHeight);
		setPosition(xpos*(screenWidth-getWidth()), screenHeight+yOffset+screenHeight*.3f*distance*(1-ypos));
	}
	
	public float getYOffset() {
		return yOffset;
	}

	/**
	 * Set the vertical correction value. This value will be used to move
	 * the ParallaxSprite vertically independent of other factors.
	 *
	 * @param yOffset The amount of pixels to move the sprite down
	 */
	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}
}
