package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Torso {

    private static final String LOG_TAG = Torso.class.getSimpleName();
	private Vector2 position;
	private float angle;
	
	private Color color;

	private static final int torso_texture_id = R.drawable.l11_pedestrian_torso;
	private static final int shadow_texture_id = R.drawable.l11_pedestrian_shadow;
	
	private Square torso;
	private Square shadow;
	
	public Torso(GL10 gl, Context context) {
		torso = new Square();
		shadow = new Square();
		
		color = new Color();
	}
	
	public void update(Vector2 pos, float angle) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
	
	}
	
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

	public void setColor(Color c) {
		this.color = c;
		
	}
}
