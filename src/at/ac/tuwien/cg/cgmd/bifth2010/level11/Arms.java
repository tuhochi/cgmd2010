package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Class, representing arm sprites.
 * The arms (2 arms and 2 hands) are part of every pedestrian. 
 * Both arms and hands can be be colored separately.
 * @author g11
 *
 */
public class Arms {
	/**
	 * openGL texture id
	 */
	private static final int arm_texture_id = R.drawable.l11_pedestrian_arm;
	/**
	 * openGL texture id
	 */
	private static final int hand_texture_id = R.drawable.l11_pedestrian_hand;
	
	private Square left_arm, right_arm, left_hand, right_hand;
	/**
	 * color of the arm and the hand skin
	 */
	private Color color_arm, color_skin = new Color();
	/**
	 * position in world coordinates
	 */
	private Vector2 position;
	/**
	 * orientation in the level
	 */
	float angle;
	/**
	 * relative position of the arm = animation
	 */
	float arm_position;
	
	/**
	 * The arm constructor creates four drawable squares,
	 * resets the arm position to zero and sets the arm and skin colors.
	 * @param gl
	 * @param context
	 */
	public Arms(GL10 gl, Context context) {
		//Log.i(LOG_TAG, "Arms()");
		
		arm_position = 0.0f;
		
		color_arm = new Color();
		color_skin = new Color();
		
		left_arm = new Square();
		right_arm = new Square();
		left_hand = new Square();
		right_hand = new Square();
		
	}
	
	/**
	 * Set arm and skin colors.
	 * @param c
	 * @param sc
	 */
	public void setColor(Color c, Color sc) {
		color_arm = c;
		color_skin = sc;
		
	}
	
	/**
	 * Update arm position and rotation (pedestrian alignment)
	 * @param pos
	 * @param angle
	 * @param arm_pos
	 */
	public void update(Vector2 pos, float angle, float arm_pos) {
		this.position = pos;
		this.angle = angle;
		this.arm_position = 3.0f*arm_pos;
	
	}
	
	/**
	 * Draw method (arms)
	 * @param gl
	 */
	public void draw(GL10 gl) {
		gl.glColor4f(color_arm.r, color_arm.g, color_arm.b, 1.0f);
		Textures.tex.setTexture(arm_texture_id);
		
		// left arm
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(0.0f, 9.0f, 0.0f);
			gl.glTranslatef(-arm_position, 0.0f, 0.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			left_arm.draw(gl);
		gl.glPopMatrix();
		
		// right arm
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(0.0f, -9.0f, 0.0f);
			gl.glTranslatef(arm_position, 0.0f, 0.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			right_arm.draw(gl);
		gl.glPopMatrix();
		
		gl.glColor4f(color_skin.r, color_skin.g, color_skin.b, 1.0f);
		
		Textures.tex.setTexture(hand_texture_id);
		
		// left hand
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(0.0f, 9.0f, 0.0f);
			gl.glTranslatef(-arm_position, 0.0f, 0.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			left_hand.draw(gl);
		gl.glPopMatrix();
		
		// right hand
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(0.0f, -9.0f, 0.0f);
			gl.glTranslatef(arm_position, 0.0f, 0.0f);
			gl.glScalef(50.0f, 50.0f, 1.0f);
			right_hand.draw(gl);
		gl.glPopMatrix();
		
	}
}
