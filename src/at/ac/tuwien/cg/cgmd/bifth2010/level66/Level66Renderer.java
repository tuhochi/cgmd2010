/**
 * Flight66 - a trip to hell
 * 
 * @author brm, dwi
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class Level66Renderer implements Renderer {

	// model for testing purpose only
	private Model testModel;
	private Context mContext;
	
	// screen dimension
    private float _width = 320f;
    private float _height = 480f;
    
    private float[]  _globalAmbient = { 0.5f, 0.5f, 0.5f, 1.0f };
    
    private float[] _lightAmbient = { 0.0f, 0.0f, 0.0f, 1.0f };
    private float[] _lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] _lightSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };

    private float[] _lightPosition = { -20.0f, 20.0f, -20.0f };
    private float[] _matSpecular = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] _matShininess = { 50.0f };
	
	public Level66Renderer(Context context){
		this.mContext = context;
	}
	
	@Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // preparation
  
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        
        float ratio = _width / _height;
        // perspective:
        GLU.gluLookAt(gl, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        GLU.gluPerspective(gl, 60.0f, 1.0f * ratio, 1.0f, 20.0f);
        // orthographic:
        //gl.glOrthof(-1, 1, -1 / ratio, 1 / ratio, 0.01f, 100.0f);
        
        gl.glViewport(0, 0, (int) _width, (int) _height);
        
        // switch to modelview
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
	    // define the color we want to be displayed as the "clipping wall"
	    gl.glClearColor(0.235f, 0.329f, 0.408f, 1.0f);
	    
	    // enable the differentiation of which side may be visible 
	    gl.glEnable(GL10.GL_CULL_FACE);
	    // which is the front? the one which is drawn counter clockwise
	    gl.glFrontFace(GL10.GL_CCW);
	    // which one should NOT be drawn
	    gl.glCullFace(GL10.GL_BACK);
	    
	    // enable blending for anti-aliasing and transparency
	    // TODO: does not work yet
	    //gl.glEnable(GL10.GL_BLEND);
	    //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glShadeModel(GL10.GL_SMOOTH);
	    //gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
	    
	    
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	    gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	    
	    // gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, _matSpecular, 0);
	    // gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, _matShininess, 0);
	   
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, _lightAmbient, 0);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, _lightDiffuse, 0);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, _lightSpecular, 0);
	    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, _lightPosition, 0);
	    
	   
	    gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, _globalAmbient, 0);

	    gl.glEnable(GL10.GL_LIGHTING);
	    gl.glEnable(GL10.GL_LIGHT0);
	    gl.glEnable(GL10.GL_COLOR_MATERIAL);
	 
		//load and render model
	    testModel = new PlayerAircraft(mContext);
	    //testModel = new Model("l66_baum.obj", mContext);
	    Sound.playSound(2.0f);
    }
	
	@Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        _width = w;
        _height = h;
        
        float ratio = w / h;
        GLU.gluPerspective(gl, 60.0f, 1.0f * ratio, 1.0f, 20.0f);
        
        gl.glViewport(0, 0, w, h);
    }
	
	@Override
    public void onDrawFrame(GL10 gl) {
        // clear the color buffer and the depth buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
       
       // render model
       if( testModel instanceof PlayerAircraft)
    	   ((PlayerAircraft) testModel).move();
       
       testModel.render(gl);
    }
	
	public void moveRight()
	{
		 if( testModel instanceof PlayerAircraft)
			 ((PlayerAircraft) testModel).moveRight();
	}
	
	public void moveLeft()
	{
		 if( testModel instanceof PlayerAircraft)
			 ((PlayerAircraft) testModel).moveLeft();
	}
	
	public void moveUp()
	{
		if( testModel instanceof PlayerAircraft)
			 ((PlayerAircraft) testModel).moveUp();
	}
	
	public void moveDown()
	{
		if( testModel instanceof PlayerAircraft)
			 //((PlayerAircraft) testModel).moveDown();
			((PlayerAircraft) testModel).moveDown();
	}
	
	public void moveSpecial()
	{
		if( testModel instanceof PlayerAircraft)
			 //((PlayerAircraft) testModel).moveDown();
			((PlayerAircraft) testModel).specialMoveRoll();
	}
}
