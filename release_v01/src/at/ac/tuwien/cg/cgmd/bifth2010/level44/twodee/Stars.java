package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

/**
 * An "exploding" circle of small stars
 * 
 * When our rabbit head gets shot, that hurts. And of course,
 * apart from the vibrating feeling we get, there has to be
 * some kind of visual representation of that hurt: Stars!
 * 
 * @author Thomas Perl
 *
 */

public class Stars {
	/** sprite for a red star */
	private Sprite redStar;
	/** sprite for a yellow star */
	private Sprite yellowStar;
	/** the current angle of the "scene" */
	private float angle;
	/** the current scale-factor */
	private float scale;
	/** are the stars visible? */
	private boolean visible;
	/** The amount of stars displayed */
	private static final int STARS = 18;
	/** Maximum scaling value after which stars disappear */
	private static final float MAXSCALE = 10f;

	/**
	 * Creates the stars from the big texture
	 * @param texture the texture used for creating the red and yellow stars
	 */
	public Stars(Texture texture) {
		this.redStar = new Sprite(TextureParts.makeRedStar(texture));
		redStar.setPosition(100, 0);
		this.yellowStar = new Sprite(TextureParts.makeYellowStar(texture));
		yellowStar.setPosition(150, 0);
		this.angle = 0.f;
		this.scale = 1.f;
		this.visible = false;
	}

	/**
	 * draws the stars on screen if they are visible
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {
		if (!visible) {
			return;
		}

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();

		// gl.glTranslatef(x, y, 0);
		gl.glRotatef(angle, 0, 0, 1); /* rotate clockwise on the z=0 2D plane */
		gl.glScalef(scale, scale, 1); /*
									 * scale everything with the given scale
									 * factor
									 */

		for (int i = 0; i < STARS; i++) {
			gl.glRotatef(360.f / (float)STARS, 0, 0, 1);
			if (i % 2 == 0) {
				redStar.draw(gl);
			} else {
				yellowStar.draw(gl);
			}
		}

		/* Restore the Model-View matrix */
		gl.glPopMatrix();
	}

	/**
	 * resets the stars and sets the visiblity to true
	 */
	public void show() {
		angle = 0.f;
		scale = .3f;
		visible = true;
	}

	/**
	 * move the stars 
	 */
	public void tick() {
		if (visible) {
			redStar.setRotation(redStar.getRotation() + 5);
			yellowStar.setRotation(yellowStar.getRotation() - 5);
			if (scale > MAXSCALE) {
				visible = false;
			}
			angle += 2.f;
			scale *= 1.05;
		}
	}
}
