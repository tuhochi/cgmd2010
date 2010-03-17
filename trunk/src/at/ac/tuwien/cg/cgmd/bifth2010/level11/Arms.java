package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Arms {

    private static final String LOG_TAG = Arms.class.getSimpleName();
	private Color color_arm, color_skin;
	private Vector2 position_leftarm, position_rightarm;
	
	public static Textures texture;
	public static int arm_texture_id = R.drawable.l11_pedestrian_arm;
	public static int hand_texture_id = R.drawable.l11_pedestrian_hand;
	
	public Arms(GL10 gl, Context context, Color color_arm, Color color_skin) {
		Log.i(LOG_TAG, "Arms()");
		
		this.color_arm = color_arm;
		this.color_skin = color_skin;
		
	}
	
	public void update(float speed) {
		Log.i(LOG_TAG, "update()");
		
		
	}
	
	public void draw(GL10 gl) {
		Log.i(LOG_TAG, "draw()");
		
	}
	
}
