package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class Hair {
	
	private Vector2 position;
	private Color color;
	private Texture texture;
	
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
		
	}
	
}