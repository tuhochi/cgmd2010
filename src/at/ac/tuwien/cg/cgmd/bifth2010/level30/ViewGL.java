package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import javax.microedition.khronos.opengles.GL;

import android.content.Context;
import android.opengl.GLSurfaceView;

import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector2;

public class ViewGL extends GLSurfaceView {
	
	private RendererGL renderer;
	private GameThread gameThread;
	private GameWorld gameWorld;
	
	
    public ViewGL(LevelActivity context, Vector2 size, GameWorld world) 
    {
    	super(context);    
        gameWorld = world;          
        renderer = new RendererGL(world);  
        setRenderer(renderer);
        
        gameThread = new GameThread(world);
        gameThread.start();   
    }     
	
	
    public void InputMove(Vector2 pos)
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
    }

	@Override
	public void onPause() {
		if(gameWorld != null)
			gameWorld.SetPause(true);
		super.onPause();
	}

	@Override
	public void onResume() {
		if(gameWorld != null)
			gameWorld.SetPause(false);
		super.onResume();
	}
	

	public void onStart() {
		if(gameThread != null && !gameThread.isAlive() && gameWorld != null)
			gameThread.start();
	}

	public void onStop() {
		if(gameThread != null && gameThread.isAlive())
			gameThread.stop();
	}
	
	


}
