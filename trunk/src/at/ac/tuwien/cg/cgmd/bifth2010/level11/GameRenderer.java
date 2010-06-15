package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
/**
 * renders the level
 * @author Fennes
 *
 */
public class GameRenderer implements Renderer {
	private static final String LOG_TAG = GameRenderer.class.getSimpleName();
    /**
     * screen width
     */
    public float _width = 480.0f;
    /**
     * screen height
     */
    public float _height = 320.0f;
    /**
     * android context
     */
    public Context context;
    /**
     * level to be rendered
     */
	public Level level;
	
	public GameRenderer(Context context, Vector2 resolution) {
		//Log.i(LOG_TAG, "GameRenderer()");
		_width = resolution.x;
		_height = resolution.y;
		this.context = context;
		this.level = ((GameActivity) context).getLevel();
		
	}

	
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Log.i(LOG_TAG, "onSurfaceCreated()");
        
        this.level.init(gl, this.context);

        // setup OpenGL
        gl.glMatrixMode(GL10.GL_PROJECTION);
        
        gl.glOrthof(0.0f, _width, 0.0f, _height, -1.0f, 100.0f);
        gl.glViewport(0, 0, (int) _width, (int) _height);
        gl.glScalef(this._width/Level.sizeX, (this._width/Level.sizeX), 1.0f);
        //gl.glScalef(this._width/Level.sizeX, this._height/Level.sizeY, 1.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
        // define the color we want to be displayed as the "clipping wall"
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // enable the differentiation of which side may be visible 
        gl.glEnable(GL10.GL_CULL_FACE);
        // which is the front? the one which is drawn counter clockwise
        gl.glFrontFace(GL10.GL_CCW);
        // which one should NOT be drawn
        gl.glCullFace(GL10.GL_BACK);
        
        //gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping 
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); //sets the clear color
 
    }

	@Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
		//Log.i(LOG_TAG, "onSurfaceChanged()");
		//do nothing
    }

    @Override
    public void onDrawFrame(GL10 gl) {
		//Log.i(LOG_TAG, "onDrawFrame()");
		//Clear Screen And Depth Buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//Reset The Current Modelview Matrix
		gl.glLoadIdentity();					
		this.level.draw(gl);
    }
}
