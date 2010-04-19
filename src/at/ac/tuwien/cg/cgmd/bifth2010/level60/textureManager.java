package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import java.util.Hashtable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class textureManager {
	GL10 gl;
	Context context;
	IntBuffer texture = IntBuffer.allocate(1);
	Bitmap bmp;
	ByteBuffer bb;
	Hashtable <String, Integer> textureMap;
	Hashtable <String, Tablet> gameObjectMap;
	
	public textureManager(Context context, GL10 gl) {
		this.gl = gl;
		this.context = context;
		textureMap = new Hashtable <String, Integer>();
		gameObjectMap = new Hashtable <String, Tablet>();
		
		
		createTexture("cop", R.drawable.l60_cop_front_l, 50, 50);
		createTexture("bunny", R.drawable.l60_bunny_front, 60, 60);
		createTexture("streetHor", R.drawable.l60_street_hor, 100, 100);
//		createTexture("streetVer", R.drawable.l60_street_ver, 100, 100);
//		createTexture("intersection", R.drawable.l60_intersection, 100, 100);
//		createTexture("TintersectionTop", R.drawable.l60_t_intersect_top, 100, 100);
//		createTexture("TintersectionBottom", R.drawable.l60_t_intersect_bottom, 100, 100);
//		createTexture("TintersectionLeft", R.drawable.l60_t_intersect_left, 100, 100);
//		createTexture("TintersectionRight", R.drawable.l60_t_intersect_right, 100, 100);

		//add all texture elements
	}
	
	public void createTexture (String name, int texId, int xSize, int ySize) {
		loadTexture (name, texId);
		Tablet tex = new Tablet(context, xSize, ySize, 0, 0, texture.get(0), gl);
		
		if (!gameObjectMap.containsKey(name)) {
			gameObjectMap.put(name, tex);
		}
	}
	
	public void loadTexture (String name, int texId) {
		gl.glGenTextures(1, texture);
		
		bmp = BitmapFactory.decodeResource(context.getResources(), texId);
		bb = extract(bmp);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.get(0));
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(), bmp.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb); 
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		if (!textureMap.containsValue(texId)) {
			textureMap.put(name, texId);
		}
	}
	
	
	private static ByteBuffer extract(Bitmap bmp) {
		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
		bb.order(ByteOrder.BIG_ENDIAN);
		IntBuffer ib = bb.asIntBuffer();
		// Convert ARGB -> RGBA
		for (int y = bmp.getHeight() - 1; y > -1; y--)	{
			for (int x = 0; x < bmp.getWidth(); x++) {
				int pix = bmp.getPixel(x, bmp.getHeight() - y - 1);
				int alpha = ((pix >> 24) & 0xFF);
				int red = ((pix >> 16) & 0xFF);
				int green = ((pix >> 8) & 0xFF);
				int blue = ((pix) & 0xFF);

				ib.put(red << 24 | green << 16 | blue << 8 | alpha);
			}
		}
		bb.position(0);
		return bb; 
	}
	
	public int getTexture (String name) {
		if (textureMap.containsKey(name))
			return (Integer)textureMap.get(name); //?????
		else
			return -1;
	}
	
	public Tablet getGameObject (String name) {
		return (Tablet)gameObjectMap.get(name);
	}
}