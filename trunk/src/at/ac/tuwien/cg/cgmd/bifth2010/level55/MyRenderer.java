package at.ac.tuwien.cg.cgmd.bifth2010.level55;

//import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

class MyRenderer implements MyOpenGLView.Renderer {
	
	Level level;
	Player player;
	Interface ui;
	Texture interfaceTexture;
	static Context context;
	
	static public void setContext(Context _context) {
		context=_context;
	}
	
    public MyRenderer(Player _player) {
    	player=_player;
    }

    @Override
	public void onDrawFrame(GL10 gl) {  	
    	Timer.update();
    	
    //	StateManager.update(Timer.dT);
    	
    	//gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    	
    	level.update(Timer.dT);
    	player.update(Timer.dT);
    	level.draw(gl);
    	player.draw(gl);
    	
    	
    	ui.draw(gl); 	
    }

    @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */

         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         
         float ratio=(float)height/(float)width;
         float numTilesHorizontal=15.0f;
         gl.glOrthof(0.0f, numTilesHorizontal, numTilesHorizontal*ratio, 0.0f, 0.0f, 5.0f);
         
         Quad.screenWidth=numTilesHorizontal;
         Quad.screenHeight=numTilesHorizontal*ratio;
         
         ui.screenWidth=numTilesHorizontal;
         ui.screenHeight=numTilesHorizontal*ratio;
         
         TileLayer.screenWidth=numTilesHorizontal;
         TileLayer.screenHeight=numTilesHorizontal*ratio;
         
         gl.glMatrixMode(GL10.GL_MODELVIEW);
         gl.glLoadIdentity();
    }

    @Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	Log.d("Renderer","onSurfaceCreate");
    	
    	gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
    	
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

         gl.glClearColor(0,0,0,0);
         gl.glDisable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
         //gl.glEnable(GL10.GL_BLEND);
         
         gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
         gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	
         gl.glFrontFace(GL10.GL_CW);
         
         Texture.cleanUp();
         Texture.setGL(gl);
         
         level=new Level();
         level.init(gl, context);
         
         //player=new Player();
         player.init(gl, level);
         
         ui=new Interface();
         ui.init(gl);
    }

}
