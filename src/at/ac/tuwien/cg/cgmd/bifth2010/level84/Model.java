package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;

/**
 * Baseclass for all OpenGL models.
 * @author Gerald, Georg
 */
public class Model {
	
	/** The buffer holding the vertices */
	protected FloatBuffer vertexBuffer;
	/** The buffer holding the texture coordinates */
	protected FloatBuffer textureBuffer;
	/** The buffer holding the indices */
	protected ByteBuffer indexBuffer;
	
	/** Number of indices. */
	protected int numIndices = 0;
	/** Our texture pointer */
	protected int[] textures = new int[1];
	/** Texture resource */
	protected int textureResource = -1;
	
	/** Transformation matrix */
	protected Matrix4x4 mTrans = new Matrix4x4();
	/** Rotation angle of the device in degrees */
	protected float deviceRotation;
	/** Delta time between the current and the last frame */
	protected double deltaTime;

	/** Quad vertices */
	protected float vertices[] = {
		-1.0f, -1.0f, 1.0f,
		 1.0f, -1.0f, 1.0f,
		-1.0f, 1.0f, 1.0f,
		 1.0f, 1.0f, 1.0f,
	};
	/** Quad texcoords */
	protected float texture[] = {
		0.0f, 1.0f,
	    1.0f, 1.0f,
	    0.0f, 0.0f,
	    1.0f, 0.0f
	};
	/** Quad indices */
	protected byte indices[] = {0,1,3, 0,3,2};
	
	/**
	 * Creates a new model.
	 */
	public Model() {
	}
	
	/**
	 * Creates a new model with an initial texture resource.
	 * @param textureResource
	 */
	public Model(int textureResource) {
		this();
		this.textureResource = textureResource;
	}
	
	/**
	 * Fills vertex/texture/index-buffers.
	 */
	protected void fillBuffers() {
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
	 * Updates the model's transformation(s).
	 * @param deltaTime
	 */
	public void update(GL10 gl, double deltaTime, float deviceRotation) {
		this.deltaTime = deltaTime;
		this.deviceRotation = deviceRotation;
	}
	
	/**
	 * This function draws our square on screen.
	 * @param gl
	 */
	public void draw(GL10 gl) {

		if (vertexBuffer != null && indexBuffer != null && numIndices > 0) {
		
			//Bind our only previously generated texture in this case
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

			//Point to our buffers
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CCW);
			
			//Enable the vertex and texture state
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawElements(GL10.GL_TRIANGLES, numIndices, GL10.GL_UNSIGNED_BYTE, indexBuffer);
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
	}
	
	/**
	 * Loads a texture.
	 * 
	 * @param gl - The GL Context
	 * @param context - The Activity context
	 */
	public void loadGLTexture(GL10 gl, Context context) {
		
		if (textureResource != -1) {			
			//Get the texture from the Android resource directory
			InputStream is = context.getResources().openRawResource(textureResource);
			Bitmap bitmap = null;
			try {
				//BitmapFactory is an Android graphics utility for images
				bitmap = BitmapFactory.decodeStream(is);
	
			} finally {
				//Always clear and close
				try {
					is.close();
					is = null;
				} catch (IOException e) {
				}
			}
	
			//Generate one texture pointer...
			gl.glGenTextures(1, textures, 0);
			//...and bind it to our array
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			
			//Create Nearest Filtered Texture
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
			//Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			//Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			
			//Clean up
			bitmap.recycle();
		}
	}
}
