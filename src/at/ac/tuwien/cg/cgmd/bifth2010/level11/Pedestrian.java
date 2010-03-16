package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Pedestrian {

	Hair hair;
	Head head;
	Torso torso;
	Arms arms;
	Legs legs;	
	
	Vector2 position;
	
	Color color_skin;
	Color color_hair;
	Color color_torso;
	int type_hair; 
	
	public Pedestrian(GL10 gl, Context context) {
		
		
		
		head = new Head(gl, context);
		hair = new Hair(gl, context);
		torso = new Torso(gl, context);
		legs = new Legs(gl, context);
		arms = new Arms(gl, context, new Color(), new Color());
	}
	
	public void update() {
		
	}
	
	public void setPosition(Vector2 pos) {
		position = pos;
	}
	
	public void draw(GL10 gl) {
		
		legs.draw(gl);
		arms.draw(gl);
		torso.draw(gl);
		head.draw(gl);
		hair.draw(gl);
		
	}
}
