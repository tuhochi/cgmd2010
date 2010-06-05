package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;


public class IntroBackground extends SpriteContainer {
	private float screenWidth;
	private float screenHeight;
	private int deltaValue;
	private Sprite touchArrows;
	private Sprite leftArrow;
	private Sprite rightArrow;
	private Sprite upArrow;
	
	private static final int DELTA_MAX = 5;
	
	private static final int TOUCH_ARROWS_X = 44;
	private static final int TOUCH_ARROWS_Y = 112;
	private static final int LEFT_ARROW_X = 273;
	private static final int LEFT_ARROW_Y = 143;
	private static final int RIGHT_ARROW_X = 390;
	private static final int RIGHT_ARROW_Y = 144;
	private static final int UP_ARROW_X = 336;
	private static final int UP_ARROW_Y = 97;

	public IntroBackground(Texture texture, float screenWidth, float screenHeight) {
		super(TextureParts.makeIntroBackground(texture));

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.deltaValue = 0;

		/* Center on screen */
		setCenter(getWidth()/2, getHeight()/2);
		setPosition(screenWidth/2, screenHeight/2);
		
		/* Add arrows */
		touchArrows = new Sprite(TextureParts.makeIntroTouchArrows(texture));
		touchArrows.setCenter(0, 0);
		addChildFront(touchArrows);
		
		leftArrow = new Sprite(TextureParts.makeIntroLeftArrow(texture));
		leftArrow.setCenter(0, 0);
		addChildFront(leftArrow);
		
		rightArrow = new Sprite(TextureParts.makeIntroRightArrow(texture));
		rightArrow.setCenter(0, 0);
		addChildFront(rightArrow);
		
		upArrow = new Sprite(TextureParts.makeIntroUpArrow(texture));
		upArrow.setCenter(0, 0);
		addChildFront(upArrow);
		
		step();
	}
	
	public void step() {
		deltaValue += 1;
		
		float val = (float)Math.sin(deltaValue*.1f)*DELTA_MAX;
		
		touchArrows.setPosition(TOUCH_ARROWS_X-getWidth()/2, TOUCH_ARROWS_Y-getHeight()/2+val);
		leftArrow.setPosition(LEFT_ARROW_X-getWidth()/2-val, LEFT_ARROW_Y-getHeight()/2);
		rightArrow.setPosition(RIGHT_ARROW_X-getWidth()/2+val, RIGHT_ARROW_Y-getHeight()/2);
		upArrow.setPosition(UP_ARROW_X-getWidth()/2, UP_ARROW_Y-getHeight()/2-val);		
	}
}
