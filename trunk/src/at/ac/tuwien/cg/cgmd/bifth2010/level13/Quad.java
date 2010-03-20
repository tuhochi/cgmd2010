package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Quad {

	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	
	boolean masking = false;
	
	private int[] textures = new int[2];
	
	private float vertices[] = {
			                     -1.0f,-1.0f, 0.0f,
			                     1.0f,-1.0f,0.0f,
			                     -1.0f,1.0f,0.0f,
			                     1.0f,1.0f,0.0f
													};
	
	private float texture[] = {1.0f, 1.0f,
							   0.0f, 1.0f,
							   1.0f, 0.0f,
							   0.0f, 0.0f
													};
	
	
	
	public Quad(){
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
		
		
		
	}
	
	
	
	public void draw(GL10 gl){
		
		if (masking){
			gl.glEnable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_DEPTH_TEST);
			gl.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ZERO);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			
			gl.glFrontFace(GL10.GL_CW);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}else {
			gl.glDisable(GL10.GL_BLEND);
			gl.glEnable(GL10.GL_DEPTH_TEST);
		}
		
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	
	
	public void loadGLTexture(GL10 gl, Context context){
		this.loadGLTexture(gl, context,false);
	}
	
	public void loadGLTexture(GL10 gl, Context context, boolean mask){
		InputStream is = context.getResources().openRawResource(R.drawable.l13_testimage);
		InputStream maskStream = null;
		
		masking = mask;
		
		if (mask)
			maskStream = context.getResources().openRawResource(R.drawable.l13_testmask);
		
		Bitmap bmp = null;
	    Bitmap maskImage = null;
		
		try{
			bmp = BitmapFactory.decodeStream(is);
			if (mask)
				maskImage = BitmapFactory.decodeStream(maskStream);
		}finally{
			try{
				is.close();
				is = null;
				if(mask){
					maskStream.close();
					maskStream = null;
				}	
				
			}catch(IOException e){
				//Bad ... some error should be generated here
			}
			
			
			
		}
		
		if (mask)
			gl.glGenTextures(2, textures,0);
		else
			gl.glGenTextures(1, textures,0);
		
			
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		
		
		if(mask){
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[1]);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
			
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, maskImage, 0);	
			
			maskImage.recycle();
		}
		
		
		bmp.recycle();
		
	}
	
	
	
	
	
	
	
	
	
}
