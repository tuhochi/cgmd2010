package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

public class ParallaxSprite extends Sprite {
	private float screenWidth;
	private float screenHeight;
	private float distance;
	private float yOffset;
	
	public float getYOffset() {
		return yOffset;
	}

	public void setYOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public ParallaxSprite(TexturePart texturePart, float screenWidth, float screenHeight) {
		super(texturePart);
		setCenter(0, getHeight());
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.distance = getWidth()/screenWidth;
		this.yOffset = 0.f;
		update(screenWidth/2, screenHeight/2);
	}
	
	public void update(float x, float y) {
		float xpos = (x/screenWidth);
		float ypos = (y/screenHeight);
		setPosition(xpos*(screenWidth-getWidth()), screenHeight+yOffset+screenHeight*.3f*distance*(1-ypos));
	}
}
