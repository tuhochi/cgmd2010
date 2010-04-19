package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class ModelDrain extends Model {

	/**
	 * drain main class
	 */

	private float width = 0.5f;
	
	private int texture_drain0 = R.drawable.l00_coin; //drain with no holes
	private int texture_drain1 = R.drawable.l00_coin; //drain for gem1
	private int texture_drain2 = R.drawable.l00_coin; //drain for gem2
	private int texture_drain3 = R.drawable.l00_coin; //drain for gem3
	private int texture_drain4 = R.drawable.l00_coin; //drain for gem4
	
	private float orientation = 0;
	
	private float pos;
	
	public ModelDrain(int drainstyle, float pos)
	{
		this.setTexture(drainstyle);
		this.setPosition(pos);
		
		//Adjust the width of Model's quad.
		vertices[0] = vertices[6] = -width/2.0f;
		vertices[3] = vertices[9] = width/2.0f;
		
		//Adjust the height of Model's quad.
		vertices[1] = vertices[4] = -width/2.0f;
		vertices[7] = vertices[10] = width/2.0f;
		
		fillBuffers();
	}
	
	/**
	 * @param drainstyle set the texture for the different drain shapes
	 */
	public void setTexture(int drainstyle)
	{
		switch (drainstyle)
		{
			case 0:	this.textureResource = texture_drain0; break;
			case 1:	this.textureResource = texture_drain1; break;
			case 2:	this.textureResource = texture_drain2; break;
			case 3:	this.textureResource = texture_drain3; break;
			case 4:	this.textureResource = texture_drain4; break;
		}
	}
	
	/**
	 * @param angle set the angle of the drain (hole)
	 */
	public void setOrientationAngle(float angle)
	{
		this.orientation = angle;
	}
	
	public float getOrientationAngle()
	{
		return this.orientation;
	}
	
	
	/**
	 * @param xOffset position of the drain on the street
	 */
	public void setPosition(float pos)
	{
		this.pos = pos;
	}
	
	public float getPosition()
	{
		return this.pos;
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime, float deviceRotation) {
		/*streetPos -= streetSpeed;
		drainPos = streetPos + this.xPos;
		
		gl.glPushMatrix();
		gl.glRotatef(deviceRotation, 0, 0, 1);
		gl.glTranslatef(-drainPos, 0, streetLevel);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);*/
	}
}
