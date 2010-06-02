package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Tablet {
	private float vertices[] = {
		      0.0f,  1.0f, 0.0f,
		      0.0f,  0.0f, 0.0f,
		      1.0f, 0.0f, 0.0f,
		      1.0f, 1.0f, 0.0f
		};
	private short[] indices = { 0, 1, 2, 0, 2, 3 };
	private float texCoords[] = {
			
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,
			1.0f, 0.0f

	};
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer texCoordBuffer;
//	IntBuffer texture = IntBuffer.allocate(1);
	private Context context;
	
	private int width;
	private int height;
	private float x;
	private float y;
	private int texture;
	private float rotation_angle;
	private static float scale = 1.0f;
	private static float mapOffset_x = 0;
	private static float mapOffset_y = 0;
	private boolean sticky = false;
	public static final int INTENDED_RES_X = 400;
	public static final int INTENDED_RES_Y = 520;
	
	public Tablet(Context context, int width, int height, float x, float y, int texture, GL10 gl) {
		this(context, width, height, x, y, texture, false, gl);
	}
	
	public Tablet(Context context, int width, int height, float x, float y, int texture, boolean sticky, GL10 gl) {
		this.context = context;
		this.sticky = sticky;
		
		vertices[1] = height;
		vertices[6] = width;
		vertices[9] = width;
		vertices[10] = height;
		
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.texture = texture;
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);	
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texCoordBuffer = tbb.asFloatBuffer();
		texCoordBuffer.put(texCoords);
		texCoordBuffer.position(0);
	}
	
	public static void addMapOffset(float x, float y) {
		mapOffset_x -= x;
		mapOffset_y -= y;
	}
	
	public static void setMapOffset(float x, float y) {
		mapOffset_x = x;
		mapOffset_y = y;
	}
	
	public static void setResolution(int width, int height) {
		if (width > height) Tablet.scale = (float)width/(float)INTENDED_RES_X;
		else Tablet.scale = (float)height/(float)INTENDED_RES_Y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void move(float xwise, float ywise) {
		x += xwise;
		y += ywise;
	}
	
	public void setXY(float newX, float newY) {
		x = newX;
		y = newY;
	}
	
	public void changeTexture(int texture) {
		this.texture = texture;
	}
	
	
	public void rotate(float angle) {
		this.rotation_angle = angle;
	}
	
	public void draw(GL10 gl) {
		float drawtoX, drawtoY;
		if (this.sticky) {
			drawtoX = x;
			drawtoY = y;
		} else {
			drawtoX = (x + mapOffset_x)*Tablet.scale;
			drawtoY = (y + mapOffset_y)*Tablet.scale;
		}
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);

		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glPushMatrix();
		gl.glTranslatef(drawtoX, drawtoY, 0);
		gl.glScalef(Tablet.scale, Tablet.scale, 0);
		gl.glRotatef(rotation_angle, 0, 0, 1);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
