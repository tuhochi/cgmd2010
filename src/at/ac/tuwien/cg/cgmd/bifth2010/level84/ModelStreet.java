package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.List;
import java.util.ListIterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class ModelStreet extends Model {
	
	float width;
	float height;
	
	List<ModelDrain> drains;
	
	private float streetPos; //street position at startup
	private float streetSpeed = 10f; //speed of street translation
	private float streetLevel = -10f; //z-position of street
	
	/**
	 * Creates a new street.
	 * @param width The street's length
	 * @param textureResource The street's texture
	 * @param drains List containing all drains
	 */
	public ModelStreet(float width, float height, int textureResource, List<ModelDrain> drains) {
		this.width = width;
		this.height = height;
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
		
		streetPos = width / 2.0f;
		
		fillBuffers();
	}
	
	public float getStreetTranslation() {
		return streetPos;
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime, float deviceRotation) {
		//mTrans = Matrix4x4.mult(Matrix4x4.RotateX((float)(1f * deltaTime)), mTrans);
		streetPos -= streetSpeed;
	}
	
	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glRotatef(deviceRotation, 0, 0, 1);
		gl.glTranslatef((float)(-streetPos * deltaTime), 0, streetLevel);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);
		
		super.draw(gl);
		
		ListIterator<ModelDrain> i = drains.listIterator();
		while(i.hasNext()) {
			ModelDrain drain = i.next();
		
			gl.glPushMatrix();
			gl.glTranslatef(drain.getPosition(), 0, 0);
			drain.draw(gl);
			gl.glPopMatrix();
		}
		
		gl.glPopMatrix();
	}
	
	public void loadGLTexture(GL10 gl, Context context) {
		super.loadGLTexture(gl, context);
		
		ListIterator<ModelDrain> i = drains.listIterator();
		while(i.hasNext())
			i.next().loadGLTexture(gl, context);
	}
}