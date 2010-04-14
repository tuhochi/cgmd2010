package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Square {
	

	int tex[]=new int[6];
	private IntBuffer texBuf=IntBuffer.wrap(tex);
	private static final int TEX_SIZE=40;
	Context context;
	
	// Our vertices.
	private float vertices[] = {
		       0.2f,  0.2f, 0.0f,  // 0, Top Left
		       0.2f,  0.8f, 0.0f,  // 1, Bottom Left
		       0.8f,  0.8f, 0.0f,  // 2, Bottom Right
		       0.8f,  0.2f, 0.0f  // 3, Top Right
		};
	
	private float texcoord[] = {
		       0.0f,  0.0f,
		       0.0f,  1.0f,
		       1.0f,  1.0f,
		       1.0f,  0.0f
		};

	// The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };

	// Our vertex buffer.
	private FloatBuffer vertexBuffer, texcoordBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;

	public Square(GL10 gl, Context context) {
		this.context = context;
		
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texcoord.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texcoordBuffer = tbb.asFloatBuffer();
		texcoordBuffer.put(texcoord);
		texcoordBuffer.position(0);
		
		gl.glGenTextures(1, texBuf);
		LoadTexture(0,R.drawable.l50_icon,gl,context);
	}

	/**
	 * This function draws our square on screen.
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// Counter-clockwise winding.
		//gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		//gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		//gl.glCullFace(GL10.GL_BACK);

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);

		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texBuf.get(0));      
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT,0, texcoordBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);

		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	private void LoadTexture(int num, int id, GL10 gl, Context context)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texBuf.get(num));				
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);		
		int pixels[]=new int[TEX_SIZE*TEX_SIZE];
		bmp.getPixels(pixels, 0, TEX_SIZE, 0, 0, TEX_SIZE, TEX_SIZE);
		int pix1[]=new int[TEX_SIZE*TEX_SIZE];
		for(int i=0; i<TEX_SIZE; i++)
        {
             for(int j=0; j<TEX_SIZE; j++)
             {
                  //correction of R and B
                  int pix=pixels[i*TEX_SIZE+j];
                  int pb=(pix>>16)&0xff;
                  int pr=(pix<<16)&0x00ff0000;
                  int px1=(pix&0xff00ff00) | pr | pb;
                  //correction of rows
                  pix1[(TEX_SIZE-i-1)*TEX_SIZE+j]=px1;
             }
        }     
		
		IntBuffer tbuf=IntBuffer.wrap(pix1);
		tbuf.position(0);		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, TEX_SIZE, TEX_SIZE, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, tbuf);		
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );											
	}

}
