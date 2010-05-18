package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Texture;

/**
 * 
 * @author arthur/sebastian (group 13)
 *
 */
public class TextureSingletons {
	/**
	 * reference to all used textures
	 */
	private static Map<String, Texture> textures = new HashMap<String, Texture>();
	
	/**
	 * creates all used textures
	 * @param gl
	 * @param context
	 */
	public static void initTextures(GL10 gl, Context context) {
		//only create textures once
		if(textures.size() != 0) {
			return;
		}
		textures.put(PlayerObject.class.getSimpleName(), new PlayerTexture(gl, context));
		textures.put(BackgroundObject.class.getSimpleName(), new BackgroundTexture(gl, context));
		textures.put(BeerObject.class.getSimpleName(), new BeerTexture(gl, context));
		textures.put(CopObject.class.getSimpleName(), new CopTexture(gl, context));
		textures.put(MistressObject.class.getSimpleName(), new MistressTexture(gl, context));
		textures.put(DrunkBar.class.getSimpleName(), new DrunkBarTexture(gl, context));
		textures.put(JailBar.class.getSimpleName(), new JailBarTexture(gl, context));
	}
	
	/**
	 * returns the texture for a game-object (specified via game-object-class)
	 * @param className
	 * @return
	 */
	public static Texture getTexture(String className) {
		if(textures.containsKey(className)) {
			return textures.get(className);
		}
		else {
			return null;
		}
	}
	
	public static void reset() {
		textures = new HashMap<String, Texture>();
	}
}
