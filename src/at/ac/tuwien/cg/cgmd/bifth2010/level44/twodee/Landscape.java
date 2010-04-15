package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.physics.PhysicalRabbit;

public class Landscape extends SpriteContainer {
	private Vector<ParallaxSprite> parallaxSprites = new Vector<ParallaxSprite>();
	private ParallaxSprite mountains = null;
	private ParallaxSprite hills = null;
	private ParallaxSprite meadow = null;
	private Gradient sky = null;

	private static final int CLOUDS = 3;
	private Vector<Cloud> clouds = new Vector<Cloud>();

	private PhysicalRabbit rabbit = null;

	public void setRabbit(PhysicalRabbit rabbit) {
		this.rabbit = rabbit;
	}

	public Landscape(Texture texture, float width, float height) {
		super(TextureParts.makeSky(texture), width, height);
		setCenter(0, 0);
		
		sky = new Gradient(width, height);
		/**
		 * The following color values are determined by using the
		 * gradient template from the texture and determining the
		 * RGB values of the top edge and bottom edge of it...
		 **/
		sky.setStartColor(0.4453125f, 0.62109375f, 0.80859375f);
		sky.setStopColor(0.8671875f, 0.90625f, 0.953125f);
		
		mountains = new ParallaxSprite(TextureParts.makeMountains(texture), width, height);
		hills = new ParallaxSprite(TextureParts.makeHills(texture), width, height);
		meadow = new ParallaxSprite(TextureParts.makeMeadow(texture), width, height);
		
		/* meadow should be drawn in front of the mountains, hills and rabbit */
		addChildFront(meadow);
		
		for (int i=0; i<CLOUDS; i++) {
			Cloud c = new Cloud(texture, i, width, height);
			addChildFront(c);
			clouds.add(c);
		}
		
		hills.setYOffset(-50);
		mountains.setYOffset(-40);
		meadow.setYOffset(-35);
		
		parallaxSprites.add(mountains);
		parallaxSprites.add(hills);
		parallaxSprites.add(meadow);
	}

	@Override
	protected void onAfterDraw(GL10 gl) {
		/* Draw the sky background gradient */
		sky.draw(gl);

		/* draw mountains and hills in the background */
		mountains.draw(gl);
		hills.draw(gl);
		
		/* draw the rabbit in front of mountains, hills, but behind meadow + clouds */
		rabbit.draw(gl);
		
		/* draw meadow + clouds */
		super.onAfterDraw(gl);
	}	
	
	public void step() {
		/* Move the clouds around */
		for (Cloud c: clouds) {
			c.step();
		}
		
		/* Move the parallax sprites depending on the rabbit position */
		Sprite s = rabbit.getSprite();
		float x = s.getX();
		float y = s.getY();
		for (ParallaxSprite p: parallaxSprites) {
			p.update(x, y);
		}
	}

}
