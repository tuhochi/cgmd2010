package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Arms {

	Color color_arm, color_skin;
	Texture texture_arm, texture_hand;
	Vector2 position_leftarm, position_rightarm;
	
	public Arms(GL10 gl, Context context, Color color_arm, Color color_skin) {
		//texture_arm = new Texture(gl, R.drawable.l11_pedestrian_arm, context);
		//texture_hand = new Texture(gl, R.drawable.l11_pedestrian_hand, context);
		
		this.color_arm = color_arm;
		this.color_skin = color_skin;
		
	}
	
	public void update(float speed) {
		
		
		
	}
	
	public void draw(GL10 gl) {
		
	}
	
}
