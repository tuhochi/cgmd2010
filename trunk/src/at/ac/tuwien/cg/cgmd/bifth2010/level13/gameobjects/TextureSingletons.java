package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * 
 * @author group13
 * 
 * class managing all textures as singletons
 *
 */
public class TextureSingletons {
	/** reference to all used textures */
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	/**
	 * creates all used textures
	 * @param gl gl
	 * @param context context
	 */
	public static void initTextures(GL10 gl, Context context) {
		//only create textures once
		if(textures.size() != 0) {
			return;
		}
		//put all textures into hash map
		textures.put(PlayerObject.class.getSimpleName(), new PlayerTexture(gl, context));
		textures.put(BackgroundObject.class.getSimpleName(), new BackgroundTexture(gl, context));
		textures.put(BeerObject.class.getSimpleName(), new BeerTexture(gl, context));
		textures.put(CopObject.class.getSimpleName(), new CopTexture(gl, context));
		textures.put(MistressObject.class.getSimpleName(), new MistressTexture(gl, context));
		textures.put(DrunkBar.class.getSimpleName(), new DrunkBarTexture(gl, context));
		textures.put(JailBar.class.getSimpleName(), new JailBarTexture(gl, context));
		textures.put(DrunkBarBlock.class.getSimpleName(), new DrunkBarBlockTexture(gl, context));
		textures.put("censored", new CensoredTexture(gl, context));
	}
	
	/**
	 * returns the texture for a game-object (specified via game-object-class)
	 * @param className name of the class
	 * @return texture of class
	 */
	public static Texture getTexture(String className) {
		if(textures.containsKey(className)) {
			return textures.get(className);
		}
		else {
			return null;
		}
	}
	
	
	/**
	 * resets singleton object
	 */
	public static void reset() {
		textures = new HashMap<String, Texture>();
	}
}
