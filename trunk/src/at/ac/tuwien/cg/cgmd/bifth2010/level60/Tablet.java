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
	private int rotation_angle;
	private float scale_x;
	private float scale_y;
	private static float mapOffset_x;
	private static float mapOffset_y;
	
//	Bitmap bmp;
//	ByteBuffer bb;
	
	public Tablet(Context context, int width, int height, int x, int y, int texture, GL10 gl) {
		this.context = context;
		
		mapOffset_x = mapOffset_y = 0;
		
		vertices[1] = height;
		vertices[6] = width;
		vertices[9] = width;
		vertices[10] = height;
		
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.texture = texture;
		scale_x = 1;
		scale_y = 1;
		
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
		
//		gl.glGenTextures(1, texture);
//		
//		bmp = BitmapFactory.decodeResource(context.getResources(), resource_id);
//		bb = extract(bmp);
	}
	
	public static void addMapOffset(float x, float y) {
		mapOffset_x -= x;
		mapOffset_y -= y;
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
	
	
	
	public void scale(int scale_x, int scale_y) {
		this.scale_x = scale_x;
		this.scale_y = scale_y;
	}
	
	public void rotate(int angle) {
		this.rotation_angle = angle;
	}
	
	public void draw(GL10 gl) {
	//	gl.glGenTextures(1, texture);
		
	//	Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource_id);
//		ByteBuffer bb = extract(bmp);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
//		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(), bmp.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb); 
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
//		
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glPushMatrix();
		gl.glTranslatef(x+mapOffset_x, y+mapOffset_y, 0);
		gl.glScalef(scale_x, scale_y, 0);
		gl.glRotatex(rotation_angle, 0, 0, 1);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
//	private static ByteBuffer extract(Bitmap bmp) {
//		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
//		bb.order(ByteOrder.BIG_ENDIAN);
//		IntBuffer ib = bb.asIntBuffer();
//		// Convert ARGB -> RGBA
//		for (int y = bmp.getHeight() - 1; y > -1; y--)	{
//			for (int x = 0; x < bmp.getWidth(); x++) {
//				int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);
//				int alpha = ((pix >> 24) & 0xFF);
//				int red = ((pix >> 16) & 0xFF);
//				int green = ((pix >> 8) & 0xFF);
//				int blue = ((pix) & 0xFF);
//
//				ib.put(red << 24 | green << 16 | blue << 8 | alpha);
//			}
//		}
//		bb.position(0);
//		return bb; 
//	}
}
