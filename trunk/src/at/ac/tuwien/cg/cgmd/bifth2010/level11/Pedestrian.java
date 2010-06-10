package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
/**
 * class pedestrians, who walk around, move towards a target, fight and grab treasure
 * @author askari
 *
 */
public class Pedestrian implements Target{

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
	private Target target;
	private Vector2 temp;
	private Vector2 bounceVector;
	private Random rand;
	private float bounceRadius;
	private float bounceStrength = 0.0002f;
	private float maxBounceVectorLength = 1f;
	/**
	 * constructor with following default values: this( 30.0f,10.0f,0.01f, 2.0f, gl, context)
	 * @param gl
	 * @param context
	 */
	public Pedestrian(GL10 gl, Context context) {
		this( 30.0f,10.0f,20.0f,0.01f, 0.3f, gl, context);
	}
	public float getBounceRadius() {
		return bounceRadius;
	}
	public void setBounceRadius(float bounceRadius) {
		this.bounceRadius = bounceRadius;
	}
	/**
	 * contructor with additional parameters
	 * @param attractionRadius distance at which the pedestrians react to treasure attraction radius
	 * @param fightingRadius distance at which the pedestrians start fighting each other
	 * @param moveSpeed walk speed
	 * @param grabSpeed treasure value grab speed
	 * @param gl
	 * @param context
	 */
	public Pedestrian(float attractionRadius, float fightingRadius, float bounceRadius, float moveSpeed, float grabSpeed, GL10 gl, Context context) {
		this.attractionRadius = attractionRadius;
		this.bounceRadius = bounceRadius;
		this.grabSpeed = grabSpeed;
		this.fightingRadius = fightingRadius;
		this.moveSpeed = moveSpeed;
		head = new Head(gl, context);
		hair = new Hair(gl, context);
		torso = new Torso(gl, context);
		legs = new Legs(gl, context);
		arms = new Arms(gl, context);
		
		rand = new Random();
		
		this.angle = (float)(Math.random()*360.0);
		this.moveSpeed = 4.0f;
		this.target = null;
		this.setColors();
		this.temp = new Vector2();
		this.bounceVector = new Vector2();
	}
	/**
	 * generates the color of the pedestrian randomly
	 */
	public void setColors() {
	

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
	/**
	 * performs walking, moving towards target, fighting, grabbing treasure and updates position of the body parts
	 * @param time
	 */
	public void update(float time, float deltaTime) {
		if(bounceVector.length()>0.0000001){
			//System.out.println("bounce");
			bounceVector.mult((float)(1/Math.pow((1+deltaTime),2)));
			position.add(bounceVector);
			legs.update(position, angle, 0.0f);
			arms.update(position, angle, (float)(Math.sin(time*moveSpeed*10.0f)));
			torso.update(position, angle);
			head.update(position, angle);
			hair.update(position, angle);
			
		}else{
			if(target != null){//target exists
				if(target instanceof Pedestrian){
					legs.update(position, angle, (float)(Math.sin(time*moveSpeed*10.0f)));
					arms.update(position, angle, (float)(Math.sin(time*moveSpeed*10.0f)));
				}else{
					if(((Treasure)target).getValue() <= 0){
						target = null;
						return;
					}
					this.temp.set(this.position.x, this.position.y);
					this.temp.subThisFrom(target.getPosition());
					if(this.temp.length()>10.0f){//move to target
						this.position.add(temp.normalize()
							.mult(this.moveSpeed*deltaTime*2.0f));
						legs.update(position, angle, (float)(Math.sin(time*moveSpeed)));
						arms.update(position, angle, (float)(Math.sin(time*moveSpeed)));
					}else{//near enough to target, so grab treasure
						legs.update(position, angle, 0.0f);
						arms.update(position, angle, (float)(Math.sin(time*moveSpeed*10.0f)));
						((Treasure)target).grabValue(this.grabSpeed*deltaTime);
					}
				}
				torso.update(position, angle);
				head.update(position, angle);
				hair.update(position, angle);
			}else{//no target
				this.temp.set((float)Math.cos(angle/180*Math.PI)*deltaTime*moveSpeed, (float)Math.sin(angle/180*Math.PI)*deltaTime*moveSpeed);
				position.add(this.temp);
				if(this.position.x > Level.sizeX)
					angle = 180;
				else if(this.position.x < 0)
					angle = 0;
				else if(this.position.y > Level.sizeY*Level.ratioFix)
					angle = 270;
				else if(this.position.y < 0)
					angle = 90;
				else 
					angle += rand.nextGaussian()*100.0f*deltaTime;
				legs.update(position, angle, (float)(Math.sin(time*moveSpeed)));
				arms.update(position, angle, (float)(Math.sin(time*moveSpeed)));
				torso.update(position, angle);
				head.update(position, angle);
				hair.update(position, angle);
			}
		}
	}
	/**
	 * sets position to pos in level
	 * @param pos
	 */
	public void setPosition(Vector2 pos) {
		this.position = pos;
	}
	/**
	 * returns position in level
	 */
	public Vector2 getPosition(){
		return this.position;
	}
	/**
	 * draws pedestrians by drawing body parts
	 * @param gl
	 */
	public void draw(GL10 gl) {

		
			legs.draw(gl);
			arms.draw(gl);
			torso.draw(gl);
			head.draw(gl);
			hair.draw(gl);
	}
	/**
	 * returns attraction radius (istance at which the pedestrians react to treasure attraction radius)
	 * @return attractionRadius
	 */
	public float getAttractionRadius(){
		return this.attractionRadius;
	}
	/**
	 * returns the speed at which the pedestrians grab treasure
	 * @return grabSpeed
	 */
	public float getGrabSpeed(){
		return this.grabSpeed;
	}
	/**
	 * 
	 * @return
	 */
	public float getFightingRadius(){
		return this.fightingRadius;
	}
	/**
	 * sets the current target to target
	 * @param target
	 */
	public void setTarget(Target target){
		this.target = target;
		this.angle = (float)Math.atan2(target.getPosition().y - this.position.y, target.getPosition().x - this.position.x) * (180.0f/(float)Math.PI);
		//this.angle = (float)Math.atan( (treasurePosition.y - this.position.y) / (treasurePosition.x - this.position.x) )
	
	}
	/**
	 * returns the current targe. null if no target is set
	 * @return target
	 */
	public Target getTarget(){
		return this.target;
	}
	public void bounce(float x1, float y1, float x2, float y2, float time){
		this.bounceVector.x += x1-x2;
		this.bounceVector.y += y1-y2;
		this.bounceVector.mult(bounceStrength/time);
		if(this.bounceVector.length() > this.maxBounceVectorLength)
			this.bounceVector.normalize().mult(this.maxBounceVectorLength);
		target = null;
		//System.out.println("bounceVector: "+bounceVector);
	}
}
