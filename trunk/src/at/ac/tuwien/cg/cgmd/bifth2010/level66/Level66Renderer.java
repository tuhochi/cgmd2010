package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class Level66Renderer implements Renderer {

	// model for testing purpose only
	private Model testModel;
	
	// screen dimension
    private float _width = 320f;
    private float _height = 480f;
	
	@Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // preparation
  
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        
        float size = 1.0f; 
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
	    gl.glClearColor(0f, 0f, 0f, 1.0f);
	    
	    // enable the differentiation of which side may be visible 
	    gl.glEnable(GL10.GL_CULL_FACE);
	    // which is the front? the one which is drawn counter clockwise
	    gl.glFrontFace(GL10.GL_CCW);
	    // which one should NOT be drawn
	    gl.glCullFace(GL10.GL_BACK);
	    
	    // enable blending for anti-aliasing and transparency
	    // TODO: does not work yet
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glShadeModel(GL10.GL_LINE_SMOOTH);
	    gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
	    
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	    
	    testModel = new Model();
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
       testModel.render(gl);
    }
	
	public void moveRight()
	{
		testModel.move( 0.05f, 0.0f, 0.0f );
	}
	
	public void moveLeft()
	{
		testModel.move( -0.05f, 0.0f, 0.0f);
	}
	
	public void moveUp()
	{
		testModel.move( 0.0f, 0.05f, 0.0f);
	}
	
	public void moveDown()
	{
		testModel.move( 0.0f, -0.05f, 0.0f);
	}
	
	
	/*
	  // a raw buffer to hold indices allowing a reuse of points.
    private ShortBuffer _indexBuffer;
    
    // a raw buffer to hold the vertices
    private FloatBuffer _vertexBuffer;
    
    // a raw buffer to hold the colors
    private FloatBuffer _colorBuffer;
    
    private int _nrOfVertices = 0;

    private float _xAngle;
    private float _yAngle;
    
    private float _width = 320f;
    private float _height = 480f;
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // preparation
  
        gl.glMatrixMode(GL10.GL_PROJECTION);
        float size = .01f * (float) Math.tan(Math.toRadians(45.0) / 2); 
        float ratio = _width / _height;
        // perspective:
        gl.glFrustumf(-size, size, -size / ratio, size / ratio, 0.01f, 100.0f);
        // orthographic:
        //gl.glOrthof(-1, 1, -1 / ratio, 1 / ratio, 0.01f, 100.0f);
        gl.glViewport(0, 0, (int) _width, (int) _height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
	    // define the color we want to be displayed as the "clipping wall"
	    gl.glClearColor(0f, 0f, 0f, 1.0f);
	    
	    // enable the differentiation of which side may be visible 
	    gl.glEnable(GL10.GL_CULL_FACE);
	    // which is the front? the one which is drawn counter clockwise
	    gl.glFrontFace(GL10.GL_CCW);
	    // which one should NOT be drawn
	    gl.glCullFace(GL10.GL_BACK);
	    
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glEnable(GL10.GL_SMOOTH);
	    gl.glEnable(GL10.GL_LINE_SMOOTH);
	    
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	
	    initTriangle();
    }
    
    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        _width = w;
        _height = h;
        gl.glViewport(0, 0, w, h);
    }
    
    public void setXAngle(float angle) {
        _xAngle = angle;
    }
    
    public float getXAngle() {
        return _xAngle;
    }
    
    public void setYAngle(float angle) {
        _yAngle = angle;
    }
    
    public float getYAngle() {
        return _yAngle;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // clear the color buffer and the depth buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
    
	    gl.glLoadIdentity();
	    gl.glTranslatef(0.0f, -1f, -1.0f + -1.5f);
	    // set rotation
	    gl.glRotatef(-_xAngle, 1f, 0f, 0f);
	    gl.glRotatef(-_yAngle, 0f, 1f, 0f);
	    gl.glDrawElements(GL10.GL_LINES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
    
    private void initTriangle() {
    	
    	 float[] coords = {
                 0.0f, 0.0f, 0.0f, // 0
                 0.5f, 0.0f, 0.0f, // 1
                 0.0f, 0.5f, 0.0f, // 2
                 0.0f, 0.0f, -0.5f // 3
         };
    	 /////
        float[] coords = {
                -0.5f, -0.5f, 0.5f, // 0
                0.5f, -0.5f, 0.5f, // 1
                0f, -0.5f, -0.5f, // 2
                0f, 0.5f, 0f, // 3
        };
        //////
        _nrOfVertices = coords.length;
        
        float[] colors = {
                1f, 0f, 0f, 1f, // point 0 red
                0f, 1f, 0f, 1f, // point 1 green
                0f, 0f, 1f, 1f, // point 2 blue
                1f, 1f, 1f, 1f // point 3 white
        };
        
        short[] indices = new short[] {
                0, 1,
                0, 2,
                0, 3
        };
        //////
        short[] indices = new short[] {
                0, 1, 3, // rwg
                0, 2, 1, // rbg
                0, 3, 2, // rbw
                1, 2, 3, // bwg
        };
        //////

        // float has 4 bytes, coordinate * 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        
        // short has 2 bytes, indices * 2 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indexBuffer = ibb.asShortBuffer();
        
        // float has 4 bytes, colors (RGBA) * 4 bytes
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        _colorBuffer = cbb.asFloatBuffer();
        
        _vertexBuffer.put(coords);
        _indexBuffer.put(indices);
        _colorBuffer.put(colors);
        
        _vertexBuffer.position(0);
        _indexBuffer.position(0);
        _colorBuffer.position(0);
    }

	
	/*
	private float _posX;
	private float _posY;
	
	private float _velocityX;
	private float _velocityY;
	
	private float _rotationX;
	private float _rotationY;
	
	private float _posStepSizeX = 0.01f;
	private float _posStepSizeY = 0.01f;
	
	private ShortBuffer _indexBuffer;
	 
	// a raw buffer to hold the vertices
	private FloatBuffer _vertexBuffer;
	 
	private short[] _indicesArray = { 0,1,2,3,6,7,4,5,0,1,5,3,7 };
	private int _nrOfVertices = 8;
	 
	private GLU glu = new GLU();
	 
	private void initTriangle() {
	    // float has 4 bytes
	    ByteBuffer vbb = ByteBuffer.allocateDirect(_nrOfVertices * 3 * 4);
	    vbb.order(ByteOrder.nativeOrder());
	    _vertexBuffer = vbb.asFloatBuffer();
	 
	    // short has 2 bytes
	    ByteBuffer ibb = ByteBuffer.allocateDirect(_indicesArray.length * 2);
	    ibb.order(ByteOrder.nativeOrder());
	    _indexBuffer = ibb.asShortBuffer();
	 
	 ////////////
	    float[] coords = {
	        -1.0f, 0.0f, 0.0f,
	        1.0f, 0.0f, 0.0f,
	        0.0f, -1.0f, 0.0f,
	        0.0f, 1.0f, 0.0f,
	        0.0f, 0.0f, 0.0f,
	        0.0f, 0.0f, -3.0f
	    };
	   ///
	    
	    float[] coords = {
		        0.0f, 0.0f, 0.0f,
		        1.0f, 0.0f, 0.0f,
		        0.0f, 1.0f, 0.0f,
		        1.0f, 1.0f, 0.0f,
		        0.0f, 0.0f, 1.0f,
		        1.0f, 0.0f, 1.0f,
		        0.0f, 1.0f, 1.0f,
		        1.0f, 1.0f, 1.0f
		    };
	 
	    _vertexBuffer.put(coords);
	    _indexBuffer.put(_indicesArray);
	 
	    _vertexBuffer.position(0);
	    _indexBuffer.position(0);
	}
	
    public void moveLeft()
    {
    	_posX = _posX - _posStepSizeX;
    	if (_posX < -1.0f)
    		_posX = -1.0f;
    }
    
    public void moveRight()
    {
    	_posX = _posX + _posStepSizeX;
    	if (_posX > 1.0f)
    		_posX = 1.0f;
    }
    
    public void moveDown()
    {
    	_posY = _posY - _posStepSizeY;
    	if (_posY < -1.0f)
    		_posY = -1.0f;
    }
	
    public void moveUp()
    {
    	_posY = _posY + _posStepSizeY;
    	if (_posY > 1.0f)
    		_posY = 1.0f;
    }
    
    
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		 
	    // set the color of our element
	    gl.glColor4f(0.5f, 0f, 0f, 0.5f);
	    gl.glLoadIdentity();
	    
	    gl.glTranslatef(_posX, _posY, 0.0f);
	    gl.glScalef(0.2f, 0.2f, 0.2f);
	    
	    // define the vertices we want to draw
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
	    // finally draw the vertices
	    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, _indicesArray.length, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
	    
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		//////////
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(gl.GL_PROJECTION);
	    gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / height, -1.0f, 10.0f);
		gl.glMatrixMode(gl.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    ////////////
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
	    //float size = .01f * (float) Math.tan(Math.toRadians(45.0) / 2);
		float size = 1.0f;
	    float ratio = width / height;
	    // perspective:
	    gl.glFrustumf(-size, size, -size / ratio, size / ratio, -5.0f, 5.0f);
	    // orthographic:
	    //gl.glOrthof(-1, 1, -1 / ratio, 1 / ratio, 0.01f, 100.0f);
	    gl.glViewport(0, 0, (int) width, (int) height);
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
		//gl.glOrthof(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		initTriangle();
	}
	*/
}
