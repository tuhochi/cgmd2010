package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * Model representing a "Street". Gets a HashMap full of drains that are then rendered onto the street.
 * Since the street is the main part of the level, it can be thought of the level itself.
 * @author Gerald, Georg
 */
public class ModelStreet extends Model {
	
	/** The street's width */
	float width;
	/** The street's height */
	float height;
	
	/** Contains all generated drains. Key is the drain's horizontal position. */
	HashMap<Integer, ModelDrain> drains;
	
	/** Horizontal position at startup */
	private float posX;
	/** Position along the depth-axis */
	private float posZ;

	/** Horizontal translation speed */
	private float speed;
	
	private boolean streetActive = false;
	
	/**
	 * Creates a new street.
	 * @param width The street's length
	 * @param height The street's height
	 * @param posX Horizontal position
	 * @param speed Defines the horizontal speed the street and its drains pass by
	 * @param textureResource The street's texture
	 * @param drains List containing all drains
	 */
	public ModelStreet(float width, float height, float posX, float posZ, float speed, int textureResource, HashMap<Integer, ModelDrain> drains) {
		this.width = width;
		this.height = height;
		this.posX = posX;
		this.posZ = posZ;
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
	 * @return actual horizontal street translation 
	 */
	public float getStreetPos()
	{
		return this.posX;
	}
	
	public float getStreetWidth()
	{
		return this.width/2f + 8f;
	}
	
	
	/**
	 * Update the model's transformations.
	 * @param deltaTime Time that passed between the current and the last frame
	 * @param deviceRotation Current device's rotation in degrees.
	 */
	public void update(double deltaTime, float deviceRotation) {
			super.update(deltaTime, deviceRotation);
			float deltaSpeed = (float)((double)speed * deltaTime);
			
			if (streetActive){ this.posX -= deltaSpeed;}
	}
	
	public void startStreet()
	{
		streetActive = true;
	}
	
	public void stopStreet()
	{
		streetActive = false;
	}
	
	public boolean isStreetActive()
	{
		return streetActive;
	}
	
	/**
	 * Draws the street and all drains.
	 * @param gl OpenGL ES context
	 */
	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glRotatef(deviceRotation, 0, 0, 1);
		gl.glTranslatef(this.posX, 0, posZ);
		
		//Draw street.
		super.draw(gl);

		Iterator<ModelDrain> i = drains.values().iterator();
		while(i.hasNext()) {
			ModelDrain drain = i.next();
		
			//set translation and rotation of the drains
			gl.glPushMatrix();
			gl.glTranslatef(drain.getPosition(), 0, 0);
			gl.glRotatef(drain.getOrientationAngle(), 0, 0, 1.0f);
				
			//Draw drain.
			drain.draw(gl);
			gl.glPopMatrix();
		}
		gl.glPopMatrix();
	}
	
	/** 
	 * Loads the street's and the drains' textures.
	 * @param gl OpenGL ES context
	 * @param context Android activity context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		super.loadGLTexture(gl, context);
		
		Iterator<ModelDrain> i = drains.values().iterator();
		while(i.hasNext())
			i.next().loadGLTexture(gl, context);
	}
}