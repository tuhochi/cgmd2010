package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class Hair {

    private static final String LOG_TAG = Hair.class.getSimpleName();
	private Vector2 position;
	private float angle;
	
	private Color color;

	private static int hair_01_texture_id = R.drawable.l11_pedestrian_hair_01;
	//public static int hair_02_texture_id = R.drawable.l11_pedestrian_hair_02;
	//public static int hair_03_texture_id = R.drawable.l11_pedestrian_hair_03;
	
	private Square hair;
	
	private int hair_type;
	
	public Hair(GL10 gl, Context context) {
		hair = new Square();
		
		hair_type = 0;
		color = new Color();
	}
	
	public void update(Vector2 pos, float angle) {
		//Log.i(LOG_TAG, "update()");
		this.position = pos;
		this.angle = angle;
	
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public void draw(GL10 gl) {
		

		gl.glColor4f(color.r, color.g, color.b, 1.0f);
		if (hair_type == 0) {	
			Textures.tex.setTexture(hair_01_texture_id);
			

			
	//	} else if () {
			
	//	} else if () }
			//this.texture.setTexture(hair_03_texture_id);
			
		}

		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y, 0.0f);
		gl.glScalef(50.0f, 50.0f, 1.0f);
		hair.draw(gl);
		
	}
	
}