package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class Hair {
	
	private Vector2 position;
	private Color color;
	
	public static Textures texture;
	public static int hair_01_texture_id = R.drawable.l11_pedestrian_hair_01;
	//public static int hair_02_texture_id = R.drawable.l11_pedestrian_hair_02;
	//public static int hair_03_texture_id = R.drawable.l11_pedestrian_hair_03;
	
	private int hair_type;
	
	public Hair(GL10 gl, Context context) {
		//texture = new Texture(gl, R.drawable.l11_pedestrian_hair, context);
	}
	
	public void setPosition(Vector2 pos) {
		this.position = pos;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public void draw(GL10 gl) {
		
		if (hair_type == 0) {	
			//this.texture.setTexture(hair_01_texture_id);
			
	//	} else if () {
			//this.texture.setTexture(hair_02_texture_id);
			
	//	} else if () }
			//this.texture.setTexture(hair_03_texture_id);
			
		}
	}
	
}