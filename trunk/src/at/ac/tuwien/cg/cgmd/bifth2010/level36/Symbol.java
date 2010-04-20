package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.*;

/**
 * A vertex shaded cube.
 */
class Symbol
{
    private FloatBuffer   mVertexBuffer;
    private FloatBuffer   mNormalBuffer;
    private FloatBuffer	  mColorBuffer;
    private FloatBuffer   mTextureBuffer;
    private ByteBuffer    mIndexBuffer;
    private float[] vertices;
    private float[] textureCoords;
    private GL10 gl;
	
    float rowFactor = 1.0f/16.0f;
    float colFactor = 1.0f/16.0f;
	
    public Symbol()
    {
    	this.vertices = new float[16];
    	this.textureCoords = new float[8];
    	
    	this.vertices[0] = -1.0f;
    	this.vertices[1] = -1.0f;
    	this.vertices[2] = -0.5f; 
    	this.vertices[3] = +1.0f;
    	
    	this.vertices[4] = -1.0f;
    	this.vertices[5] = +1.0f;
    	this.vertices[6] = -0.5f;
    	this.vertices[7] = +1.0f;
    	
    	this.vertices[8] = +1.0f;
    	this.vertices[9] = -1.0f;
    	this.vertices[10] = -0.5f;
    	this.vertices[11] = +1.0f;
    	
    	this.vertices[12] = +1.0f;
    	this.vertices[13] = +1.0f;
    	this.vertices[14] = -0.5f;
    	this.vertices[15] = +1.0f;
        
        float normals[] =  {
        		0.0f, 0.0f, -1.0f, 
        		0.0f, 0.0f, -1.0f,
        		0.0f, 0.0f, -1.0f, 
        		0.0f, 0.0f, -1.0f,
        };
        
        //  11------10
        //   |       |
        //   |       |
        //  01------00
  
        //links oben
        textureCoords[0] = 0.0625f; //width2 rechts
        textureCoords[1] = 0.125f; //height1 unten
        //rechts oben
        textureCoords[2] = 0.0625f; //width2 rechts
        textureCoords[3] = 0.061f; //height2 oben
        //links unten
        textureCoords[4] = 0.0f; //width1 links
        textureCoords[5] = 0.125f; //height1 unten
        //rechts unten
        textureCoords[6] = 0.0f; //width1 links
        textureCoords[7] = 0.061f;//height2 oben

        float colors[] = {
                0.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f
        };
        
        byte indices[] = {
        		0, 2, 1,
        		2, 3, 1
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
        
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());
        mNormalBuffer = nbb.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
     
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoords.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
        mTextureBuffer.put(textureCoords);
        mTextureBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }
     
    public void getNumber(int number) {
    	this.textureCoords[0] = 0.0f + (number+1)*colFactor;
    	this.textureCoords[2] = 0.0f + (number+1)*colFactor;
    	this.textureCoords[4] = 0.0f + number*colFactor;
    	this.textureCoords[6] = 0.0f + number*colFactor;
        ByteBuffer tbb = ByteBuffer.allocateDirect(this.textureCoords.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
        mTextureBuffer.put(this.textureCoords);
        mTextureBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(4,GL10.GL_FLOAT, 0, mVertexBuffer);
        //gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
	        gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
	}
}

