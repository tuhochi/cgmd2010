package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class ModelStreet extends Model {
	
	float width;
	float height;
	
	HashMap<Integer, ModelDrain> drains;
	
	private float posX; //street position at startup
	private float speed; //speed of street translation
	private float posZ = -10f; //z-position of street
	
	/**
	 * Creates a new street.
	 * @param width The street's length
	 * @param height The street's height
	 * @param posX Horizontal position
	 * @param speed Defines the horizontal speed the street and its drains pass by
	 * @param textureResource The street's texture
	 * @param drains List containing all drains
	 */
	public ModelStreet(float width, float height, float posX, float speed, int textureResource, HashMap<Integer, ModelDrain> drains) {
		this.width = width;
		this.height = height;
		this.posX = posX;
		this.speed = speed;
		this.textureResource = textureResource;
		this.drains = drains;
		
		//Adjust the width of Model's quad.
		vertices[0] = vertices[6] = -width/2.0f;
		vertices[3] = vertices[9] = width/2.0f;
		
		//Adjust the height of Model's quad.
		vertices[1] = vertices[4] = -height/2.0f;
		vertices[7] = vertices[10] = height/2.0f;
		
		//Adjust the texture coordinates of Model's quad.
		texture[2] = texture[6] = width / height;
		
		fillBuffers();
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime, float deviceRotation) {
		super.update(gl, deltaTime, deviceRotation);
		float deltaSpeed = (float)((double)speed * deltaTime);
		posX += deltaSpeed;
	}
	
	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glRotatef(deviceRotation, 0, 0, 1);
		gl.glTranslatef(posX, 0, posZ);
		
		//Draw street.
		super.draw(gl);

		Iterator<ModelDrain> i = drains.values().iterator();
		while(i.hasNext()) {
			ModelDrain drain = i.next();
		
			gl.glPushMatrix();
			gl.glTranslatef(drain.getPosition(), 0, 0);
				
			//Draw drain.
			drain.draw(gl);
			
			gl.glPopMatrix();
		}
		
		gl.glPopMatrix();
	}
	
	public void loadGLTexture(GL10 gl, Context context) {
		super.loadGLTexture(gl, context);
		
		Iterator<ModelDrain> i = drains.values().iterator();
		while(i.hasNext())
			i.next().loadGLTexture(gl, context);
	}
}