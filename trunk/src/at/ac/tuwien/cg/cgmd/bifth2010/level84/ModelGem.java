package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;

public class ModelGem extends Model {

	float width = 0.3f; //quadsize
		
	private final float gemStartpos = -2f; 
	private boolean visibility = false;
	private float gemRotation = 0.0f;
	private float gemPos = gemStartpos;
	private float fallSpeed = 0.0f;
	private boolean active = false;
	
	//TODO: ev. suebergabe des wertes ??
	private float streetLevel = -10f;
	
	/**
	 * Creates a new quad.
	 */
	public ModelGem() {

		//Adjust the width of Model's quad.
		vertices[0] = vertices[6] = -width/2.0f;
		vertices[3] = vertices[9] = width/2.0f;
		
		//Adjust the height of Model's quad.
		vertices[1] = vertices[4] = -width/2.0f;
		vertices[7] = vertices[10] = width/2.0f;
		
		fillBuffers();
	}
	
	/**
	 * Creates a new quad with an initial texture resource.
	 * @param textureResource
	 */
	public ModelGem(int textureResource) {
		this();
		this.textureResource = textureResource;
		this.visibility = false;
	}
	
	public void setVisible() {
		this.visibility = true;
	}
	
	public void setInvisible() {
		this.visibility = false;
	}
	
	public void changeVisibility() {
		this.visibility = !this.visibility;
	}
	
	public void rotateLeft() {
		gemRotation -= 0.5f;
	}
	
	public void rotateRight() {
		gemRotation += 0.5f;
	}

	public void resetPosition() {
		gemPos = gemStartpos;
		fallSpeed = 0;
	}
	
	public void startFall()	{
		this.setVisible();
		this.active = true;
	}
	
	public void endFall() {
		this.setInvisible();
		this.active = false;
		resetPosition();
	}
	
	public boolean checkCollision()	{
		if (gemPos <= streetLevel) {
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime) {
		
		if (this.active) {
			if (!checkCollision()) {
				fallSpeed += 5f * deltaTime;
				gemPos -= fallSpeed;
			}
			else {
				//TODO: check ausrichtung, punkte, ...
				endFall();
			}
		}
	}
	
	public void draw(GL10 gl) {

		//mTrans = Matrix4x4.mult(Matrix4x4.RotateZ(gemRotation), mTrans);
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, gemPos);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);
		
		super.draw(gl);
		
		gl.glPopMatrix();
	}
}

	

