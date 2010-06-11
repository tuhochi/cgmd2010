package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class represents the aiming bar that will appear when the shaman has
 * targetted our rabbit. It consists of a background texture (the "empty bar")
 * and a foreground texture (the "fill") which will be used to compose a bar
 * that fills itself.
 * 
 * The progress that the bar displays can be set using "setProgress".
 * 
 * @author thp
 */
public class AimBar extends SpriteContainer {
	private Sprite foreground = null;
	private float progress = 0.0f;

	/**
	 * Create a new AimBar from a texture
	 * 
	 * @param texture
	 *            The MireRabbit main texture object that contains the aimbar
	 *            assets
	 */
	public AimBar(Texture texture) {
		super(TextureParts.makeAimBarBackground(texture));

		foreground = new Sprite(TextureParts.makeAimBarForeground(texture));
		foreground.setPosition(0, getHeight() / 2);
		foreground.setCenter(0, getHeight());
	}

	/**
	 * Set the progress that should be displayed by this aimbar
	 * 
	 * @param progress
	 *            The progress from 0 (just aimed) to 1 (shooting)
	 */
	public void setProgress(float progress) {
		this.progress = progress;
	}

	/**
	 * This function is overridden to be able to compose the "filled" part of
	 * the progress bar on top of the bar itself.
	 **/
	@Override
	protected void onAfterDraw(GL10 gl) {
		super.onAfterDraw(gl);

		gl.glPushMatrix();
		gl.glTranslatef(-getWidth() / 2, 0, 0);
		gl.glScalef(progress, 1, 1);
		foreground.draw(gl);
		gl.glPopMatrix();
	}

}
