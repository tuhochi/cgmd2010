package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;

public class ModelStreet extends Model {
	
	float length = 9f; //length along the x-axis.
	
	/** Quad vertices */
	protected float vertices[] = {
			-length, -length, 1.0f, //v0
			length, -length, 1.0f,  //v1
	    	-length, length, 1.0f,  //v2
	    	length, length, 1.0f,   //v3
	};
	/** Quad texcoords */
	protected float texture[] = {
			0.0f, 1.0f,
	    	1.0f, 1.0f,
	    	0.0f, 0.0f,
	    	1.0f, 0.0f
	};
	/** Quad indices */
	private byte indices[] = {0,1,3, 0,3,2};
	
	private float streetPos = 0f; //street position at startup
	private float streetSpeed = 0.05f; //speed of street translation
	private float streetLevel = -10f; //z-pos of street
	
	/**
	 * Creates a new quad.
	 */
	public ModelStreet() {
		fillBuffers();
	}
	
	/**
	 * Creates a new quad with an initial texture resource.
	 * @param textureResource
	 */
	public ModelStreet(float length, int textureResource) {
		this.length = length;
		this.textureResource = textureResource;
		
		vertices[0] = vertices[6] = -length/2.0f;
		vertices[3] = vertices[9] = length/2.0f;
		
		texture[2] = texture[6] = length / 20f;
		
		streetPos = length / 2.0f;
		
		fillBuffers();
	}
	
	public int getModeltype()
	{
		return this.modeltype;
	}
	
	public float getStreetTranslation()
	{
		return streetPos;
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime, float deviceRotation) {
		//mTrans = Matrix4x4.mult(Matrix4x4.RotateX((float)(1f * deltaTime)), mTrans);
		streetPos -= streetSpeed;
		
		gl.glPushMatrix();
		gl.glRotatef(deviceRotation, 0, 0, 1);
		gl.glTranslatef(-streetPos, 0, streetLevel);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);
	}
	
	private void fillBuffers() {
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
}
