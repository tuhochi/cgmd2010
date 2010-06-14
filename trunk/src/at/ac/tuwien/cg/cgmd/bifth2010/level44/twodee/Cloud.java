package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Random;

/**
 * A Decorative Cloud
 * 
 * A simple cloud sprite that will position itself at the top of the game
 * environment and will move through the screen. This object is self-contained,
 * will automatically choose a different texture part based on its ID and will
 * move itself if step() is called for every frame.
 * 
 * Not to be confused with Cloud Computing.
 * 
 * @author Thomas Perl
 */
public class Cloud extends Sprite {
	/** the id of the cloud */
	private int id;
	/** the width of the screen */
	private float screenWidth;
	/** the height of the screen */
	private float screenHeight;

	/**
	 * Create a new cloud
	 * 
	 * @param texture
	 *            The main texture for MireRabbit that contains clouds
	 * @param id
	 *            A numeric id (starting from 0) for this cloud, used for
	 *            texture selection
	 * @param screenWidth
	 *            The width of the screen this cloud will be displayed on
	 * @param screenHeight
	 *            The height of the screen this cloud will be displayed on
	 */
	public Cloud(Texture texture, int id, float screenWidth, float screenHeight) {
		super(TextureParts.makeCloud(texture, id));

		this.id = id;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		Random rand = new Random();
		setPosition(rand.nextInt((int) screenWidth), rand.nextInt((int) (screenHeight / 2)));
	}

	/**
	 * Execute a timestep for this cloud.
	 * 
	 * This will take care of moving the cloud according to its rules in the
	 * game world and should be called from the game thread for all clouds once
	 * per loop iteration.
	 */
	public void step() {
		float x = getX() + (1 + .5f * id) * .2f;

		if (x > screenWidth + getWidth()) {
			x = -getWidth();
		}

		setPosition(x, getY());
	}

}
