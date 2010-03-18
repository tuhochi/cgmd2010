package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Legs {
	
	private static final int leg_texture_id = R.drawable.l11_pedestrian_leg;
	private Square left_leg, right_leg;
	
    private static final String LOG_TAG = Legs.class.getSimpleName();
    
	private Vector2 position;
	private float angle;
	private float leg_position;
	
	Color color;
	
	public Legs(GL10 gl, Context context) {
		left_leg = new Square();
		right_leg = new Square();

		leg_position = 0.0f;
		
		color = new Color();
	}
	
	public void update(Vector2 pos, float angle, float leg_pos) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
		this.leg_position = 5.0f*leg_pos;
		
	}
	
	public void draw(GL10 gl) {

		gl.glColor4f(color.r, color.g, color.b, 1.0f);
		Textures.tex.setTexture(leg_texture_id);
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y+5.0f, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(leg_position, 0.0f, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		left_leg.draw(gl);
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y-5.0f, 0.0f);
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(-leg_position, 0.0f, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		right_leg.draw(gl);
	}

	public void setColor(Color c) {
		color = c;
		
	}
}
