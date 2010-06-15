package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
/**
 * class, representing the head sprite of a pedestrian
 *
 */
public class Head {

    private static final String LOG_TAG = Hair.class.getSimpleName();
    /**
     * position of the head in world coordinates
     */
	private Vector2 position;
	/**
	 * orientation of the head in the level
	 */
	private float angle;
	/**
	 * texture id
	 */
	public static int head_texture_id = R.drawable.l11_pedestrian_head;
	/**
	 * square onto which the texture is rendered
	 */
	private Square head;
	/**
	 * color of the head = skin color
	 */
	private Color color;
	
	/**
	 * head constructor
	 * @param gl
	 * @param context
	 */
	public Head(GL10 gl, Context context) {
		head = new Square();
		
		color = new Color();
	}
	
	/**
	 * updates head position and rotation
	 * @param pos
	 * @param angle
	 */
	public void update(Vector2 pos, float angle) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
	
	}
	
	/**
	 * draws head
	 * @param gl
	 */
	public void draw(GL10 gl) {
		
		gl.glColor4f(color.r, color.g, color.b, 1.0f);
		Textures.tex.setTexture(head_texture_id);
		
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			head.draw(gl);
		gl.glPopMatrix();
		
	}

	/**
	 * sets head color
	 * @param c
	 */
	public void setColor(Color c) {
		this.color = c;
		
	}
}
