package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Torso {

    private static final String LOG_TAG = Torso.class.getSimpleName();
    /**
     * position of the torso in world coordinates
     */
	private Vector2 position;
	/**
	 * orientation of the torso in the level
	 */
	private float angle;
	/**
	 * color of the torso
	 */
	private Color color;
	/**
	 * texture id
	 */
	private static final int torso_texture_id = R.drawable.l11_pedestrian_torso;
	/**
	 * texture id of the shadow
	 */
	private static final int shadow_texture_id = R.drawable.l11_pedestrian_shadow;
	/**
	 * square, onto which the texture is rendered
	 */
	private Square torso;
	/**
	 * square, onto which the shadow texture is rendered
	 */
	private Square shadow;
	
	/**
	 * torso constructor
	 * @param gl
	 * @param context
	 */
	public Torso(GL10 gl, Context context) {
		torso = new Square();
		shadow = new Square();
		
		color = new Color();
	}
	
	/**
	 * updates torso position and rotation
	 * @param pos
	 * @param angle
	 */
	public void update(Vector2 pos, float angle) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
	
	}
	
	/**
	 * draws torso
	 * @param gl
	 */
	public void draw(GL10 gl) {
		
		gl.glColor4f(color.r, color.g, color.b, 1.0f);
		
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
	
			Textures.tex.setTexture(shadow_texture_id);
			shadow.draw(gl);
			Textures.tex.setTexture(torso_texture_id);
			torso.draw(gl);	
		gl.glPopMatrix();
	}

	/**
	 * sets torso color
	 * @param c
	 */
	public void setColor(Color c) {
		this.color = c;
		
	}
}