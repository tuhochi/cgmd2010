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
	
	private int mVertBufferIndex;
    private int mIndexBufferIndex;
    private int mTextureCoordBufferIndex;
    
    private FloatBuffer   mVertexBuffer;
    private FloatBuffer	mTexCoordBuffer;
    private ShortBuffer  mIndexBuffer;
	
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
    
    public void init(GL10 gl, FloatBuffer _mVertexBuffer, FloatBuffer _mTexCoordBuffer, ShortBuffer  _mIndexBuffer) {
    	mVertexBuffer=_mVertexBuffer;
    	mTexCoordBuffer=_mTexCoordBuffer;
    	mIndexBuffer=_mIndexBuffer;
    	
    	mVertexBuffer.position(0);
    	mTexCoordBuffer.position(0);
    	mIndexBuffer.position(0);
    	
    	/*if (gl instanceof GL11 && false) {
    		GL11 gl11=(GL11)gl;
    		if (MyOpenGLView.isExtensionSupported(gl11,"vertex_buffer_object")) {
    			
                int[] buffer = new int[1];
                
                // Allocate and fill the vertex buffer.
                gl11.glGenBuffers(1, buffer, 0);
                mVertBufferIndex = buffer[0];
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
                final int vertexSize = mVertexBuffer.capacity()*4; 
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexSize, 
                        mVertexBuffer, GL11.GL_STATIC_DRAW);
                
                
                // Allocate and fill the texture coordinate buffer.
                gl11.glGenBuffers(1, buffer, 0);
                mTextureCoordBufferIndex = buffer[0];
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 
                        mTextureCoordBufferIndex);
                final int texCoordSize = mTexCoordBuffer.capacity()*4;
                gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texCoordSize, 
                        mTexCoordBuffer, GL11.GL_STATIC_DRAW);

                
                // Unbind the array buffer.
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
                
                // Allocate and fill the index buffer.
                gl11.glGenBuffers(1, buffer, 0);
                mIndexBufferIndex = buffer[0];
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 
                        mIndexBufferIndex);
                // A char is 2 bytes.
                final int indexSize = mIndexBuffer.capacity()*2;
                gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize, mIndexBuffer, 
                        GL11.GL_STATIC_DRAW);
                
                // Unbind the element array buffer.
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    			
    			
    			useHardwareBuffers=true;
    		} else {
    			useHardwareBuffers=false;
    		}
    	} else {
    		useHardwareBuffers=false;
    	}*/
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
        
        //init(gl, mVertexBuffer, mTexCoordBuffer, mIndexBuffer);
    }

    public void draw(GL10 gl)
    {
    	if (mVertexBuffer!=null || mTexCoordBuffer!=null || mIndexBuffer!=null) {
    		if (useHardwareBuffers) {	// vbo
    			/*GL11 gl11 = (GL11)gl;
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mVertBufferIndex);
                gl11.glVertexPointer(3, GL10.GL_FLOAT, 0, 0);
                
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, mTextureCoordBufferIndex);
    	        gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
                
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferIndex);
                gl11.glDrawElements(GL11.GL_TRIANGLES, mIndexBuffer.capacity(),
                        GL11.GL_UNSIGNED_SHORT, 0);
                
                gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
                gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);*/
    		} else {	// fall-back: vertex arrays
    			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	        
		        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer);
		        gl.glDrawElements(GL10.GL_TRIANGLES, mIndexBuffer.capacity(), GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		        //gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mIndexBuffer.capacity());
    		}
    	}
    }
}
