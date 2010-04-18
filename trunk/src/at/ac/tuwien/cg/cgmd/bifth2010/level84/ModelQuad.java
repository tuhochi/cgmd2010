package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;

public class ModelQuad extends Model {
	
	/** Quad vertices */
	protected float vertices[] = {
			-1.0f, -1.0f, 1.0f, //v0
	    	1.0f, -1.0f, 1.0f,  //v1
	    	-1.0f, 1.0f, 1.0f,  //v2
	    	1.0f, 1.0f, 1.0f,   //v3
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
	
	private float streetPos = 2.0f; //street position at startup
	private float streetSpeed = 0.05f; //speed of street translation
	
	
	/**
	 * Creates a new quad.
	 */
	public ModelQuad() {
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
	
	/**
	 * Creates a new quad with an initial texture resource.
	 * @param textureResource
	 */
	public ModelQuad(int textureResource) {
		this();
		this.textureResource = textureResource;
	}
	
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime) {
		//mTrans = Matrix4x4.mult(Matrix4x4.RotateX((float)(1f * deltaTime)), mTrans);

		gl.glPushMatrix();
		streetPos -= streetSpeed;
		gl.glTranslatef(streetPos, 0, -10);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);
	}
}
