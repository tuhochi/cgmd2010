package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Quad {

	//Vertex and texure Coords
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	
	
	//Type of the object
	
	private String objectType;
	
	//Current x and y position of the quad
	
	private float x = 0;
	private float y = 0;
	
	//Movement speed
	
	private float speedX = 0;
	private float speedY = 0;
	
	
	private int[] textures = new int[1];
	
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
	
	public void setPos(float x, float y){
		this.x = x;
		this.y = y;
		
	}
	
	public float getSpeedX(){
		return this.speedX;
	}
	
	public float getSpeedY(){
		return this.speedY;
	}
	
	
	public void setSpeedX(float speedX){
		this.speedX = speedX;
	}
	
	public void setSpeedY(float speedY){
		this.speedY = speedY;
	}
	
	public void setObjectType(String type){
		
		this.objectType = type;	
	}
	
	public String getObjectType(){
		return this.objectType;
	}
	

	public float getMinX(){
		return this.x - 1.0f;	
	}
	
	public float getMaxX(){
		return this.x+1.0f;
	}
	
	public float getMinY(){
		return this.y-1.0f;
	}

	public float getMaxY(){
		return this.y+1.0f;
	}
	
	
	public float X(){
		return this.x;
	}
	
	public float Y(){
		return this.y;
	}
	
	
	public void update(){
		this.x += speedX;
		this.y += speedY;
	}
	
	
	public void draw(GL10 gl){
		
		gl.glLoadIdentity();
		gl.glTranslatef(x, y, -15.0f);
		
		gl.glEnable(GL10.GL_BLEND);
		
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length/3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_BLEND);
		
	
	}
	
	

	
	public void loadGLTexture(GL10 gl, Context context,int textureId){
		InputStream is = context.getResources().openRawResource(textureId);
		
		Bitmap bmp = null;
		
		try{
			bmp = BitmapFactory.decodeStream(is);
		
		}finally{
			try{
				is.close();
				is = null;
				
				
			}catch(IOException e){
				//Bad ... some error should be generated here
			}
			
			
			
		}
		
		
		gl.glGenTextures(1, textures,0);
		
			
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

		
		bmp.recycle();
		
	}
	
	
	
	
	
	
	
	
	
}
