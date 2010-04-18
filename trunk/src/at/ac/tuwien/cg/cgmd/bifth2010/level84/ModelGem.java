package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;

public class ModelGem extends Model {

	float qs = 0.3f; //quadsize
	
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
			gemRotation -= 0.5f;
		}
		
		public void rotateRight()
		{
			gemRotation += 0.5f;
		}

		public void resetPosition()
		{
			gemPos = gemStartpos;
		}
		
		public void startFall()
		{
			this.setVisible();
			active = true;
		}
		
		public void endFall()
		{
			this.setInvisible();
			resetPosition();
		}
		
		public boolean checkCollision()
		{
				if (gemPos <= streetLevel)	{ return true;}
				else return false;
		}
		
		/**
		 * Update the model's transformations.
		 */
		public void update(GL10 gl, double deltaTime) {
			
			if (visibility)
			{
				if (!checkCollision())
				{
					fallSpeed += 0.005f;
					gemPos -= fallSpeed;
					//mTrans = Matrix4x4.mult(Matrix4x4.RotateZ(gemRotation), mTrans);
					gl.glPushMatrix();
					gl.glTranslatef(0, 0, gemPos);
					gl.glMultMatrixf(mTrans.toFloatArray(), 0);
				}
				else
				{
					//TODO: check ausrichtung, punkte, ...
					endFall();
				}
			}
		}
}

	

