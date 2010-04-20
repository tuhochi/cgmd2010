package at.ac.tuwien.cg.cgmd.bifth2010.level55;

//import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

/**
 * The Renderer
 * @author Wolfgang Knecht
 *
 */
class MyRenderer implements MyOpenGLView.Renderer {
	
	Level level;
	Player player;
	Camera camera;
	Interface ui;
	Texture interfaceTexture;
	static Context context;
	static float numTilesHorizontal=15.0f;
	static float numTilesVertical;
	
	static float resX;
	static float resY;
	
	/**
	 * Sets the Activity context
	 * @param _context The Activity context
	 */
	static public void setContext(Context _context) {
		context=_context;
	}
	
    /*public MyRenderer(Player _player) {
    	player=_player;
    }*/
	
	/**
	 * Constructor
	 */
	public MyRenderer() {
		super();
		
    }

    @Override
	public void onDrawFrame(GL10 gl) {
    	Timer.update();
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    	
    	
    	//Ich hab jetzt dT auf Sekunden umgerechnet, weils sonst verwirrend ist...
    	//Player updated früher weil sonst das Level um ein Frame nachhinkt.
    	float dt = Timer.dT*0.001f;
    	player.update(dt);
    	camera.update(dt);
    	
    	//Performs view transformation
    	camera.draw(gl);
    	
    	//Renders level + background layers
    	level.draw(gl);
    	
    	//Renders the player
    	player.draw(gl);
    	
    	//Renders the user interface
    	ui.draw(gl);	  
    }

    @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
    	Log.d("Renderer","onSurfaceChanged");
    	
    	Log.d("Renderer width", Integer.toString(width));
    	Log.d("Renderer height", Integer.toString(height));
    	
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */
         
         resX=width;
         resY=height;

         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         
         float ratio=(float)height/(float)width;
         
         gl.glOrthof(0.0f, numTilesHorizontal, numTilesHorizontal*ratio, 0.0f, 0.5f, 5.0f);
         //gl.glOrthof(-20f, 20, 20, -20.0f, 00.0f, 5.0f);
         
         numTilesVertical = numTilesHorizontal*ratio;
         
         Quad.screenWidth=numTilesHorizontal;
         Quad.screenHeight=numTilesVertical;
         
         ui.screenWidth=numTilesHorizontal;
         ui.screenHeight=numTilesVertical;
         
         TileLayer.screenWidth=numTilesHorizontal;
         TileLayer.screenHeight=numTilesVertical;
         
         gl.glMatrixMode(GL10.GL_MODELVIEW);
         gl.glLoadIdentity();
    }

    @Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	Log.d("Renderer","onSurfaceCreate");
    	
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

         gl.glClearColor(0.63f,0.74f,0.96f,0);
         
         gl.glDisable(GL10.GL_CULL_FACE);
         gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
         gl.glEnable(GL10.GL_BLEND);
         
         
    	
         //gl.glFrontFace(GL10.GL_CW);
         
         Texture.cleanUp();
         Texture.setGL(gl);
         
         
         
         level=new Level();
         level.init(gl, context);
         
         camera = new Camera(level);
         
         player=new Player();
         player.init(gl, level, camera);

         gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
         gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
         
         ui=new Interface();
         ui.init(gl);
         
         Timer.lastTime=0;
         
    }

}
