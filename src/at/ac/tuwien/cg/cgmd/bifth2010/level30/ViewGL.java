package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import javax.microedition.khronos.opengles.GL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector2;


/**
 * Implements GLSurfaceView
 */
public class ViewGL extends GLSurfaceView {
	
	private RendererGL renderer;
	private GameWorld gameWorld;
	
	/**
	 * Creates a new GlView
	 * @param context The Activity context
	 * @param size The size of the GlView
	 * @param world The game world 
	 */
    public ViewGL(LevelActivity context, Vector2 size, GameWorld world) 
    {
    	super(context);    
        gameWorld = world;          
        renderer = new RendererGL(world);  
        setRenderer(renderer);
    }    	


    /**
     * For correct life cycle management. Override from GLSurfaceView.
     */
	@Override
	public void onPause() {
		if(gameWorld != null)
			gameWorld.SetPause(true);
		super.onPause();
		

	}

    /**
     * For correct life cycle management. Override from GLSurfaceView.
     */
	@Override
	public void onResume() {
		if(gameWorld != null)
			gameWorld.SetPause(false);
		super.onResume();
		

	}
	
    /**
     * For correct life cycle management. Start the thread.
     */
	public void onStart() {
		if(gameWorld != null && !gameWorld.isAlive())
			gameWorld.SetPause(false);
		

	}

    /**
     * For correct life cycle management. Stop the thread.
     */
	public void onStop() {
		if(gameWorld != null && gameWorld.isAlive())
			gameWorld.SetPause(true);
		

	}
	
	
    /*public void InputMove(Vector2 pos)
    {
    	gameWorld.InputMove(pos);
    }
    
	
    public void InputDown(Vector2 pos)
    {
    	gameWorld.InputDown(pos);
    }      

  
    public void InputUp(Vector2 pos)
    
    {
    	gameWorld.InputUp(pos);
    }*/


}
