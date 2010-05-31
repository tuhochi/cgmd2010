package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Background {
	
	Background()
	{
		
		initialized = false;
		
		float vertexData[] = {
				0.0f, 0.0f, -100.0f,
				0.0f, 100.0f, -100.0f,
				100.0f, 0.0f, -100.0f,
				100.0f, 100.0f, -100.0f
		};
		
		float uvData[] = {
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 1.0f,
				1.0f, 0.0f
		};
		byte indexData[] = {
				3, 1, 0, 
				0, 2, 3
		};
		
		ByteBuffer tBuf = ByteBuffer.allocateDirect( vertexData.length * 4 );
		tBuf.order( ByteOrder.nativeOrder() );
		vertices = tBuf.asFloatBuffer();
		vertices.put( vertexData );
		vertices.position( 0 );
		
		tBuf = ByteBuffer.allocateDirect( uvData.length * 4 );
		tBuf.order( ByteOrder.nativeOrder() );
		uvCoords = tBuf.asFloatBuffer();
		uvCoords.put( uvData );
		uvCoords.position( 0 );
		
		indices = ByteBuffer.wrap( indexData );
		indices.position( 0 );
	}
	
	void draw( Context context, GL10 renderContext )
	{
		
		if ( !initialized )
		{
			
			initialized = true;
			
			textureID = new int[ 1 ];
			renderContext.glGenTextures( 1, textureID, 0);
			
			InputStream imagestream = context.getResources().openRawResource( R.drawable.l22_background );
			Bitmap bitmap = BitmapFactory.decodeStream( imagestream );

			renderContext.glBindTexture(GL10.GL_TEXTURE_2D, textureID[ 0 ] );
			renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			bitmap.recycle();
			
			try {
				imagestream.close();
				imagestream = null;
			} catch (IOException e) {
			}
		}
		
		renderContext.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		renderContext.glFrontFace(GL10.GL_CCW );
		
		renderContext.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices );
		
		renderContext.glActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glClientActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glEnable( GL10.GL_TEXTURE_2D );
		renderContext.glBindTexture( GL10.GL_TEXTURE_2D, textureID[ 0 ] );
		renderContext.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		renderContext.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvCoords );
		
		renderContext.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(), GL10.GL_UNSIGNED_BYTE, indices );
		
		renderContext.glActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glClientActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		renderContext.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	void changeSize( int width, int height )
	{
		
		float vertexData[] = {
				0.0f, 0.0f, -100.0f,
				0.0f, height, -100.0f,
				width, 0.0f, -100.0f,
				width, height, -100.0f
		};
		
		vertices.put( vertexData );
		vertices.position( 0 );
	}

	FloatBuffer vertices;
	FloatBuffer uvCoords;
	ByteBuffer indices;
	int[] textureID;
	
	boolean initialized;
}
