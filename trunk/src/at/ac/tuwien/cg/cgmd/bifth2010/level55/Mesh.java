package wk.games.freeze;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * A vertex shaded cube.
 */
class Mesh
{
    public Mesh()
    {
    }
    
    public void init(float[] vertices, float[] texCoords, byte[] indices)
    {
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
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTexCoordBuffer = tbb.asFloatBuffer();
        mTexCoordBuffer.put(texCoords);
        mTexCoordBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
    	gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.limit(), GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
    }

    private FloatBuffer   mVertexBuffer;
    private FloatBuffer	mTexCoordBuffer;
    private ByteBuffer  mIndexBuffer;
}
