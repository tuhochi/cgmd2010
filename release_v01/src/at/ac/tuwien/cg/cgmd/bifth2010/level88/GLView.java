package at.ac.tuwien.cg.cgmd.bifth2010.level88;


import android.content.Context;
import android.opengl.GLSurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.game.Game;

/**
 * Class for the general viewing context of android
 * @author Asperger, Radax
 */
public class GLView extends GLSurfaceView {
	/** Rendering instance of the level*/
	private GLRenderer renderer;
	/** Game instance of the level*/
	private Game game;

    /**
     * Constructor
     * @param context context of android
     * @param _game The game
     */
    public GLView(Context context, Game _game) 
    {
         super(context);                
     
         game = _game;
        
         renderer = new GLRenderer(game); 
         setRenderer(renderer);
    }
}
