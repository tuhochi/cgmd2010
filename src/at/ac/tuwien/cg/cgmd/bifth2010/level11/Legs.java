package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
/**
 * class, representing the legs sprite of pedestrians
 *
 */
public class Legs {
	
	private static final int leg_texture_id = R.drawable.l11_pedestrian_leg;
	/**
	 * square onto which the texture is rendered
	 */
	private Square left_leg, right_leg;
	
    private static final String LOG_TAG = Legs.class.getSimpleName();
    /**
     * position of the legs in worldcoordinates
     */
	private Vector2 position;
	/**
	 * orientation of the legs in the level
	 */
	private float angle;
	/**
	 * relative position of the legs = animation
	 */
	private float leg_position;
	/**
	 * color of the legs
	 */
	private Color color;
	
	/**
	 * legs constructor
	 * @param gl
	 * @param context
	 */
	public Legs(GL10 gl, Context context) {
		left_leg = new Square();
		right_leg = new Square();

		leg_position = 0.0f;
		
		color = new Color();
	}
	
	/**
	 * update legs position and rotation
	 * @param pos
	 * @param angle
	 * @param leg_pos
	 */
	public void update(Vector2 pos, float angle, float leg_pos) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
		this.leg_position = 5.0f*leg_pos;
		
	}
	
	/**
	 * draws legs
	 * @param gl
	 */
	public void draw(GL10 gl) {

		gl.glColor4f(color.r, color.g, color.b, 1.0f);
		Textures.tex.setTexture(leg_texture_id);
		
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(0.0f, 5.0f, 0.0f);
			gl.glTranslatef(leg_position, 0.0f, 0.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			left_leg.draw(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(0.0f, -5.0f, 0.0f);
			gl.glTranslatef(-leg_position, 0.0f, 0.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			right_leg.draw(gl);
		gl.glPopMatrix();
	}

	/**
	 * set color for legs
	 * @param c
	 */
	public void setColor(Color c) {
		color = c;
		
	}
}
