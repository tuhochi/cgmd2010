package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * class, representing the treasure object in the level, with a value and a position
 * @author g11
 */
public class Treasure implements Target{
	/**
	 * current value of the treasure
	 */
	private float value;
	/**
	 * distance at which pedestrians are attracted
	 */
	private float attractionRadius;
	/**
	 * position of the treasure in world coordinates
	 */
	private Vector2 position;
	/**
	 * texture id
	 */
	private static final int treasure_texture_id = R.drawable.l11_treasure;
	/**
	 * sound id of the sound played when dropping the treasure
	 */
	private static final int treasure_sound_01 = R.raw.l00_gold01;
	/**
	 * factor, that resizes the attraction circle at creation
	 */
	public static final float attractionRadiusMultiplacator = 40.0f; 
	/**
	 * square, onto which the texture is rendered
	 */
	private Square sprite;
	/**
	 * value at time of creation
	 */
	private float startingValue;
	
	public Treasure(float value, Vector2 position){
		this(value, value, position);
	}

	public Treasure(float value, float startingValue, Vector2 position){
		this.startingValue = value;
		this.value = this.startingValue;
		this.attractionRadius = value*attractionRadiusMultiplacator;
		this.position = position;
		this.sprite = new Square();
		Sounds.singleton.play(treasure_sound_01);
		
	}
	/**
	 * returns the radius, at which the pedestrians are attracted to the treasure
	 * @return
	 */
	public float getAttractionRadius(){
		return this.attractionRadius;
	}
	/**
	 * subtracts value from the treasure and returns false if treasure is empty
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

		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Textures.tex.setTexture(treasure_texture_id);
		
		gl.glPushMatrix();
			gl.glTranslatef(position.x, position.y, 0.0f);
			gl.glScalef(10.0f+this.value*2, 10.0f+this.value*2, 1.0f);
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