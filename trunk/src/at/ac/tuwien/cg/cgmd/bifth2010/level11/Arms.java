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
	
	public Arms(GL10 gl, Context context) {
		Log.i(LOG_TAG, "Arms()");
		
		color_arm = new Color();
		color_skin = new Color();
		
		left_arm = new Square();
		right_arm = new Square();
		left_hand = new Square();
		right_hand = new Square();
		
	}
	
	public void setColor(Color c, Color sc) {
		color_arm = c;
		color_skin = sc;
		
	}
	
	public void update(Vector2 pos, float angle) {
		Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
	
	}
	
	public void draw(GL10 gl) {
		Log.i(LOG_TAG, "draw()");
		

		gl.glColor4f(color_arm.r, color_arm.g, color_arm.b, 1.0f);
		
		
		Textures.tex.setTexture(arm_texture_id);
		
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y+10, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		left_arm.draw(gl);
		
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y-10, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		right_arm.draw(gl);
		
		gl.glColor4f(color_skin.r, color_skin.g, color_skin.b, 1.0f);
		
		Textures.tex.setTexture(hand_texture_id);
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y+10, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		left_hand.draw(gl);
		
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y-10, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		right_hand.draw(gl);
		
	}
	
}
