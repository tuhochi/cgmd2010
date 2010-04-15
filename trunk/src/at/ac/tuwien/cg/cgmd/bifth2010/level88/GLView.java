package at.ac.tuwien.cg.cgmd.bifth2010.level88;


import android.content.Context;
import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.UpdateThread;

public class GLView extends GLSurfaceView {
	private GLRenderer renderer; 
	private Game game;

    public GLView(Context context, Game _game) 
    {
         super(context);                
     
         game = _game;
        
         renderer = new GLRenderer(game); 
         setRenderer(renderer);
    }
}
