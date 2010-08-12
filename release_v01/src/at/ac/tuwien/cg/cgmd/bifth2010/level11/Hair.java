package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * hair sprite of a pedestrian
 * @author g11
 */
public class Hair {

    private static final String LOG_TAG = Hair.class.getSimpleName();
    /**
     * position in world coordinates
     */
	private Vector2 position;
	/**
	 * orientation in the level
	 */
	private float angle;
	/**
	 * hair color
	 */
	private Color color;
	/**
	 * texture id
	 */
	private static int hair_01_texture_id = R.drawable.l11_pedestrian_hair_01;
	//private static int hair_02_texture_id = R.drawable.l11_pedestrian_hair_02;
	//private static int hair_03_texture_id = R.drawable.l11_pedestrian_hair_03;
	/**
	 * square on that the texture is rendered
	 */
	private Square hair;
	/**
	 * determines the texture of the hair
	 */
	private int hair_type;
	
	/**
	 * hair constructor
	 * @param gl
	 * @param context
	 */
	public Hair(GL10 gl, Context context) {
		hair = new Square();
		
		hair_type = 0;
		//hair_type = Math.round(Math.random()*3);
		color = new Color();
	}
	
	/**
	 * updates hair position and rotation
	 * @param pos
	 * @param angle
	 */
	public void update(Vector2 pos, float angle) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
	
	}
	
	/**
	 * sets hair color
	 * @param c
	 */
	public void setColor(Color c) {
		this.color = c;
	}
	
	/**
	 * draws hair
	 * @param gl
	 */
	public void draw(GL10 gl) {

		gl.glColor4f(color.r, color.g, color.b, 1.0f);
		if (hair_type == 0) {	
			Textures.tex.setTexture(hair_01_texture_id);
		/*} else if (hair_type == 1) {
			this.texture.setTexture(hair_02_texture_id);
		} else if (hair_type == 2) }
			this.texture.setTexture(hair_03_texture_id);
			*/
		}

		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			hair.draw(gl);
		gl.glPopMatrix();
		
	}
	
}