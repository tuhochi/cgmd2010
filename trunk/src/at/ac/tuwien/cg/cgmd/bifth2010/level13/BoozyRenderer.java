package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class BoozyRenderer extends GLSurfaceView implements Renderer{

	private Quad quad;
	private Context context;
	
	float x = 0;
	float y = 0;
	
	float charMoveY = 0.0f;
	float charMoveX = 0.0f;
	
	public BoozyRenderer(Context context){
		super(context);
		//this.context = context;
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		this.context = context;
		quad = new Quad();
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		
		
		y += charMoveY;
		x += charMoveX;
		gl.glTranslatef(x, y, -15.0f);
		quad.draw(gl);
		//gl.glTranslatef(10, 10, -15.0f);
		//quad.draw(gl);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0)
			height = 1;
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity(); //Reset projection matrix
		
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity(); //Reset world matrix
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Default setup taken from nehe android port tutorial
		quad.loadGLTexture(gl, this.context,true);
		
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(1.0f, 1.0f, 0.0f, 0.5f);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
	
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	
	
	}

	public boolean onTouchEvent(MotionEvent event){
		float currentX = event.getX();
		float currentY = event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			int upAreaY = this.getHeight()/10;
			int downAreaY = this.getHeight()-upAreaY;
			int leftAreaX = this.getWidth()/10;
			int rightAreaX = this.getWidth()-leftAreaX;
			
			
			if (currentY < upAreaY){
				charMoveY = 0.01f;
				charMoveX = 0;
			}else if(currentY > downAreaY){
				charMoveY = -0.01f;
				charMoveX = 0;
			}else if(currentX < leftAreaX){
				charMoveX = -0.01f;
				charMoveY = 0;
			}else if(currentX > rightAreaX){
				charMoveX = 0.01f;
				charMoveY = 0;
			}else{
				charMoveX = 0;
				charMoveY = 0;
			}
			
			
			
			
		}
		
		
		
		
		
		return true;

	}
	
	
	
	
}
