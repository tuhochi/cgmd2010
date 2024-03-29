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

/**
 * TextureManager for level 60. This class loads all needed textures for the game.
 * 
 * @author      Martin Schenk
 * @author      Tiare Feuchtner
 */
public class textureManager {
	GL10 gl;
	Context context;
	IntBuffer texture = IntBuffer.allocate(1);
	Bitmap bmp;
	ByteBuffer bb;
	Hashtable <String, Integer> textureMap;
	Hashtable <String, Tablet> gameObjectMap;
	
	/**
	 * Constructor for the TextureManager. Loads all textures for later use.
	 * @param context
	 * @param gl
	 */
	public textureManager(Context context, GL10 gl) {
		this.gl = gl;
		this.context = context;
		textureMap = new Hashtable <String, Integer>();
		gameObjectMap = new Hashtable <String, Tablet>();
		
		//add all texture elements
		createTexture("control", R.drawable.l60_arrows, LevelRenderer.CONTROL_SIZE, LevelRenderer.CONTROL_SIZE, true);
		
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
		
		createTexture("cop_front_l", R.drawable.l60_cop_front_l, LevelRenderer.COP_WIDTH, LevelRenderer.COP_HEIGHT);
		createTexture("cop_front_r", R.drawable.l60_cop_front_r, LevelRenderer.COP_WIDTH, LevelRenderer.COP_HEIGHT);
		createTexture("cop_back_l", R.drawable.l60_cop_back_l, LevelRenderer.COP_WIDTH, LevelRenderer.COP_HEIGHT);
		createTexture("cop_back_r", R.drawable.l60_cop_back_r, LevelRenderer.COP_WIDTH, LevelRenderer.COP_HEIGHT);
		
		createTexture("bunny_front_l", R.drawable.l60_bunny_front_l, LevelRenderer.BUNNY_WIDTH, LevelRenderer.BUNNY_HEIGHT);
		createTexture("bunny_back_l", R.drawable.l60_bunny_back_l, LevelRenderer.BUNNY_WIDTH, LevelRenderer.BUNNY_HEIGHT);
		createTexture("bunny_front_r", R.drawable.l60_bunny_front_r, LevelRenderer.BUNNY_WIDTH, LevelRenderer.BUNNY_HEIGHT);
		createTexture("bunny_back_r", R.drawable.l60_bunny_back_r, LevelRenderer.BUNNY_WIDTH, LevelRenderer.BUNNY_HEIGHT);
		createTexture("streetHor", R.drawable.l60_street_hor, 100, 100);
		createTexture("streetVer", R.drawable.l60_street_ver, 100, 100);
		createTexture("intersection", R.drawable.l60_intersection, 100, 100);
		createTexture("TintersectionTop", R.drawable.l60_t_intersect_top, 100, 100);
		createTexture("TintersectionBottom", R.drawable.l60_t_intersect_bottom, 100, 100);
		createTexture("TintersectionLeft", R.drawable.l60_t_intersect_left, 100, 100);
		createTexture("TintersectionRight", R.drawable.l60_t_intersect_right, 100, 100);
		createTexture("houseDoor1", R.drawable.l60_house1, 100, 100);
		createTexture("houseDoor2", R.drawable.l60_house2, 100, 100);
		
		createTexture("houseWall", R.drawable.l60_wall, 100, 100);
		createTexture("houseSpray", R.drawable.l60_wall_sprayed, 100, 100);
		createTexture("houseGlass", R.drawable.l60_glass, 100, 100);
		createTexture("houseBreak", R.drawable.l60_glass_broken, 100, 100);
		
		loadTexture("spraytag", R.drawable.l60_spraytag);
		
		createTexture("cloud1", R.drawable.l60_cloud1, 50, 50);
		createTexture("cloud2", R.drawable.l60_cloud2, 50, 50);
		createTexture("cloud3", R.drawable.l60_cloud3, 50, 50);
		
		loadTexture("car0", R.drawable.l60_car0);
		loadTexture("car1", R.drawable.l60_car1);
		
		loadTexture("blow0", R.drawable.l60_blow0);
		loadTexture("blow1", R.drawable.l60_blow1);
		loadTexture("blow2", R.drawable.l60_blow2);
		loadTexture("blow3", R.drawable.l60_blow3);
		loadTexture("blow4", R.drawable.l60_blow4);
		
		createTexture("win1", R.drawable.l60_win1, 200, 150, true);
		createTexture("win2", R.drawable.l60_win2, 200, 150, true);
		createTexture("lose1", R.drawable.l60_gameover1, 200, 150, true);
		createTexture("lose2", R.drawable.l60_gameover2, 200, 150, true);
	}
	
	/**
	 * Creates a sticky tablet with the specified texture.
	 * @param name
	 * @param texId
	 * @param xSize
	 * @param ySize
	 * @param sticky
	 */
	public void createTexture(String name, int texId, int xSize, int ySize, boolean sticky) {
		loadTexture (name, texId);
		Tablet tex = new Tablet(xSize, ySize, 0, 0, texture.get(0), sticky);
		
		if (!gameObjectMap.containsKey(name)) {
			gameObjectMap.put(name, tex);
		}
	}
	
	/**
	 *  Creates a non-sticky tablet with the specified texture.
	 * @param name
	 * @param texId
	 * @param xSize
	 * @param ySize
	 */
	public void createTexture (String name, int texId, int xSize, int ySize) {
		createTexture(name, texId, xSize, ySize, false);
	}
	
	/**
	 * Create a texture from an image file.
	 * @param name
	 * @param texId
	 */
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
	
	/**
	 * Returns the texture with the name specified.
	 * @param name
	 * @return
	 */
	public int getTexture (String name) {
		if (textureMap.containsKey(name))
			return (Integer)textureMap.get(name); //?????
		else
			return -1;
	}
	
	/**
	 * Returns the tablet with the name specified
	 * @param name
	 * @return
	 */
	public Tablet getGameObject (String name) {
		return (Tablet)gameObjectMap.get(name);
	}
}