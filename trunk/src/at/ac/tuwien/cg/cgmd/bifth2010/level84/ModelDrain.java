package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class ModelDrain extends Model {

	/**
	 * drain main class
	 */

	private float qs = 0.5f;
	
	/** Quad vertices */
	protected float vertices[] = {
			-qs, -qs, 1.0f, //v0
	    	qs, -qs, 1.0f,  //v1
	    	-qs, qs, 1.0f,  //v2
	    	qs, qs, 1.0f,   //v3
	};
	/** Quad texcoords */
	protected float texture[] = {
			0.0f, 1.0f,
	    	1.0f, 1.0f,
	    	0.0f, 0.0f,
	    	1.0f, 0.0f, 
	};
	/** Quad indices */
	private byte indices[] = {0,1,3, 0,3,2};
	
	private int texture_drain0 = R.drawable.l00_coin; //drain with no holes
	private int texture_drain1 = R.drawable.l00_coin; //drain for gem1
	private int texture_drain2 = R.drawable.l00_coin; //drain for gem2
	private int texture_drain3 = R.drawable.l00_coin; //drain for gem3
	private int texture_drain4 = R.drawable.l00_coin; //drain for gem4
	
	private float xPos = 0;
	private float orientation = 0;
	
	private float streetPos = 2.0f; //street position at startup
	private float streetSpeed = 0.05f; //speed of street translation
	private float streetLevel = -10f; //z-pos of street
	
	private float drainPos = 0;
	
	public ModelDrain()
	{
		numIndices = indices.length;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	public ModelDrain(int drainstyle, float xPos)
	{
		this();
		this.setTexture(drainstyle);
		this.setPosition(xPos);
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
	public void setPosition(float xOffset)
	{
		this.xPos = xOffset;
	}
	
	public float getPosition()
	{
		return this.xPos;
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime) {
		
		gl.glPushMatrix();
		streetPos -= streetSpeed;
		drainPos = streetPos + this.xPos;
		gl.glTranslatef(drainPos, 0, streetLevel);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);
	}
	
	
}
