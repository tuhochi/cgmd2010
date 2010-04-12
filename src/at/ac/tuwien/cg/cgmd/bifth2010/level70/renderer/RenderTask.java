package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.ArrayList;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GlTexture;

/**
 * @author herrjohann
 */
public class RenderTask implements Renderer {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	GameScene scene; //< Game scene
	GlTexture tex;
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Ctor.
	 * @param The game scene.
	 */
	public RenderTask(GameScene scene) {
		this.scene = scene;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * OpenGL initialization.
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		/*
		 * EDIT BY PONTOMEDON, UNCOMMENT THE FOLLOWING LINE!
		 * 
		 * missing resource 'l70_spritetest' caused compile error!
		 */
//		tex = new GlTexture(R.drawable.l70_spritetest);
	}
	
	
	/**
	 * Surface changed.
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	
	/**
	 * Draw.
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glTranslatef(0.01f, 0, 0);
		//gl.glLoadIdentity();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glColor4f(0.8f, 0.5f, 0.0f, 1.0f);
		synchronized(scene) {
			try {
				scene.wait();
			}
			catch(InterruptedException e) {
				
			}
			gl.glActiveTexture(0);
			tex.bind();
			ArrayList<Geometry> geoms = scene.getGeometry();
			for (Geometry it : geoms) {
				it.draw(gl);
			}
		}
		
		Log.i("Renderer", "onDrawFrame");
	}	
}
