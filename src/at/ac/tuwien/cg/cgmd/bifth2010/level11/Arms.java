package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Arms {

    private static final String LOG_TAG = Arms.class.getSimpleName();
	
	private static final int arm_texture_id = R.drawable.l11_pedestrian_arm;
	private static final int hand_texture_id = R.drawable.l11_pedestrian_hand;
	
	private Square left_arm, right_arm, left_hand, right_hand;
	
	private Color color_arm, color_skin = new Color();
	private Vector2 position, position_leftarm, position_rightarm;
	float angle;
	float arm_position;
	
	/**
	 * arm constructor
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
	 * sets color of arms
	 * @param c
	 * @param sc
	 */
	public void setColor(Color c, Color sc) {
		color_arm = c;
		color_skin = sc;
		
	}
	
	/**
	 * update arm position and rotation
	 * @param pos
	 * @param angle
	 * @param arm_pos
	 */
	public void update(Vector2 pos, float angle, float arm_pos) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
		this.arm_position = 3.0f*arm_pos;
	
	}
	
	/**
	 * draws arms
	 * @param gl
	 */
	public void draw(GL10 gl) {
		//Log.i(LOG_TAG, "draw()");
		

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
