package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Torso {

	Vector2 position;

	public static Textures texture;
	public static int torso_texture_id = R.drawable.l11_pedestrian_torso;
	
	public Torso(GL10 gl, Context context) {
		//texture = new Texture(gl, R.drawable.l11_pedestrian_torso, context);
	}
	
	public void update(Vector2 pos) {
		position = pos;
		
		
	}
	
	public void draw(GL10 gl) {
		//this.texture.setTexture(torso_texture_id);
	}
}
