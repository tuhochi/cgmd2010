package at.ac.tuwien.cg.cgmd.bifth2010.level36.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.*;

/**
 * A vertex shaded cube.
 */
class Plane
{
    public Plane(int num)
    {
    	int one = 0x10000;
    	float z;
    	if (num == 1)
    		z = 0.0f;
    	else 
    		z = -0.2f;
    	
        float vertices[] = {
        		-5.0f, -5.0f, z, 1.0f,
        		-5.0f, +5.0f, z, 1.0f,
        		+5.0f, -5.0f, z, 1.0f,
        		+5.0f, +5.0f, z, 1.0f
        }; 
        
        float normals[] =  {
        		0.0f, 0.0f, -1.0f, 
        		0.0f, 0.0f, -1.0f,
        		0.0f, 0.0f, -1.0f, 
        		0.0f, 0.0f, -1.0f,
        };
        
//        float textureCoords[] = {
//        		0.0f, 0.0f, 1.0f, 1.0f,
//        		1.0f, 0.0f, 1.0f, 1.0f,
//        		0.0f, 1.0f, 1.0f, 1.0f,
//        		1.0f, 1.0f, 1.0f, 1.0f,
//        };
        
        float textureCoords[] = {
        		0.0f, 0.0f, 
        		1.0f, 0.0f, 
        		0.0f, 1.0f, 
        		1.0f, 1.0f
        };

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

    public void draw(GL10 gl)
    {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(4,GL10.GL_FLOAT, 0, mVertexBuffer);
        //gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

    private FloatBuffer   mVertexBuffer;
    private FloatBuffer   mNormalBuffer;
    private FloatBuffer	  mColorBuffer;
    private FloatBuffer   mTextureBuffer;
    private ByteBuffer    mIndexBuffer;
}
