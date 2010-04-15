package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A vertex shaded cube.
 */
class Plane_Running
{
    public Plane_Running()
    {
    	int one = 0x10000;
        float vertices[] = {
        		-5.0f, -5.0f, 0.0f, 1.0f,
        		-5.0f, +5.0f, 0.0f, 1.0f,
        		+5.0f, -5.0f, 0.0f,	1.0f,
        		+5.0f, +5.0f, 0.0f, 1.0f
        };
        
        float normals[] =  {
        		0.0f, 0.0f, -1.0f,
        		0.0f, 0.0f, -1.0f,
        		0.0f, 0.0f, -1.0f,
        		0.0f, 0.0f, -1.0f,	
        };
        
        int textures[] = {
        		0, 0,
        		1, 0,
        		0, 1,
        		1, 1
        };
        
        int colors[] = {
                0, 0, 0, one,
                one, 0, 0, one,
                one, one, 0, one,
                0, one, 0, one
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
        
//        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
//        cbb.order(ByteOrder.nativeOrder());
//        mColorBuffer = cbb.asIntBuffer();
//        mColorBuffer.put(colors);
//        mColorBuffer.position(0);
        
//        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*3);
//        nbb.order(ByteOrder.nativeOrder());
//        mNormalBuffer = nbb.asFloatBuffer();
//        mNormalBuffer.put(normals);
//        mNormalBuffer.position(0);
      
//        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*2);
//        vbb.order(ByteOrder.nativeOrder());
//        mTextureBuffer = tbb.asIntBuffer();
//        mTextureBuffer.put(textures);
//        mTextureBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(4, gl.GL_FLOAT, 0, mVertexBuffer);
        //gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
        //gl.glNormalPointer(gl.GL_FLOAT, 0, mNormalBuffer);
        //gl.glTexCoordPointer(2, gl.GL_FIXED, 0, mTextureBuffer);
        gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

    private FloatBuffer   mVertexBuffer;
    private FloatBuffer   mNormalBuffer;
    private IntBuffer	mColorBuffer;
    private IntBuffer   mTextureBuffer;
    private ByteBuffer  mIndexBuffer;
}
