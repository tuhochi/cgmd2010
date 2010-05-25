package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import java.util.Hashtable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
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
		
		createTexture("control", R.drawable.l83_hexagon_blue, 50, 50, true);
		createTexture("control", R.drawable.l83_hexagon, 100, 100, true);
		
		createTexture("gold", R.drawable.l00_coin, 20, 20, true);
		createTexture("zero", R.drawable.l60_gold_0, 20, 20, true);
		createTexture("one", R.drawable.l60_gold_1, 20, 20, true);
		createTexture("two", R.drawable.l60_gold_2, 20, 20, true);
		createTexture("three", R.drawable.l60_gold_3, 20, 20, true);
		createTexture("four", R.drawable.l60_gold_4, 20, 20, true);
		createTexture("five", R.drawable.l60_gold_5, 20, 20, true);
		createTexture("six", R.drawable.l60_gold_6, 20, 20, true);
		createTexture("seven", R.drawable.l60_gold_7, 20, 20, true);
		createTexture("eight", R.drawable.l60_gold_8, 20, 20, true);
		createTexture("nine", R.drawable.l60_gold_9, 20, 20, true);
		
		createTexture("cop", R.drawable.l60_cop_front_l, 50, 50);
		createTexture("bunny", R.drawable.l60_bunny_front, 60, 60);
		createTexture("streetHor", R.drawable.l60_street_hor, 100, 100);
		createTexture("streetVer", R.drawable.l60_street_ver, 100, 100);
		createTexture("intersection", R.drawable.l60_intersection, 100, 100);
		createTexture("TintersectionTop", R.drawable.l60_t_intersect_top, 100, 100);
		createTexture("TintersectionBottom", R.drawable.l60_t_intersect_bottom, 100, 100);
		createTexture("TintersectionLeft", R.drawable.l60_t_intersect_left, 100, 100);
		createTexture("TintersectionRight", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("smallHousefl", R.drawable.l60_smallhouse_fl, 100, 100);
		createTexture("smallHousefr", R.drawable.l60_smallhouse_fr, 100, 100);
		createTexture("smallHousebl", R.drawable.l60_smallhouse_bl, 100, 100);
		createTexture("smallHousebr", R.drawable.l60_smallhouse_br, 100, 100);
		createTexture("housefl", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("housefr", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("housebl", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("housebr", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("housecl", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("housecr", R.drawable.l60_t_intersect_right, 100, 100);
		loadTexture("spraytag", R.drawable.l60_spraytag);

		//add all texture elements
	}
	
	public void createTexture(String name, int texId, int xSize, int ySize, boolean sticky) {
		loadTexture (name, texId);
		Tablet tex = new Tablet(context, xSize, ySize, 0, 0, texture.get(0), sticky, gl);
		
		if (!gameObjectMap.containsKey(name)) {
			gameObjectMap.put(name, tex);
		}
	}
	
	public void createTexture (String name, int texId, int xSize, int ySize) {
		createTexture(name, texId, xSize, ySize, false);
	}
	
	
	public void loadTexture (String name, int texId) {
		gl.glGenTextures(1, texture);
		
		InputStream is = context.getResources().openRawResource(texId);
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {}
		is = null;
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.get(0));
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		bitmap.recycle();
		if (!textureMap.containsValue(texture.get(0))) {
			textureMap.put(name, texture.get(0));
		}
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