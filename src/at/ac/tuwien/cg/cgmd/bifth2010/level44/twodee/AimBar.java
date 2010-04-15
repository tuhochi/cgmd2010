package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import javax.microedition.khronos.opengles.GL10;

public class AimBar extends SpriteContainer {
	private Sprite foreground = null;
	private float progress = 0.0f;
	
	public AimBar(Texture texture) {
		super(TextureParts.makeAimBarBackground(texture));
		
		foreground = new Sprite(TextureParts.makeAimBarForeground(texture));
		foreground.setPosition(0, getHeight()/2);
		foreground.setCenter(0, getHeight());
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Override
	protected void onAfterDraw(GL10 gl) {
		super.onAfterDraw(gl);
		
		gl.glPushMatrix();
		gl.glTranslatef(-getWidth()/2, 0, 0);
		gl.glScalef(progress, 1, 1);
		foreground.draw(gl);
		gl.glPopMatrix();
	}

}
