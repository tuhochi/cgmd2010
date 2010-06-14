package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

/**
 * Container that represents the Intro-Screen
 * 
 * @author thp
 *
 */

public class IntroBackground extends SpriteContainer {
	/** the width of the screen */
	private float screenWidth;
	/** the height of the screen */
	private float screenHeight;
	/** used for moving the arrows */
	private int deltaValue;
	/** the moving arrows */
	private Sprite touchArrows;
	/** the moving arrows */
	private Sprite leftArrow;
	/** the moving arrows */
	private Sprite rightArrow;
	/** the moving arrows */
	private Sprite upArrow;

	private static final int DELTA_MAX = 5;

	/** starting x-coordinate */
	private static final int TOUCH_ARROWS_X = 71;
	/** starting y-coordinate */
	private static final int TOUCH_ARROWS_Y = 112;
	/** starting x-coordinate */
	private static final int LEFT_ARROW_X = 296;
	/** starting y-coordinate */
	private static final int LEFT_ARROW_Y = 143;
	/** starting x-coordinate */
	private static final int RIGHT_ARROW_X = 413;
	/** starting y-coordinate */
	private static final int RIGHT_ARROW_Y = 144;
	/** starting x-coordinate */
	private static final int UP_ARROW_X = 359;
	/** starting y-coordinate */
	private static final int UP_ARROW_Y = 97;

	/**
	 * Creates the Intro-Background
	 * @param texture the texture used for creating
	 * @param screenWidth width of the phones screen
	 * @param screenHeight height of the phones screen
	 */
	public IntroBackground(Texture texture, float screenWidth, float screenHeight) {
		super(TextureParts.makeIntroBackground(texture));

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.deltaValue = 0;

		/* Center on screen */
		setCenter(getWidth() / 2, getHeight() / 2);
		setPosition(screenWidth / 2, screenHeight / 2);
		
		/* Scale the image to be full-screen */
		setScale(screenHeight/getHeight());

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

	/**
	 * move the arrows one step further
	 */
	public void step() {
		deltaValue += 1;

		float val = (float) Math.sin(deltaValue * .1f) * DELTA_MAX;

		touchArrows.setPosition(TOUCH_ARROWS_X - getWidth() / 2, TOUCH_ARROWS_Y - getHeight() / 2 + val);
		leftArrow.setPosition(LEFT_ARROW_X - getWidth() / 2 - val, LEFT_ARROW_Y - getHeight() / 2);
		rightArrow.setPosition(RIGHT_ARROW_X - getWidth() / 2 + val, RIGHT_ARROW_Y - getHeight() / 2);
		upArrow.setPosition(UP_ARROW_X - getWidth() / 2, UP_ARROW_Y - getHeight() / 2 - val);
	}
}
