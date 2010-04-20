package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Pedestrian {

	private Hair hair;
	private Head head;
	private Torso torso;
	private Arms arms;
	private Legs legs;	
	
	private Vector2 position;
	
	private Color color_skin;
	private Color color_hair;
	private Color color_torso;
	int type_hair;
	private float attractionRadius;
	private float grabSpeed;
	private float fightingRadius;
	private float moveSpeed;
	
	public Pedestrian(GL10 gl, Context context) {
		this( 30.0f,10.0f,1.0f, 1.0f, gl, context);
	}
	
	public Pedestrian(float attractionRadius, float fightingRadius, float moveSpeed, float grabSpeed, GL10 gl, Context context) {
		this.attractionRadius = attractionRadius;
		this.grabSpeed = grabSpeed;
		this.fightingRadius = fightingRadius;
		this.moveSpeed = moveSpeed;
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
	
	public Vector2 getPosition(){
		return this.position;
	}
	
	public void draw(GL10 gl) {
		
		legs.draw(gl);
		arms.draw(gl);
		torso.draw(gl);
		head.draw(gl);
		hair.draw(gl);
		
	}
	public float getAttractionRadius(){
		return this.attractionRadius;
	}
	public float getGrabSpeed(){
		return this.grabSpeed;
	}
	public float getFightingRadius(){
		return this.fightingRadius;
	}
}
