/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

/**
 * The standard RenderView enabling OpenGLES 10. Handles all Render related tasks. 
 *
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class RenderView extends GLSurfaceView implements Renderer {


//	protected GameManager gameManager;

	protected float gameTime;
	
	/** The texture collection. (The ids increase themselves) */	 
	protected Hashtable<Integer, Integer> textures;
	
	// Textures.
	static final int[] TEXTURE_BUNNY = {R.drawable.l20_bunny1, 
										R.drawable.l20_bunny2,
										R.drawable.l20_bunny3,
										R.drawable.l20_bunny4};
	
	static final int TEXTURE_SHELF = 	R.drawable.l20_backg;
	static final int TEXTURE_CART = 	R.drawable.l20_shopping_cart;
	
	static final int[] TEXTURE_PRODUCTS = new int[]{
										R.drawable.l20_bananamilk,
										R.drawable.l20_beer,
										R.drawable.l20_broccoli,
										R.drawable.l20_cereal,
										R.drawable.l20_drink,
										R.drawable.l20_icecream,
										R.drawable.l20_lollipop,
										R.drawable.l20_penne,
										R.drawable.l20_pizza,
										R.drawable.l20_playboy,
										R.drawable.l20_probio,
										R.drawable.l20_soup,
										R.drawable.l20_yoghurt
										
										};
	

	/**
	 * @param context The render context
	 * @param attr The attribute set
	 */
	public RenderView(Context context, AttributeSet attr)
	{
		super(context, attr);
		
		setRenderer(this); 
        setFocusable(true);
        requestFocus();
		setFocusableInTouchMode(true);
		
		textures = new Hashtable<Integer, Integer>();
		gameTime = 60.f;
	}
	
	

	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {			

		//Settings
		gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
		gl.glClearDepthf(1);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_FLAT);
		
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glDisable(GL10.GL_DITHER);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);				
		//gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Really fast perspective calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		// Rendering via arrays
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		
		if (LevelActivity.gameManager.firstRun) {
			LevelActivity.gameManager.createEntities(gl);
			Log.d(getClass().getSimpleName(), "RenderView created");
		} else {
			LevelActivity.gameManager.reCreateEntities(gl);
			Log.d(getClass().getSimpleName(), "RenderView reCreated");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if(height == 0) {
			height = 1;
		}
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		// Use an Ortho projection
		gl.glOrthof(0, width, 0, height, -10, 100);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

	}
	
	
	/* (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		GameManager gameManager = LevelActivity.gameManager;
		
		// Update the GameManager first
		gameManager.update();
		
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();		
		
		// Render all objects individually. NOTE: No more stacked renders
		gameManager.shelf.render(gl);		

		// Render products.	
		Enumeration<Integer> keys = gameManager.shelf.products.keys();
		while(keys.hasMoreElements()) {
			gameManager.shelf.products.get(keys.nextElement()).render(gl);
		}	
		
		gameManager.shoppingCart.render(gl);
		gameManager.bunny.render(gl);

	}

	

//	/* (non-Javadoc)
//	 * @see android.opengl.GLSurfaceView#onPause()
//	 */
//	@Override
//	public void onPause() {
//		
//		super.onPause();
//	}



	
//	/* (non-Javadoc)
//	 * @see android.opengl.GLSurfaceView#onResume()
//	 */
//	@Override
//	public void onResume() {
//		super.onResume();		
//		timer.reset();
//	}



	

	


//	/* (non-Javadoc)
//	 * @see android.view.View#onKeyUp(int, android.view.KeyEvent)
//	 */
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		//
//		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//			gameManager.scrollSpeed += 20f;
//		
//			
//		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//			gameManager.scrollSpeed -= 20f;
//			
//			
//		} else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
//			
//			
//		} else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//			
//			
//		} else if(keyCode == KeyEvent.KEYCODE_BACK) {
//			
//			
//		} else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
//
//		}
//
//		//We handled the event
//		return true;
//	}
	
	

	
	
	/** 
	 * This method returns the texture id of the given resource file if already available or creates it on the fly. 
	 * Note: You have to pass a valid GL everytime you use this method. 
	 * 
	 * @param resource The resource identifier delivered by "at.ac.tuwien.cg.cgmd.bifth2010.R".
	 * @param gl Pass a valid GL here, or the texture id will always be 0. 
	 * @returns The texture id of the file.
	 */
	public int getTexture(int resource, GL10 gl) {
		
		// Try to find the texture
		Integer t = textures.get(resource);
		if (t != null) {
			return t;
		}
		
		// Texture not yet created. Try it now.		
		// If using the fast version, abort here and return zero. 
		if (gl == null)
			return 0;

		//Get the texture from the Android resource directory
		InputStream is = LevelActivity.instance.getResources().openRawResource(resource);
		Bitmap bitmap = null;
		
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}

		//Generate there texture pointer. (glGenTextures needs a pointer as input)
		int[] texture = new int[1];
		gl.glGenTextures(1, texture, 0);		

		//Create Linear Filtered Texture and bind it to texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
		
		if(gl instanceof GL11) {
			gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);							
		} 
//		else {
//			Toast.makeText(activity, "GL11 not supported", 2);
//		}		
			
		//Clean up
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
		
		textures.put(resource, texture[0]);
		return texture[0];	
	}
	
	/** 
	 * This method returns the texture id of the resource file if already available. Otherwise returns 0.
	 * Use "int getTexture(int resource, GL10 gl)" to create a save texture handle on the fly. 
	 * 
	 * @param resource The resource identifier delivered by "at.ac.tuwien.cg.cgmd.bifth2010.R".
	 * @returns The texture id of the file.
	 */
	public int getTexture(int resource) {
		return getTexture(resource, null);
	}
}
