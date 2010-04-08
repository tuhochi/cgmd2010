package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Treasure implements Target{
	private float value;
	private float attractionRadius;
	private Vector2 position;
	private static final int treasure_texture_id = R.drawable.l11_treasure;
	private Square sprite;
	public Treasure(float value, float attractionRadius, Vector2 position){
		this.value = value;
		//System.out.println(value);
		this.attractionRadius = attractionRadius;
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
		if(this.value > 1)
			return true;
		else{
			this.value = 0;
			return false;
		}
	}

	public void setPosition(Vector2 pos) {
		this.position = pos;
	}
	
	public Vector2 getPosition(){
		return this.position;
	}
	public void draw(GL10 gl) {

		Textures.tex.setTexture(treasure_texture_id);
		
		gl.glLoadIdentity();
		gl.glTranslatef(position.x, position.y, 0.0f);
		gl.glScalef(10.0f+this.value, 10.0f+this.value, 1.0f);
		sprite.draw(gl);
	}
	public float getValue(){
		return this.value;
	}
}
