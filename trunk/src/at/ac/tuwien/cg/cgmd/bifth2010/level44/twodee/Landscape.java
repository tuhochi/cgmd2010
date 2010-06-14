package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;

/**
 * The landscape: Meadow, sky, clouds and mountains
 * 
 * This class encapsulates all assets that are used in the landscape of the game
 * (as decorations mostly).
 * 
 * This will also draw and read the PhysicalRabbit object that can be set with
 * setRabbit(). We need to draw the rabbit inside this object, because we want
 * to set it in between the landscape objects (in front of the sky and the
 * mountains, but behind the meadow and the clouds).
 * 
 * @author Thomas Perl
 */
public class Landscape extends SpriteContainer {
	private Vector<ParallaxSprite> parallaxSprites = new Vector<ParallaxSprite>();
	private ParallaxSprite mountains = null;
	private ParallaxSprite hills = null;
	private ParallaxSprite meadow = null;
	private Gradient sky = null;

	private static final int CLOUDS = 5;
	private Vector<Cloud> clouds = new Vector<Cloud>();

	private PhysicalRabbit rabbit = null;

	/**
	 * Set the physical rabbit that should be displayed and interacted with from
	 * this landscape object.
	 * 
	 * @param rabbit
	 *            The rabbit object to be displayed
	 */
	public void setRabbit(PhysicalRabbit rabbit) {
		this.rabbit = rabbit;
	}

	/**
	 * Create a new landscape object that will draw all elements (meadow, hills,
	 * montains, sky, rabbit and clouds, etc..) to the screen.
	 * 
	 * @param texture
	 *            The main texture of MireRabbit containing all landscape assets
	 * @param width
	 *            The width of the screen
	 * @param height
	 *            The height of the screen
	 */
	public Landscape(Texture texture, float width, float height) {
		super(TextureParts.makeSky(texture), width, height);
		setCenter(0, 0);

		sky = new Gradient(width, height);
		/**
		 * The following color values are determined by using the gradient
		 * template from the texture and determining the RGB values of the top
		 * edge and bottom edge of it...
		 **/
		sky.setStartColor(0.4453125f, 0.62109375f, 0.80859375f);
		sky.setStopColor(0.8671875f, 0.90625f, 0.953125f);

		mountains = new ParallaxSprite(TextureParts.makeMountains(texture), width, height);
		hills = new ParallaxSprite(TextureParts.makeHills(texture), width, height);
		meadow = new ParallaxSprite(TextureParts.makeMeadow(texture), width, height);

		/* meadow should be drawn in front of the mountains, hills and rabbit */
		addChildFront(meadow);

		for (int i = 0; i < CLOUDS; i++) {
			Cloud c = new Cloud(texture, i, width, height);
			addChildFront(c);
			clouds.add(c);
		}

		hills.setYOffset(-70);
		mountains.setYOffset(-90);
		meadow.setYOffset(-15);

		parallaxSprites.add(mountains);
		parallaxSprites.add(hills);
		parallaxSprites.add(meadow);
	}

	/**
	 * Overridden from Sprite - draw everything "in front"
	 * 
	 * @param gl The OpenGL ES context
	 */
	@Override
	protected void onAfterDraw(GL10 gl) {
		/* Draw the sky background gradient */
		sky.draw(gl);

		/* draw mountains and hills in the background */
		mountains.draw(gl);
		hills.draw(gl);

		/*
		 * draw the rabbit in front of mountains, hills, but behind meadow +
		 * clouds
		 */
		rabbit.draw(gl);

		/* draw meadow + clouds */
		super.onAfterDraw(gl);
	}

	/**
	 * This will move all assets that are contained in the landscape and should
	 * be called once per loop iteration from the main thread to keep the
	 * landscape alive.
	 */
	public void step() {
		/* Move the clouds around */
		for (Cloud c : clouds) {
			c.step();
		}

		/* Move the parallax sprites depending on the rabbit position */
		Sprite s = rabbit.getSprite();
		float x = s.getX();
		float y = s.getY();
		for (ParallaxSprite p : parallaxSprites) {
			p.update(x, y);
		}
	}

}
