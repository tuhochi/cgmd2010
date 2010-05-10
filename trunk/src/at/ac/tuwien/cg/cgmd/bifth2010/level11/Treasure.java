package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Treasure implements Target{
	private float value;
	private float attractionRadius;
	private Vector2 position;
	private static final int treasure_texture_id = R.drawable.l11_treasure;
	private Square sprite;
	private float startingValue;
	public Treasure(float value, Vector2 position){
		this(value, value, position);
	}
	public Treasure(float value, float startingValue, Vector2 position){
		this.startingValue = value;
		this.value = this.startingValue;
		this.attractionRadius = 200.0f;
		this.position = position;
		this.sprite = new Square();
	}
	public float getAttracktionRadius(){
		return this.attractionRadius;
	}
	/**
	 * subtracts value from the trasure and returns false if treasure is empty
	 * @param value
	 * @return
	 */
	public boolean grabValue(float value){
		this.value -= value;
		if(this.value > 0)
			return true;
		else{
			this.value = 0;
			return false;
		}
	}
	/**
	 * sets position in level
	 * @param pos
	 */
	public void setPosition(Vector2 pos) {
		this.position = pos;
	}
	/**
	 * gets position in level
	 */
	public Vector2 getPosition(){
		return this.position;
	}
	/**
	 * draws treasure
	 * @param gl gl-context
	 */
	public void draw(GL10 gl) {

		Textures.tex.setTexture(treasure_texture_id);
		
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glScalef(10.0f+this.value, 10.0f+this.value, 1.0f);
			sprite.draw(gl);
		gl.glPopMatrix();
	}
	/**
	 * returns the current value of the treasure
	 * @return
	 */
	public float getValue(){
		return this.value;
	}
	/**
	 * returns the value that was already grabbed
	 * @return grabbed value
	 */
	public float getGrabbedValue(){
		return this.startingValue - this.value;
	}
	/**
	 * returns the value with which the treasure was placed
	 * @return
	 */
	public float getStartingValue(){
		return this.startingValue;
	}
}