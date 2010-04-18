package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

public class ModelGem extends Model {

	float qs = 0.3f; //quadsize
	
		/** Quad vertices */
		protected float vertices[] = {
				-qs, -qs, qs, //v0
		    	qs, -qs, qs,  //v1
		    	-qs, qs, qs,  //v2
		    	qs, qs, qs,   //v3
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
		
		private boolean visibility = false;
		
		/**
		 * Creates a new quad.
		 */
		public ModelGem() {
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
		public ModelGem(int textureResource) {
			this();
			this.textureResource = textureResource;
			this.visibility = false;
		}
		
		
		public void setVisible()
		{
			this.visibility = true;
		}
		
		public void setInvisible()
		{
			this.visibility = false;
		}
		
		public void changeVisibility()
		{
			this.visibility = !this.visibility;
		}
		
		public void rotateLeft()
		{
			
			
		}
		
		public void rotateRight()
		{
			
		}
		
		/**
		 * Update the model's transformations.
		 */
		public void update(GL10 gl, double deltaTime) {
			//mTrans = Matrix4x4.mult(Matrix4x4.RotateX((float)(1f * deltaTime)), mTrans);
			if (visibility)
			{
				gl.glPushMatrix();
				gl.glTranslatef(0, 0, -2);
				gl.glMultMatrixf(mTrans.toFloatArray(), 0);
			}
		}
}

	

