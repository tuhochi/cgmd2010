package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.util.Log;

/**
 * A vertex shaded cube.
 */
class Mesh
{
	
	boolean useHardwareBuffers=false;
	
    public Mesh()
    {
    }
    
    public void init(GL10 gl, Vector<Float> vertices, Vector<Float> texCoords, Vector<Short> indices)
    {	
    	float[] primVertices=new float[vertices.size()];
    	float[] primTexCoords=new float[texCoords.size()];
    	short[] primIndices=new short[indices.size()];
    	
    	for (int i=0; i<vertices.size(); i++) {
    		primVertices[i]=vertices.elementAt(i).floatValue();
    	}
    	
    	for (int i=0; i<texCoords.size(); i++) {
    		primTexCoords[i]=texCoords.elementAt(i).floatValue();
    	}
    	
    	for (int i=0; i<indices.size(); i++) {
    		primIndices[i]=indices.elementAt(i).shortValue();
    	}
    	
    	init(gl, primVertices, primTexCoords, primIndices);    
    }
    
    public void init(GL10 gl, float[] vertices, float[] texCoords, short[] indices)
    {
        // Buffers to be passed to gl*Pointer() functions
        // must be direct, i.e., they must be placed on the
        // native heap where the garbage collector cannot
        // move them.
        //
        // Buffers with multi-byte datatypes (e.g., short, int, float)
        // must have their byte order set to native order
    	
    	if (gl instanceof GL11) {
    		GL11 gl11=(GL11)gl;
    		if (MyOpenGLView.isExtensionSupported(gl11,"GL_ANDROID_vertex_buffer_object") ||
    				MyOpenGLView.isExtensionSupported(gl11,"GL_OES_vertex_buffer_object")) {
    			useHardwareBuffers=true;
    			Log.d("useHardwareBuffers", "true");
    		}
    	} else {
    		Log.d("useHardwareBuffers", "false");
    	}

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

        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length*2);
        ibb.order(ByteOrder.nativeOrder());
        mIndexBuffer = ibb.asShortBuffer();
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
    	if (mVertexBuffer!=null || mTexCoordBuffer!=null || mIndexBuffer!=null) {
	    	gl.glEnable(GL10.GL_TEXTURE_2D);
	        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    	
	        gl.glFrontFace(GL10.GL_CW);
	        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
	        gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.limit(), GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
    	}
    }

    private FloatBuffer   mVertexBuffer;
    private FloatBuffer	mTexCoordBuffer;
    private ShortBuffer  mIndexBuffer;
}
