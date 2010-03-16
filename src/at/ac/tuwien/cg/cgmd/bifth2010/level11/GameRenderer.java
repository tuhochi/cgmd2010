package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class GameRenderer implements Renderer {
	private static final String LOG_TAG = GameRenderer.class.getSimpleName();
    
    private float _xAngle;
    private float _yAngle;
    
    private int _w = 320;
    private int _h = 480;
    private float _width = 320f;
    private float _height = 480f;

    GL10 gl;
   
    Texture texture;
    Context context;
    Square square;
    
	private float xrot;				//X Rotation ( NEW )
	private float yrot;				//Y Rotation ( NEW )
	private float zrot;				//Z Rotation ( NEW )
	
	public GameRenderer(Context context)
	{
		this.context = context;
	}

	
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i(LOG_TAG, "onSurfaceCreated()");
        
        this.gl = gl;
        
        gl.glMatrixMode(GL10.GL_PROJECTION);
        
        gl.glOrthof(0.0f, _width, 0.0f, _height, -1.0f, 100.0f);
        		
        gl.glViewport(0, 0, (int) _width, (int) _height);
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
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        //initTextures();
        
        square = new Square();
        square.loadGLTexture(gl, this.context);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping 
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		
        

    //initSquare();
   // square = new Square();
    
   // square = new Square();
 
    

 
 //  square.setTextureId(R.drawable.l00_map_landscape);
  // square.setTextures(texture);
    
    }
    
    private void initTextures() {
    	texture = new Texture(gl, context);
    	texture.add(R.drawable.l00_map_landscape);
    	//texture.add(R.drawable.l11_pedestrian_hair);    
        //texture.add(R.drawable.l11_pedestrian_head);
        //texture.add(R.drawable.l11_pedestrian_shadow);
        //texture.add(R.drawable.l11_pedestrian_torso); 
        texture.loadTextures();
	}


	@Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
       // Log.i(LOG_TAG, "onSurfaceChanged()");
        _width = w;
        _height = h;
        _w = w;
        _h = h;
        gl.glOrthof(0.0f, _width, 0.0f, _height, -1.0f, 100.0f);
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
	
        		//Clear Screen And Depth Buffer
        		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);	
        		gl.glLoadIdentity();					//Reset The Current Modelview Matrix
        		
        		//Drawing
        		gl.glTranslatef(100.0f, 100.0f, 0.0f);		//Move 5 units into the screen
        		gl.glScalef(50.0f, 50.0f, 50.0f); 			//Scale the Cube to 80 percent, otherwise it would be too large for the screen
        		
        		//Rotate around the axis based on the rotation matrix (rotation, x, y, z)
        		//gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f);	//X
        		//gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f);	//Y
        		gl.glRotatef(zrot, 0.0f, 0.0f, 1.0f);	//Z
        				
        		square.draw(gl);							//Draw the Cube	
        		
        		//Change rotation factors (nice rotation)
        		xrot += 0.3f;
        		yrot += 0.2f;
        		zrot += 0.4f;
    }
}
