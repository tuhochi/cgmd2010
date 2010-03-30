package at.ac.tuwien.cg.cgmd.bifth2010.level55;

//import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class Renderer implements MyOpenGLView.Renderer {
	
	Level level;
	
    public Renderer() {  
    }

    @Override
	public void onDrawFrame(GL10 gl) {  	
    	Timer.update();
    	
    //	StateManager.update(Timer.dT);
    	
    	//gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    	gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    	
    	level.update(Timer.dT);
    	
    	level.render(gl);
    	
    	
    //	StateManager.current_state.update(Timer.dT);
    //	StateManager.current_state.render(gl);
   
    //	StateManager.render(gl);
    }

  /*  public int[] getConfigSpec() {
        int[] configSpec = {
                EGL10.EGL_RED_SIZE,      8,
                EGL10.EGL_GREEN_SIZE,    8,
                EGL10.EGL_BLUE_SIZE,     8,
                EGL10.EGL_ALPHA_SIZE,    8,
                //EGL10.EGL_DEPTH_SIZE,   16,
                EGL10.EGL_NONE
        };
        return configSpec;
    }*/

    @Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */

     //    float ratio = (float) width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         
         //gl.glOrthof(0.0f, 480.0f, 320.0f, 0.0f, 0.0f, 5.0f);
         gl.glOrthof(0.0f, width, height, 0.0f, 0.0f, 5.0f);
         //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    @Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);

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
         gl.glEnable(GL10.GL_BLEND);
         //gl.glEnable(GL10.GL_DEPTH_TEST);
         
         Texture.setGL(gl);
         
         level=new Level();
         
         level.init(gl);
    }
    
	/*public boolean onTouch(View arg0, MotionEvent arg1) {
		//Level.onTouch(event);
    	
    	synchronized(this) {
	    	try {
				this.wait(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	return true;
	}*/

}
