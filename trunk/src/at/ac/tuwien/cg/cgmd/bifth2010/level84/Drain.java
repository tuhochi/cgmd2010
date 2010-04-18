package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Drain extends Model {

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
	
	private int texture_drain0 = -1; //drain with no holes
	private int texture_drain1 = -1; //drain for gem1
	private int texture_drain2 = -1; //drain for gem2
	private int texture_drain3 = -1; //drain for gem3
	private int texture_drain4 = -1; //drain for gem4
	
	private float xPos = 0;
	private float orientation = 0;
	
	public Drain()
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
	
	public Drain(int drainstyle)
	{
		this();
		this.setTexture(drainstyle);
		
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
		xPos = xOffset;
	}
	
	
}
