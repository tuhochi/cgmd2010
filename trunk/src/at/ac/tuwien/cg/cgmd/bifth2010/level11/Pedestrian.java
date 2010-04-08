package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class Pedestrian {

    private static final String LOG_TAG = Pedestrian.class.getSimpleName();
    
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
	private float angle;
	private Treasure targetTreasure;
	private float oldTime;
	private Vector2 temp;
	
	public Pedestrian(GL10 gl, Context context) {
		this( 30.0f,10.0f,0.003f, 1.0f, gl, context);
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
		arms = new Arms(gl, context);
		
		this.angle = 0.0f;
		this.moveSpeed = 4.0f;
		this.targetTreasure = null;
		this.setColors();
		this.oldTime = 0;
		this.temp = new Vector2();
	}
	
	public void setColors() {
	
		Random rand = new Random();

		Color color_skin = new Color();
		Color color_hair = new Color();
		Color color_shirt = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f);
		Color color_pants = new Color();
		
		int i = rand.nextInt(4);
		

		// define skin color
		switch (i) {
			case 0:
				color_skin = Color.caucasian;
			case 1:
				color_skin = Color.caucasian;
				break;
			case 2:
				color_skin = Color.asian;
				break;
			case 3:
				color_skin = Color.african;
				break;
			default:
				color_skin = Color.caucasian;
				break;
		}
		
		// define hair color
		int j = rand.nextInt(3);
		
		switch (j) {
			case 0:
				color_hair = Color.black;
				break;
			case 1:
				color_hair = Color.brown;
				break;
			case 2:
				color_hair = Color.blonde;
				break;
			default:
				color_hair = Color.black;
				break;
		}
		
		// define  pants color
		int k = rand.nextInt(2);
		
		switch (k) {
			case 0:
				color_pants = Color.black;
				break;
			case 1:
				color_pants = Color.brown;
				break;
			default:
				color_pants = Color.brown;
				break;
		}
		
		head.setColor(color_skin);
		hair.setColor(color_hair);
		torso.setColor(color_shirt);
		legs.setColor(color_pants);
		arms.setColor(color_shirt, color_skin);
	}
	
	public void update(float time) {
		if(oldTime == 0)
			oldTime = time;
		float deltaTime = time - oldTime;
		oldTime = time;
		if(targetTreasure != null){//move pedestrian towards target
			/*this.position = this.position.add(
							targetTreasure.getPosition().sub(this.position).
							normalize().mult(this.moveSpeed*deltaTime));*/
			this.temp.set(this.position.x, this.position.y);
			this.position.add(this.temp.subThisFrom(
					targetTreasure.getPosition()).normalize()
					.mult(this.moveSpeed*deltaTime));
		}
		
		legs.update(position, angle, (float)(Math.sin(time*moveSpeed)));
		arms.update(position, angle, (float)(Math.sin(time*moveSpeed)));
		torso.update(position, angle);
		head.update(position, angle);
		hair.update(position, angle);
	}
	
	public void setPosition(Vector2 pos) {
		this.position = pos;
	}
	
	public Vector2 getPosition(){
		return this.position;
	}
	
	public void draw(GL10 gl) {

		//gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

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
	public void setTargetTreasure(Treasure target){
		this.targetTreasure = target;
		this.angle = (float)Math.atan2(target.getPosition().y - this.position.y, target.getPosition().x - this.position.x) * (180.0f/(float)Math.PI);
		//this.angle = (float)Math.atan( (treasurePosition.y - this.position.y) / (treasurePosition.x - this.position.x) )
	
	}
}
