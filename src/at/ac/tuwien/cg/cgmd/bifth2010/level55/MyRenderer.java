package at.ac.tuwien.cg.cgmd.bifth2010.level55;

//import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
	static float numTilesVertical=10.0f;
	
	static float resX;
	static float resY;
	
	/**
	 * Sets the Activity context
	 * @param _context The Activity context
	 */
	static public void setContext(Context _context) {
		context=_context;
	}
	
	/**
	 * Constructor
	 */
	public MyRenderer() {
		super();
		
    }

    /**
     * The main draw method
     * @param gl The OpenGL context
     */
	public void onDrawFrame(GL10 gl) {
    	MyTimer.update();
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    	
    	float dt = MyTimer.dT*0.001f;
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

    /**
     * Gets called on surface changes
     * @param gl The OpenGL context
     * @param width Surface width
     * @param height Surface height
     */
	public void onSurfaceChanged(GL10 gl, int width, int height) {  	
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
         
         float ratio=(float)width/(float)height;
         numTilesHorizontal=numTilesVertical*ratio;
         
         gl.glOrthof(0.0f, numTilesHorizontal, numTilesVertical, 0.0f, 0.5f, 5.0f);
         //gl.glOrthof(-20f, 20, 20, -20.0f, 00.0f, 5.0f);
         
         camera.offset[0] = (float) Math.floor(MyRenderer.numTilesHorizontal*0.5f);
         
         Quad.screenWidth=numTilesHorizontal;
         Quad.screenHeight=numTilesVertical;
         
         ui.screenWidth=numTilesHorizontal;
         ui.screenHeight=numTilesVertical;
         
         TileLayer.screenWidth=numTilesHorizontal;
         TileLayer.screenHeight=numTilesVertical;
         
         gl.glMatrixMode(GL10.GL_MODELVIEW);
         gl.glLoadIdentity();
    }

    /**
     * Gets called on surface creation, loads level and textures
     * @param gl The OpenGL context
     */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {   	
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
         
         SharedPreferences mPrefs = ((Activity) context).getPreferences(Context.MODE_PRIVATE);
         float posx=mPrefs.getFloat("L55_POSX", 2.0f);
         float posy=mPrefs.getFloat("L55_POSY", 5.0f);
         
         String coinStates=mPrefs.getString("L55_COINSTATES", ""); 
         
         level=new Level();
         level.init(gl, context, coinStates);
         
         camera = new Camera(level);
         
         player=new Player();
         player.init(gl, level, camera, posx, posy);

         gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
         gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
         
         ui=new Interface();
         ui.init(gl);
         
         MyTimer.lastTime=0;
         
    }

}
