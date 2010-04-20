package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import static android.opengl.GLES10.glGetIntegerv;
import static android.opengl.GLES11.glGetFloatv;
import static android.opengl.GLU.gluUnProject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.*;

import android.opengl.GLU;

/**
 * A vertex shaded cube.
 */
class Point
{
    private FloatBuffer   mVertexBuffer;
    private FloatBuffer   mNormalBuffer;
    private FloatBuffer	  mColorBuffer;
    private FloatBuffer   mTextureBuffer;
    private ByteBuffer    mIndexBuffer;
    private float[] vertices;
    private GL10 gl;
   
    public Point(GL10 gl)
    {
    	this.gl = gl;
    	int one = 0x10000;
    	this.vertices = new float[4]; 
    	this.vertices[0] = 0.0f;
    	this.vertices[1] = 0.0f;
    	this.vertices[2] = -1.0f;
    	this.vertices[3] = 1.0f;
//        float[] vertices = {
//        		0.0f, 0.0f, -1.0f, 1.0f
//        };
        
//        float normals[] =  {
//        		0.0f, 0.0f, -1.0f, 
//        		0.0f, 0.0f, -1.0f,
//        		0.0f, 0.0f, -1.0f, 
//        		0.0f, 0.0f, -1.0f,
//        };
//        
//        float textureCoords[] = {
//        		0.0f, 0.0f, 
//        		1.0f, 0.0f, 
//        		0.0f, 1.0f, 
//        		1.0f, 1.0f
//        };
//        
//        float colors[] = {
//                0.0f, 0.0f, 0.0f, 1.0f,
//                1.0f, 0.0f, 0.0f, 1.0f,
//                1.0f, 1.0f, 0.0f, 1.0f,
//                0.0f, 1.0f, 0.0f, 1.0f
//        };
    	
    	float colors[] = {
    		  1.0f, 0.0f, 0.0f, 1.0f,
    	};
        
        byte indices[] = {
        		0
        };

        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
//        
//        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
//        nbb.order(ByteOrder.nativeOrder());
//        mNormalBuffer = nbb.asFloatBuffer();
//        mNormalBuffer.put(normals);
//        mNormalBuffer.position(0);
//     
//        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length*4);
//        tbb.order(ByteOrder.nativeOrder());
//        mTextureBuffer = tbb.asFloatBuffer();
//        mTextureBuffer.put(textureCoords);
//        mTextureBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }
    
	public float[] getScreenToWorldCoords(float x, float y, float z) {
		float[] modelView = new float[16];
        float[] projection = new float[16];
        int[] viewport = new int[4];
        float[] obj = new float[3];
        
        
//        GL11 gl11 = (GL11) this.gl;

//        gl11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projection, 0);
//        gl11.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
//        gluUnProject(x, viewport[4]-y, z, modelView, 0, projection, 0, viewport, 0, obj, 0);
        return obj;
	}
    
    public void setXY(float x, float y) {
    	float[] coords = getScreenToWorldCoords(x, y, 0.0f);
    	this.vertices[0] = coords[0];
    	this.vertices[1] = coords[1];
        ByteBuffer vbb = ByteBuffer.allocateDirect(this.vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(this.vertices);
        mVertexBuffer.position(0);
    }
    
    public float getX() {
    	return this.vertices[0];
    }
    
    public float getY() {
    	return this.vertices[1];
    }
    
    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(4,GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        //gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        //gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        gl.glDrawElements(gl.GL_POINTS, 1, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}
