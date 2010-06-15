package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import javax.microedition.khronos.opengles.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.Integer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.opengl.GLUtils;

/** Manages the textures of the game, sets texture to active , loads the samples */
public class TextureManager {

	public static HashMap <Integer, Integer> mTextureMap = null; /** keys == texture sample ID, values == int identifying the loaded texture */
	private GL10 mGl; /** opengl context */
	private Context mContext; /** acitvity context */
	private static TextureManager singletonObject = null; /** singleton of the texture manager */
	private int mTextures[] = null; /**the texture array for opengl */
	
	/** sets the opengl context */
	public void initializeGL(GL10 gl){		
		mGl = gl;
	}
	
	/** constructor, initializes the texture hashmap */
	private TextureManager() {
		mTextureMap = new HashMap<Integer, Integer>();
	}
	
	/** sets the activity context */
	public void initializeContext( Context context ){
		mContext = context;
	}
	
	/** returns, and creats the first time, the singleton */
	public static TextureManager getSingletonObject() {
		if (singletonObject == null) {
			singletonObject = new TextureManager();
		}
		return singletonObject;
	}
	
	
	/**
	 * Add a resource to the textureFile array
	 * @param resource
	 */
	public void add(int resID) {	
		if( mTextureMap.containsKey(resID)) return;
		mTextureMap.put(resID, 0 );
	}
	
	/**
	 * Load all textures and put them into the textureMap
	 */
	public void loadTextures() {
		Set<Integer> keys = mTextureMap.keySet();
		mTextures = new int[ keys.size() ];
		int counter = 0;
		mGl.glGenTextures(keys.size(), mTextures, 0);
		if( keys.isEmpty() ) return;
		Iterator<Integer> keysiter = keys.iterator();
		do{
			int resID = keysiter.next();
			InputStream is = mContext.getResources().openRawResource(resID);
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(is);	
			} finally {
				try {
					is.close();
				} catch (IOException e) {
				}
			}		
			mGl.glBindTexture(GL10.GL_TEXTURE_2D, counter);
			mGl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
			mGl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	        bitmap.recycle();
			mTextureMap.put(resID, counter);
			counter++;
		} while( keysiter.hasNext());	
	}
	
	/**
	 * sets active texture according to id
	 * @param id
	 */
	public void setTexture(int resID) {
		if( mTextureMap == null ) System.out.println("mTEXTUREMAP == NULL ");
		if( mTextureMap.containsKey(resID) == false){
			System.out.println("Key not found! key:"+resID);
			return;
		}
		int val = mTextureMap.get(resID);
		if( val == -1 ) return;
		mGl.glBindTexture(GL10.GL_TEXTURE_2D, val);
	}
	
}

